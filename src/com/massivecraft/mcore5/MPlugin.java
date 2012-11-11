package com.massivecraft.mcore5;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.mcore5.integration.Integration;
import com.massivecraft.mcore5.integration.IntegrationFeatures;
import com.massivecraft.mcore5.store.Coll;
import com.massivecraft.mcore5.util.Txt;
import com.massivecraft.mcore5.xlib.gson.Gson;
import com.massivecraft.mcore5.xlib.gson.GsonBuilder;

public abstract class MPlugin extends JavaPlugin implements Listener
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
		
		return true;
	}
	
	public void postEnable()
	{
		log(Txt.parse("=== ENABLE <g>COMPLETE <i>(Took <h>"+(System.currentTimeMillis()-timeEnableStart)+"ms<i>) ==="));
	}
	
	// -------------------------------------------- //
	// DISABLE
	// -------------------------------------------- //
	
	public void onDisable()
	{
		// Collection shutdowns.
		for (Coll<?, ?> coll : Coll.instances)
		{
			if (coll.getMplugin() != this) continue;
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
		log(Txt.parse("<b>Now I suicide!"));
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
