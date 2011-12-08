package com.massivecraft.core.plugin.listener;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import com.massivecraft.core.persist.Persist;
import com.massivecraft.core.plugin.MCore;

public class PluginServerListenerMonitor extends ServerListener
{
	MCore p;
	
	public PluginServerListenerMonitor(MCore p)
	{
		this.p = p;
	}
	
	@Override
	public void onPluginDisable(PluginDisableEvent event)
	{
		Persist.removeRealm(event.getPlugin());
	}
	
	@Override
	public void onPluginEnable(PluginEnableEvent event)
	{
		// TODO: Is this run after or before???
		Persist.createRealm(event.getPlugin());
	}
}
