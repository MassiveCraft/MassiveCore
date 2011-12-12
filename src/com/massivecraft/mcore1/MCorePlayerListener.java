package com.massivecraft.mcore1;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import com.massivecraft.mcore1.persist.IClassManager;
import com.massivecraft.mcore1.persist.Persist;

public class MCorePlayerListener extends PlayerListener
{
	MCore p;
	
	public MCorePlayerListener(MCore p)
	{
		this.p = p;
	}
	
	@Override
	public void onPlayerPreLogin(PlayerPreLoginEvent event)
	{
		String id = event.getName();
		
		for (Persist realm : MCore.getPersistInstances().values())
		{
			for (IClassManager<?> manager : realm.getClassManagers().values())
			{
				if (manager.idCanFix(Player.class) == false) continue;
				if (manager.containsId(id)) continue;
				manager.create(id);
			}
		}
	}
	
	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if (event.isCancelled()) return;
		if (MCore.handleCommand(event.getPlayer(), event.getMessage().substring(1), false))
		{
			event.setCancelled(true);
		}
	}
}
