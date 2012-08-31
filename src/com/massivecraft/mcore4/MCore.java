package com.massivecraft.mcore4;

import java.lang.reflect.Modifier;
import java.util.Random;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore4.adapter.InventoryAdapter;
import com.massivecraft.mcore4.adapter.ItemStackAdapter;
import com.massivecraft.mcore4.adapter.MongoURIAdapter;
import com.massivecraft.mcore4.persist.Persist;
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
	
	// -------------------------------------------- //
	// NON STATIC :)
	// -------------------------------------------- //
	
	InternalListener listener;

	@Override
	public void onEnable()
	{
		// Setup PlayerUtil and it's events
		new PlayerUtil(this);
		
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		Persist.instances.clear();
		
		// Register events
		this.listener = new InternalListener(this);
		
		if ( ! preEnable()) return;
		this.postEnable();
	}
	
}
