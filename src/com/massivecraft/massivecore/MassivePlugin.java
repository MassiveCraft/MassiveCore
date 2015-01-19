package com.massivecraft.massivecore;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.integration.IntegrationGlue;
import com.massivecraft.massivecore.integration.Integration;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

public abstract class MassivePlugin extends JavaPlugin implements Listener
{
	// Gson
	public Gson gson;
	
	// -------------------------------------------- //
	// ENABLE
	// -------------------------------------------- //
	
	private long timeEnableStart;
	public boolean preEnable()
	{
		timeEnableStart = System.currentTimeMillis();
		
		this.logPrefixColored = Txt.parse("<teal>[<aqua>%s %s<teal>] <i>", this.getDescription().getName(), this.getDescription().getVersion());
		this.logPrefixPlain = ChatColor.stripColor(this.logPrefixColored);
		
		log("=== ENABLE START ===");
		
		// Create Gson
		this.gson = this.getGsonBuilder().create();
		
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
		log(Txt.parse("=== ENABLE <g>COMPLETE <i>(Took <h>"+(System.currentTimeMillis()-timeEnableStart)+"ms<i>) ==="));
	}
	
	@Override
	public void onEnable()
	{
		if (!this.preEnable()) return;
		
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
