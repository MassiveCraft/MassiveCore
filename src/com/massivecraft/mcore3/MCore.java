package com.massivecraft.mcore3;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.mcore3.cmd.Cmd;
import com.massivecraft.mcore3.gson.InventoryTypeAdapter;
import com.massivecraft.mcore3.gson.ItemStackAdapter;
import com.massivecraft.mcore3.gson.MongoURIAdapter;
import com.massivecraft.mcore3.lib.gson.GsonBuilder;
import com.massivecraft.mcore3.lib.mongodb.MongoURI;
import com.massivecraft.mcore3.persist.One;
import com.massivecraft.mcore3.persist.Persist;
import com.massivecraft.mcore3.util.LibLoader;
import com.massivecraft.mcore3.util.PlayerUtil;
import com.massivecraft.mcore3.util.Txt;

public class MCore extends JavaPlugin
{
	InternalListener listener;
	
	// -------------------------------------------- //
	// PERSIST
	// -------------------------------------------- //
	private static Map<Object, Persist> persistInstances = new HashMap<Object, Persist>();
	public static Map<Object, Persist> getPersistInstances() { return persistInstances; }
	public static Persist getPersist(Object owner) { return persistInstances.get(owner); }
	public static void removePersist(Object owner) { persistInstances.remove(owner); }
	public static void createPersist(Object owner)
	{
		if (persistInstances.containsKey(owner)) return;
		persistInstances.put(owner, new Persist());
	}
	
	// -------------------------------------------- //
	// CMD
	// -------------------------------------------- //
	private static Map<Object, Cmd> cmdInstances = new HashMap<Object, Cmd>();
	public static Map<Object, Cmd> getCmdInstances() { return cmdInstances; }
	public static Cmd getCmd(Object owner) { return cmdInstances.get(owner); }
	public static void removeCmd(Object owner) { cmdInstances.remove(owner); }
	public static void createCmd(Object owner)
	{
		if (cmdInstances.containsKey(owner)) return;
		cmdInstances.put(owner, new Cmd());
	}
	
	// -------------------------------------------- //
	// ONE
	// -------------------------------------------- //
	private static Map<MPlugin, One> oneInstances = new HashMap<MPlugin, One>();
	public static Map<MPlugin, One> getOneInstances() { return oneInstances; }
	public static One getOne(MPlugin owner) { return oneInstances.get(owner); }
	public static void removeOne(MPlugin owner) { oneInstances.remove(owner); }
	public static void createOne(MPlugin owner)
	{
		if (oneInstances.containsKey(owner)) return;
		oneInstances.put(owner, new One(owner));
	}
	
	// -------------------------------------------- //
	// LIBLOADER
	// -------------------------------------------- //
	private static Map<MPlugin, LibLoader> libLoaderInstances = new HashMap<MPlugin, LibLoader>();
	public static Map<MPlugin, LibLoader> getLibLoaderInstances() { return libLoaderInstances; }
	public static LibLoader getLibLoader(MPlugin owner) { return libLoaderInstances.get(owner); }
	public static void removeLibLoader(MPlugin owner) { libLoaderInstances.remove(owner); }
	public static void createLibLoader(MPlugin owner)
	{
		if (libLoaderInstances.containsKey(owner)) return;
		libLoaderInstances.put(owner, new LibLoader(owner));
	}
	
	// -------------------------------------------- //
	// DERP
	// -------------------------------------------- //
	
	public static Random random = new Random();
	
	
	
	@Override
	public void onDisable()
	{
		// Avoid memleak by clearing ???
		// Or will this trigger errors???
		//Persist.getRealms().clear();
	}

	@Override
	public void onEnable()
	{
		logPrefix = "["+this.getDescription().getName()+"] ";
		
		PlayerUtil.populateAllVisitorNames();
		
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		getPersistInstances().clear();
		
		// Register events
		this.listener = new InternalListener(this);
	}
	
	public static GsonBuilder getGsonBuilder()
	{
		return new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT)
		.registerTypeAdapter(MongoURI.class, MongoURIAdapter.get())
		.registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
		.registerTypeAdapter(Inventory.class, new InventoryTypeAdapter());
	}
	
	// -------------------------------------------- //
	// LOGGING
	// -------------------------------------------- //
	private static String logPrefix = null;
	public static void log(Object... msg)
	{
		log(Level.INFO, msg);
	}
	public static void log(Level level, Object... msg)
	{
		Logger.getLogger("Minecraft").log(level, logPrefix + Txt.implode(msg, " "));
	}
}
