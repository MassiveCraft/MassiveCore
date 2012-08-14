package com.massivecraft.mcore4;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import com.massivecraft.mcore4.persist.IClassManager;
import com.massivecraft.mcore4.persist.Persist;
import com.massivecraft.mcore4.util.PlayerUtil;

public class InternalListener implements Listener
{
	MCore p;
	
	public InternalListener(MCore p)
	{
		this.p = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, this.p);
	}
	
	// TODO: Does this even trigger? If not we have an issue.
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
}
