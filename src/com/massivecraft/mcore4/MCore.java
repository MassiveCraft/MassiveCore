package com.massivecraft.mcore4;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.mcore4.adapter.InventoryAdapter;
import com.massivecraft.mcore4.adapter.ItemStackAdapter;
import com.massivecraft.mcore4.adapter.MongoURIAdapter;
import com.massivecraft.mcore4.lib.gson.Gson;
import com.massivecraft.mcore4.lib.gson.GsonBuilder;
import com.massivecraft.mcore4.lib.mongodb.MongoURI;
import com.massivecraft.mcore4.persist.Persist;
import com.massivecraft.mcore4.util.PlayerUtil;
import com.massivecraft.mcore4.util.Txt;

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
	// DERP
	// -------------------------------------------- //
	
	public static Random random = new Random();
	public static Gson gson = getGsonBuilder().create();
	
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
		.registerTypeAdapter(Inventory.class, new InventoryAdapter());
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
