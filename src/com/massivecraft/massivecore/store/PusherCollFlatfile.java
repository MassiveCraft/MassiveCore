package com.massivecraft.massivecore.store;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * This pusher looks for changes in a flatfile database system.
 * Hopefully it is quicker than the poller.
 */
public class PusherCollFlatfile extends Thread implements PusherColl
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final Kind<?>[] EVENT_TYPES = {StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY};
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String folderUri;
	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private final Coll<?> coll;
	private final Set<String> handledIds = new HashSet<>();
	
	// -------------------------------------------- //
	// OVERRIDE: THREAD
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		//MassiveCore.get().log("Starting Pusher for " + coll.getBasename());
		while (true)
		{
			try
			{
				WatchKey key = this.watcher.take();
				Path dir = this.keys.get(key);
				if (dir == null)
				{
					//System.err.println("WatchKey not recognized!!");
					continue;
				}
				
				for (WatchEvent<?> event : key.pollEvents())
				{
					handleEvent((WatchEvent<Path>) event, dir);
				}
				
				boolean valid = key.reset();
				if ( ! valid) this.keys.remove(key);
			}
			catch (InterruptedException e)
			{
				// We've been interrupted. Lets bail.
				return;
			}
			catch (Exception e)
			{
				System.out.println("Pusher error for" + this.coll.getDebugName());
				e.printStackTrace();
			}
			finally
			{
				handledIds.clear();
			}
		}
	}
	
	public void handleEvent(WatchEvent<Path> event, Path root) throws IOException
	{
		// Note on my computer running OSX El Capitan
		// updates is sent every 10 seconds.
		// So the information /could/ be a little less than 10 seconds old.
		// Thus we cannot trust the kind of the modification event.
		// But only trust that the file has been modified in some way.
			
		// Context for directory entry event is the file name of entry
		Path context = event.context();
		Path fullPath = root.resolve(context);
		File file = fullPath.toFile();
		long mtime = file.lastModified();
		
		// Id
		String id = context.toString();
		if ( ! isIdOk(id)) return;
		id = id.substring(0, id.length() - JsonFileFilter.DOTJSON.length());
		
		// Most registered modifications here will actually be something done locally.
		// So most of the time we should just ignore this.
		Modification mod = this.coll.examineIdFixed(id, mtime, true, true);
		
		//System.out.println("old: " + coll.getMetaCreative(id).getMtime() + "new: " + mtime);
		//System.out.println(String.format("Coll %s found %s on %s", this.coll.getBasename(), mod, id));
		
		// At LOCAL_ATTACH we get NONE.
		// At LOCAL_ALTER we get NONE.
		// At LOCAL_DETACH we get UNKNOWN.
		// At isDefault we get NONE.
		// At REMOTE_DETACH we get REMOTE_DETACH.
		// At REMOTE_ATTACH we get REMOTE_ATTACH (?)
		// At REMOTE_ALTER we get REMOTE_ALTER.
		
		switch(mod)
		{
			// It was modified locally.
			case NONE:
			case UNKNOWN:
			case UNKNOWN_LOG:
			case UNKNOWN_CHANGED:
				return;
				
			// It was modified remotely.
			case REMOTE_ATTACH:
			case REMOTE_DETACH:
			case REMOTE_ALTER:
				// Usually we don't use the results of an async examination
				// because the entity might be modified locally during the examination.
				// But now we only care about  remote modifications. Which should be accurate.
				// However from examination to synchronization there can go a whole tick.
				// And if a remote detach occurs during that tick, the system would break if we said the modification was alter or attach.
				// Thus we cannot be sure about the modification, when we get to the synchronization point.
				coll.putIdentifiedModificationFixed(id, Modification.UNKNOWN);
				break;
				
			// Should not happen
			case LOCAL_ALTER:
			case LOCAL_ATTACH:
			case LOCAL_DETACH:
				break;
		}
	}

	public boolean isIdOk(String id)
	{
		// Special files made by the OS and some programs starts with '.'
		if (id.charAt(0) == '.') return false;
		
		// It must be a json file
		if ( ! id.endsWith(JsonFileFilter.DOTJSON)) return false;
		
		// If adding this id DIDN'T have an effect.
		// It was already there and already handled.
		if ( ! handledIds.add(id)) return false;
		
		return true;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: PUSHER
	// -------------------------------------------- //

	@Override
	public void init()
	{
		this.start();
	}

	@Override
	public void deinit()
	{
		this.interrupt();
	}
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	public void register(Path path) throws IOException
	{
		if (Files.notExists(path)) throw new IllegalArgumentException(path.toString() + " does not exist.");
		WatchKey key = path.register(watcher, EVENT_TYPES);
		//System.out.format("register: %s\n", path);
		keys.put(key, path);
	}
	
	public void registerAll(final Path start) throws IOException
	{
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
			{
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PusherCollFlatfile(Coll<?> coll) throws IOException
	{
		Db db = coll.getDb();
		if ( ! (db instanceof DbFlatfile)) throw new IllegalArgumentException("Coll doesn't use flatfile database");
		this.folderUri = db.getDbName() + "/" + coll.getBasename();
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<>();
		this.coll = coll;
		
		// We must make sure that the paths exists,
		// otherwise we cannot register to listen for changes.
		// So now we'll have some empty directories.
		Path path = Paths.get(this.folderUri);
		path.toFile().mkdirs();
		this.register(path);
	}

}
