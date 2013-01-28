package com.massivecraft.mcore5.integration;

import java.util.Collection;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore5.MPlugin;
import com.massivecraft.mcore5.util.Txt;

public class Integration implements Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected MPlugin ourPlugin;
	protected IntegrationFeatures features;
	
	@Getter protected boolean active = false;
	
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
		String namelist = Txt.implodeCommaAndDot(this.features.getTargetPluginNames(), "<h>%s", "<i>, ", " <i>and ", "<i>.");
		
		if (isPluginsEnabled(this.features.getTargetPluginNames()))
		{
			if (!this.active)
			{
				try
				{
					this.features.activate();
					this.active = true;
					this.ourPlugin.log(Txt.parse("<g>Activated <i>integration with "+namelist));
				}
				catch (Exception e)
				{
					this.ourPlugin.log(Txt.parse("<b>Failed to activate <i>integration with "+namelist));
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
					this.ourPlugin.log(Txt.parse("<g>Deactivated <i>integration with "+namelist));
				}
				catch (Exception e)
				{
					this.ourPlugin.log(Txt.parse("<b>Failed to deactivate <i>integration with "+namelist));
					e.printStackTrace();
				}
			}
		}
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static boolean isPluginEnabled(String pluginName)
	{
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null) return false;
		return plugin.isEnabled();
	}
	
	public static boolean isPluginsEnabled(Collection<String> pluginNames)
	{
		for (String pluginName : pluginNames)
		{
			if (!isPluginEnabled(pluginName)) return false;
		}
		return true;
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
