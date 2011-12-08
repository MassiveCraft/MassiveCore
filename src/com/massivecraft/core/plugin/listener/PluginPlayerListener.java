package com.massivecraft.core.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.massivecraft.core.persist.IClassManager;
import com.massivecraft.core.persist.Persist;
import com.massivecraft.core.persist.PersistRealm;
import com.massivecraft.core.plugin.MCore;

public class PluginPlayerListener extends PlayerListener
{
	MCore p;
	
	public PluginPlayerListener(MCore p)
	{
		this.p = p;
	}
	
	@Override
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		
		for (PersistRealm realm : Persist.getRealms().values())
		{
			for (IClassManager<?> manager : realm.getClassManagers().values())
			{
				if (manager.idFix(player) == null) continue;
				if (manager.containsId(player)) continue;
				manager.create(player);
			}
		}
	}
}
