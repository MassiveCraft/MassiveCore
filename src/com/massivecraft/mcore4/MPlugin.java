package com.massivecraft.mcore4;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.mcore4.cmd.Cmd;
import com.massivecraft.mcore4.lib.gson.Gson;
import com.massivecraft.mcore4.lib.gson.GsonBuilder;
import com.massivecraft.mcore4.persist.One;
import com.massivecraft.mcore4.persist.Persist;
import com.massivecraft.mcore4.util.LibLoader;
import com.massivecraft.mcore4.util.Txt;

public abstract class MPlugin extends JavaPlugin implements Listener
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
		
		
		MCore.createPersist(this);
		this.cmd = new Cmd();
		this.persist = MCore.getPersist(this);
		this.one = new One(this);
		this.lib = new LibLoader(this);
		
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
		this.persist.saveAll();
		MCore.removePersist(this);
		
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
