package com.massivecraft.mcore4;

import java.lang.reflect.Modifier;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore4.adapter.InventoryAdapter;
import com.massivecraft.mcore4.adapter.ItemStackAdapter;
import com.massivecraft.mcore4.adapter.MongoURIAdapter;
import com.massivecraft.mcore4.persist.Persist;
import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.store.Db;
import com.massivecraft.mcore4.store.MStore;
import com.massivecraft.mcore4.store.USelColl;
import com.massivecraft.mcore4.usys.AspectColl;
import com.massivecraft.mcore4.usys.MultiverseColl;
import com.massivecraft.mcore4.usys.cmd.CmdUsys;
import com.massivecraft.mcore4.util.PlayerUtil;
import com.massivecraft.mcore4.xlib.gson.Gson;
import com.massivecraft.mcore4.xlib.gson.GsonBuilder;
import com.massivecraft.mcore4.xlib.mongodb.MongoURI;

public class MCore extends MPlugin
{
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
		.registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
		.registerTypeAdapter(Inventory.class, new InventoryAdapter());
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
	
	@Override
	public void onEnable()
	{
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		// Note this one must be before preEnable. dooh.
		Persist.instances.clear();
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
		USelColl.i.init(); // TODO: Remove and deprecate!? possibly yes... how soon?
		MultiverseColl.i.init();
		AspectColl.i.init();
		
		// Register commands
		this.cmdUsys = new CmdUsys();
		this.cmdUsys.register(true);
		
		this.postEnable();
	}
	
}
