package com.massivecraft.mcore4.integration;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore4.MPlugin;

public class Integration implements Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected MPlugin ourPlugin;
	protected IntegrationFeatures features;
	
	protected boolean active = false;
	public boolean active() { return this.active; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Integration(MPlugin ourPlugin, IntegrationFeatures features)
	{
		this.ourPlugin = ourPlugin;
		this.features = features;
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this.ourPlugin);
		this.tick();
	}
	
	// -------------------------------------------- //
	// LOGIC
	// -------------------------------------------- //
	
	public void tick()
	{
		if (pluginEnabled(this.features.getTargetPluginName()))
		{
			if (!this.active)
			{
				try
				{
					this.features.activate();
					this.active = true;
					this.ourPlugin.log("Activated integration with "+this.features.getTargetPluginName()+".");
				}
				catch (Exception e)
				{
					this.ourPlugin.log("Failed to activate integration with "+this.features.getTargetPluginName()+".");
					e.printStackTrace();
				}
			}
		}
		else
		{
			if (this.active)
			{
				try
				{
					this.active = false;
					this.features.deactivate();
					this.ourPlugin.log("Deactivated integration with "+this.features.getTargetPluginName()+".");
				}
				catch (Exception e)
				{
					this.ourPlugin.log("Failed to deactivate integration with "+this.features.getTargetPluginName()+".");
					e.printStackTrace();
				}
			}
		}
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static boolean pluginEnabled(String pluginName)
	{
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null) return false;
		return plugin.isEnabled();
	}
	
	// -------------------------------------------- //
	// EVENT LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event)
	{
		this.tick();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event)
	{
		this.tick();
	}
}
