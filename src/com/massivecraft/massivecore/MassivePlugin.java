package com.massivecraft.massivecore;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.integration.IntegrationGlue;
import com.massivecraft.massivecore.integration.Integration;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

public abstract class MassivePlugin extends JavaPlugin implements Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Gson
	protected Gson gson = null;
	public Gson getGson() { return this.gson; }
	public void setGson(Gson gson) { this.gson = gson; }
	
	protected boolean versionSynchronized = false;
	public boolean isVersionSynchronized() { return this.versionSynchronized; }
	public void setVersionSynchronized(boolean versionSynchronized) { this.versionSynchronized = versionSynchronized; }
	
	// -------------------------------------------- //
	// ENABLE
	// -------------------------------------------- //
	
	private long enableTime;
	public long getEnableTime() { return this.enableTime; }
	
	public boolean preEnable()
	{
		enableTime = System.currentTimeMillis();
		
		this.logPrefixColored = Txt.parse("<teal>[<aqua>%s %s<teal>] <i>", this.getDescription().getName(), this.getDescription().getVersion());
		this.logPrefixPlain = ChatColor.stripColor(this.logPrefixColored);
		
		log("=== ENABLE START ===");
		
		// Create Gson
		Gson gson = this.getGsonBuilder().create();
		this.setGson(gson);
		
		// Listener
		Bukkit.getPluginManager().registerEvents(this, this);

		// Metrics
		try
		{
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		}
		catch (IOException e)
		{
			String message = Txt.parse("<b>Metrics Initialization Failed :'(");
			log(message);
		}
		
		return true;
	}
	
	public void postEnable()
	{
		long ms = System.currentTimeMillis() - enableTime;
		
		this.checkVersionSynchronization();
		
		log(Txt.parse("=== ENABLE <g>COMPLETE <i>(Took <h>" + ms + "ms<i>) ==="));
	}
	
	public void checkVersionSynchronization()
	{
		// If this plugin is version synchronized ...
		if ( ! this.isVersionSynchronized()) return;
		
		// ... and checking is enabled ...
		if ( ! MassiveCoreMConf.get().checkVersionSynchronization) return;
		
		// ... get the version numbers ...
		String thisVersion = this.getDescription().getVersion();
		String massiveVersion = MassiveCore.get().getDescription().getVersion();
		
		// ... and if the version numbers are different ...
		if (thisVersion.equals(massiveVersion)) return;
		
		// ... log a warning message ...
		String thisName = this.getDescription().getName();
		String massiveName = MassiveCore.get().getDescription().getName();
		
		log(Txt.parse("<b>WARNING: You are using <pink>" + thisName + " <aqua>" + thisVersion + " <b>and <pink>" + massiveName + " <aqua>" + massiveVersion + "<b>!"));
		log(Txt.parse("<b>WARNING: They must be the exact same version to work properly!"));
		log(Txt.parse("<b>WARNING: Remember to always update all plugins at the same time!"));
		log(Txt.parse("<b>WARNING: You should stop your server and properly update."));
		
		// ... and pause for 10 seconds.
		try
		{
			Thread.sleep(10000L);
		}
		catch (InterruptedException ignored)
		{
			
		}
	}
	
	@Override
	public void onEnable()
	{
		if ( ! this.preEnable()) return;
		
		this.postEnable();
	}
	
	// -------------------------------------------- //
	// DISABLE
	// -------------------------------------------- //
	
	@Override
	public void onDisable()
	{
		// Commands
		MassiveCommand.unregister(this);
		
		// Collections
		for (Coll<?> coll : Coll.getInstances())
		{
			if (coll.getPlugin() != this) continue;
			coll.deinit();
		}
		
		log("Disabled");
	}
	
	// -------------------------------------------- //
	// GSON
	// -------------------------------------------- //
	
	public GsonBuilder getGsonBuilder()
	{
		return MassiveCore.getMassiveCoreGsonBuilder();
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public void suicide()
	{
		log(Txt.parse("<b>Now I suicide!"));
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public void integrate(Integration... features)
	{
		for (Integration f : features)
		{
			new IntegrationGlue(this, f);
		}
	}
	
	// -------------------------------------------- //
	// LOGGING
	// -------------------------------------------- //
	private String logPrefixColored = null;
	private String logPrefixPlain = null;
	public void log(Object... msg)
	{
		log(Level.INFO, msg);
	}
	public void log(Level level, Object... msg)
	{
		String imploded = Txt.implode(msg, " ");
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		if (level == Level.INFO && sender != null)
		{
			Bukkit.getConsoleSender().sendMessage(this.logPrefixColored + imploded);
		}
		else
		{
			Logger.getLogger("Minecraft").log(level, this.logPrefixPlain + imploded);
		}
	}
}
