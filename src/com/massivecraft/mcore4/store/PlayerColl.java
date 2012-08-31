package com.massivecraft.mcore4.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.Predictate;

public class PlayerColl<E extends PlayerEntity<E>> extends Coll<E, String>
{
	public PlayerColl(Db<?> db, MPlugin mplugin, String name, Class<E> entityClass)
	{
		super(db, mplugin, "ai", name, entityClass, String.class, true);
	}
	
	public PlayerColl(MPlugin mplugin, String name, Class<E> entityClass)
	{
		super(MCore.getDb(), mplugin, "ai", name, entityClass, String.class, true);
	}
	
	@Override
	public String idFix(Object oid)
	{
		if (oid == null) return null;
		if (oid instanceof String) return (String) oid;
		if (oid instanceof Player) return ((Player)oid).getName();
		if (oid instanceof PlayerEvent) return ((PlayerEvent)oid).getPlayer().getName();
		return null;
	}
	
	public Collection<E> getAllOnline()
	{
		List<E> ret = new ArrayList<E>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			E entity = this.get(player.getName());
			if (entity == null) continue;
			ret.add(entity);
		}
		return ret;
	}
	
	public Collection<E> getAllOffline()
	{
		return this.getAll(new Predictate<E>()
		{
			public boolean apply(E entity)
			{
				return entity.isOffline();
			}
		});
	}

}
