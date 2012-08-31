package com.massivecraft.mcore4;

import java.lang.reflect.Modifier;
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
	public static Random random = new Random();
	public static Gson gson = getGsonBuilder().create();
	
	InternalListener listener;

	@Override
	public void onEnable()
	{
		logPrefix = "["+this.getDescription().getName()+"] ";
		
		PlayerUtil.populateAllVisitorNames();
		
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		Persist.instances.clear();
		
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
