package com.massivecraft.core.plugin.listener;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import com.massivecraft.core.persist.Persist;
import com.massivecraft.core.plugin.MCore;

public class PluginServerListener extends ServerListener
{
	MCore p;
	
	public PluginServerListener(MCore p)
	{
		this.p = p;
	}
	
	@Override
	public void onPluginEnable(PluginEnableEvent event)
	{
		// TODO: Is this run after or before???
		Persist.createRealm(event.getPlugin());
	}
}
