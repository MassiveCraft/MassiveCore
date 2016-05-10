package com.massivecraft.massivecore;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

public abstract class MassivePlugin extends JavaPlugin implements Listener, Named
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
		
		// Version Synchronization
		this.checkVersionSynchronization();
		
		// Create Gson
		Gson gson = this.getGsonBuilder().create();
		this.setGson(gson);
		
		// Listener
		Bukkit.getPluginManager().registerEvents(this, this);

		return true;
	}
	
	public void postEnable()
	{
		// Metrics
		if (MassiveCoreMConf.get().mcstatsEnabled)
		{
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
		}
		
		long ms = System.currentTimeMillis() - enableTime;
		log(Txt.parse("=== ENABLE <g>COMPLETE <i>(Took <h>" + ms + "ms<i>) ==="));
	}
	
	public void checkVersionSynchronization()
	{
		// If this plugin is version synchronized ...
		if ( ! this.isVersionSynchronized()) return;
		
		// ... and checking is enabled ...
		if ( ! MassiveCoreMConf.get().versionSynchronizationEnabled) return;
		
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
		
		this.onEnableInner();
		
		this.postEnable();
	}
	
	public void onEnableInner()
	{
		
	}
	
	// -------------------------------------------- //
	// DISABLE
	// -------------------------------------------- //
	
	@Override
	public void onDisable()
	{
		// Commands
		this.deactivate(MassiveCommand.getAllInstances());
		
		// Engines
		this.deactivate(Engine.getAllInstances());
		
		// Collections
		this.deactivate(Coll.getInstances());
		
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
		this.log(Txt.parse("<b>Now I suicide!"));
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public void activate(Object... objects)
	{
		for (Object object : objects)
		{
			Active active = asActive(object);
			active.setActive(this);
		}
	}
	
	private static Active asActive(Object object)
	{
		if (object instanceof Active)
		{
			return (Active)object;
		}
		
		if (object instanceof Class<?>)
		{
			Class<?> clazz = (Class<?>)object;
			if ( ! Active.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Not Active Class: " + (clazz == null ? "NULL" : clazz));
			
			Object instance = ReflectionUtil.getSingletonInstance(clazz);
			if ( ! (instance instanceof Active)) throw new IllegalArgumentException("Not Active Instance: " + (instance == null ? "NULL" : instance) + " for object: " + (object == null ? "NULL" : object));
			
			Active active = (Active)instance;
			return active;
		}
		
		throw new IllegalArgumentException("Neither Active nor Class: " + object);
	}
	
	private void deactivate(Collection<? extends Active> actives)
	{
		// Fail Fast
		if (actives == null) throw new NullPointerException("actives");
		
		// Clone to Avoid CME
		List<Active> all = new MassiveList<>(actives);
		
		// Reverse to Disable Reversely
		Collections.reverse(all);
		
		// Loop
		for (Active active : all)
		{
			// Check
			if ( ! this.equals(active.getActivePlugin())) continue;
			
			// Deactivate
			active.setActive(false);
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
