package com.massivecraft.mcore2dev;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.massivecraft.mcore2dev.persist.IClassManager;
import com.massivecraft.mcore2dev.persist.Persist;
import com.massivecraft.mcore2dev.util.PlayerUtil;

public class InternalListener implements Listener
{
	MCore p;
	
	public InternalListener(MCore p)
	{
		this.p = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, this.p);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPreLogin(PlayerPreLoginEvent event)
	{
		String id = event.getName();
		
		PlayerUtil.getAllVisitorNames().add(id);
		
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if (event.isCancelled()) return;
		if (MCore.handleCommand(event.getPlayer(), event.getMessage().substring(1), false))
		{
			event.setCancelled(true);
		}
	}
	
	private final static String refCommand = "mcoresilenteater";
	@EventHandler(priority = EventPriority.LOWEST)
	public void onServerCommand(ServerCommandEvent event)
	{
		if (event.getCommand().length() == 0) return;
		if (MCore.handleCommand(event.getSender(), event.getCommand(), false))
		{
			event.setCommand(refCommand);
		}
	}
}
