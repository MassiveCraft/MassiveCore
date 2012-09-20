package com.massivecraft.mcore4;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.mcore4.cmd.Cmd;
import com.massivecraft.mcore4.integration.Integration;
import com.massivecraft.mcore4.integration.IntegrationFeatures;
import com.massivecraft.mcore4.persist.One;
import com.massivecraft.mcore4.persist.Persist;
import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.util.LibLoader;
import com.massivecraft.mcore4.util.Txt;
import com.massivecraft.mcore4.xlib.gson.Gson;
import com.massivecraft.mcore4.xlib.gson.GsonBuilder;

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
		
		// Create Gson
		this.gson = this.getGsonBuilder().create();
		
		// Create tools
		this.cmd = new Cmd(); // TODO: Stop creating this asap :)
		this.persist = new Persist(); // TODO: Stop creating this asap :)
		this.one = new One(this); // TODO: Stop creating this asap :)
		this.lib = new LibLoader(this); // TODO: Stop creating this asap :)
		
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
		// Collection shutdowns for old system.
		this.persist.saveAll();
		Persist.instances.remove(this.persist);
		
		// Collection shutdowns for new system.
		for (Coll<?, ?> coll : Coll.instances)
		{
			if (coll.mplugin() != this) continue;
			coll.examineThread().interrupt();
			coll.syncAll(); // TODO: Save outwards only? We may want to avoid loads at this stage...
			Coll.instances.remove(coll);
		}
		
		log("Disabled");
	}
	
	// -------------------------------------------- //
	// GSON
	// -------------------------------------------- //
	
	public GsonBuilder getGsonBuilder()
	{
		return MCore.getMCoreGsonBuilder();
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public void suicide()
	{
		log("Now I suicide!");
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public void integrate(IntegrationFeatures... features)
	{
		for (IntegrationFeatures f : features)
		{
			new Integration(this, f);
		}
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
