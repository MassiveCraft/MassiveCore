package com.massivecraft.mcore5;

import java.lang.reflect.Modifier;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore5.adapter.InventoryAdapter;
import com.massivecraft.mcore5.adapter.ItemStackAdapter;
import com.massivecraft.mcore5.adapter.MongoURIAdapter;
import com.massivecraft.mcore5.adapter.PSAdapter;
import com.massivecraft.mcore5.cmd.CmdMcore;
import com.massivecraft.mcore5.store.Coll;
import com.massivecraft.mcore5.store.Db;
import com.massivecraft.mcore5.store.MStore;
import com.massivecraft.mcore5.usys.AspectColl;
import com.massivecraft.mcore5.usys.MultiverseColl;
import com.massivecraft.mcore5.usys.cmd.CmdUsys;
import com.massivecraft.mcore5.util.PlayerUtil;
import com.massivecraft.mcore5.xlib.gson.Gson;
import com.massivecraft.mcore5.xlib.gson.GsonBuilder;
import com.massivecraft.mcore5.xlib.mongodb.MongoURI;

public class MCore extends MPlugin
{
	// -------------------------------------------- //
	// COMMON CONSTANTS
	// -------------------------------------------- //
	
	public final static String INSTANCE = "instance";
	public final static String DEFAULT = "default";
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static Random random = new Random();
	public static Gson gson = getMCoreGsonBuilder().create();
	
	public static GsonBuilder getMCoreGsonBuilder()
	{
		return new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT)
		.registerTypeAdapter(MongoURI.class, MongoURIAdapter.get())
		.registerTypeAdapter(ItemStack.class, ItemStackAdapter.get())
		.registerTypeAdapter(Inventory.class, InventoryAdapter.get())
		.registerTypeAdapter(PS.class, new PSAdapter());
	}
	
	public static String getServerId() { return Conf.serverid; }
	private static Db<?> db;
	public static Db<?> getDb() { return db; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static MCore p;
	public MCore()
	{
		p = this;
	}
	
	// -------------------------------------------- //
	// NON STATIC :)
	// -------------------------------------------- //

	private Runnable collTickTask = new Runnable()
	{
		public void run()
		{
			for (Coll<?, ?> coll : Coll.instances)
			{
				coll.onTick();
			}
		}
	};
	
	public InternalListener internalListener;
	public CmdUsys cmdUsys;
	public CmdMcore cmdMcore;
	
	@Override
	public void onEnable()
	{
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		// Note this one must be before preEnable. dooh.
		Coll.instances.clear();
		
		if ( ! preEnable()) return;
		
		Conf.i.load();
		
		// Setup the default database
		db = MStore.getDb(Conf.dburi);
		
		// Setup PlayerUtil and it's events
		new PlayerUtil(this);
		
		// Register events
		this.internalListener = new InternalListener(this);
		
		// Schedule the collection ticker.
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.collTickTask, 1, 1);
		
		// Init internal collections
		MultiverseColl.i.init();
		AspectColl.i.init();
		
		// Register commands
		this.cmdUsys = new CmdUsys();
		this.cmdUsys.register(this, true);
		
		this.cmdMcore = new CmdMcore();
		this.cmdMcore.register(this, true);
		
		this.postEnable();
	}
	
}
