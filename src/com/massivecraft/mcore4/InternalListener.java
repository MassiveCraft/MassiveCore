package com.massivecraft.mcore4;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.massivecraft.mcore4.persist.IClassManager;
import com.massivecraft.mcore4.persist.Persist;
import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.store.ModificationState;
import com.massivecraft.mcore4.store.PlayerColl;

public class InternalListener implements Listener
{
	MCore p;
	
	public InternalListener(MCore p)
	{
		this.p = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, this.p);
	}
	
	// TODO: Does this even trigger? If not we have an issue.
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerPreLogin(PlayerLoginEvent event)
	{
		String id = event.getPlayer().getName();
		
		for (Persist instance : Persist.instances)
		{
			for (IClassManager<?> manager : instance.getClassManagers().values())
			{
				if (manager.idCanFix(Player.class) == false) continue;
				if (manager.containsId(id)) continue;
				manager.create(id);
			}
		}
	}
	
	/**
	 * We sync the player in all player collections at PlayerLoginEvent LOW. LOWEST is left for anti flood and bans.
	 * The syncs are not to heavy to do and other events can rest assure the data is up to date. 
	 */
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void syncPlayerEntities(PlayerLoginEvent event)
	{
		String playerName = event.getPlayer().getName();
		for (Coll<?, ?> coll : Coll.instances)
		{
			if (!(coll instanceof PlayerColl)) continue;
			PlayerColl<?> pcoll = (PlayerColl<?>)coll;
			ModificationState mstate = pcoll.syncId(playerName);
			p.log("syncPlayerEntities", coll.name(), playerName, mstate);
		}
	}
	
}
