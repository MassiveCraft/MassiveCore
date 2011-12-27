package com.massivecraft.mcore1;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.mcore1.cmd.Cmd;
import com.massivecraft.mcore1.lib.gson.Gson;
import com.massivecraft.mcore1.lib.gson.GsonBuilder;
import com.massivecraft.mcore1.persist.One;
import com.massivecraft.mcore1.persist.Persist;
import com.massivecraft.mcore1.util.LibLoader;
import com.massivecraft.mcore1.util.Txt;


public abstract class MPlugin extends JavaPlugin
{
	// Tools
	public Cmd cmd;
	public Persist persist;
	public One one;
	public LibLoader lib;
	
	// Gson
	public Gson gson;
	
	// -------------------------------------------- //
	// ENABLE
	// -------------------------------------------- //
	
	private long timeEnableStart;
	public boolean preEnable()
	{
		timeEnableStart = System.currentTimeMillis();
		this.logPrefix = "["+this.getDescription().getFullName()+"] ";
		log("=== ENABLE START ===");
		
		// Ensure the base folder exists
		this.getDataFolder().mkdirs();
		
		// Create Gson
		this.gson = this.getGsonBuilder().create();
		
		// Create Tools
		MCore.createCmd(this);
		MCore.createPersist(this);
		MCore.createOne(this);
		MCore.createLibLoader(this);
		
		// Assign tool pointers
		this.cmd = MCore.getCmd(this);
		this.persist = MCore.getPersist(this);
		this.one = MCore.getOne(this);
		this.lib = MCore.getLibLoader(this);
		
		return true;
	}
	
	public void postEnable()
	{
		log("=== ENABLE DONE (Took "+(System.currentTimeMillis()-timeEnableStart)+"ms) ===");
	}
	
	// -------------------------------------------- //
	// DISABLE
	// -------------------------------------------- //
	
	public void onDisable()
	{
		MCore.getPersist(this).saveAll();
		MCore.removePersist(this);
		MCore.removeOne(this);
		MCore.removeCmd(this);
		MCore.removeLibLoader(this);
		
		this.cmd = null;
		this.persist = null;
		this.one = null;
		this.lib = null;
		
		log("Disabled");
	}
	
	// -------------------------------------------- //
	// GSON
	// -------------------------------------------- //
	
	public GsonBuilder getGsonBuilder()
	{
		return MCore.getGsonBuilder();
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public void suicide()
	{
		log("Now I suicide!");
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public void registerEvent(Event.Type type, Listener listener, Event.Priority priority)
	{
		Bukkit.getPluginManager().registerEvent(type, listener, priority, this);
	}
	
	public void registerEvent(Event.Type type, Listener listener)
	{
		registerEvent(type, listener, Event.Priority.Normal);
	}
	
	// -------------------------------------------- //
	// LOGGING
	// -------------------------------------------- //
	private String logPrefix = null;
	public void log(Object... msg)
	{
		log(Level.INFO, msg);
	}
	public void log(Level level, Object... msg)
	{
		Logger.getLogger("Minecraft").log(level, this.logPrefix + Txt.implode(msg, " "));
	}
}
