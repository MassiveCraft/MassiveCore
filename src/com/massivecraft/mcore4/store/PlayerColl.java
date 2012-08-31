package com.massivecraft.mcore4.store;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.Predictate;

public class PlayerColl<E extends PlayerEntity<E>> extends Coll<E, String>
{
	public PlayerColl(MPlugin mplugin, Db<?> db, String name, Class<E> entityClass)
	{
		super(mplugin, db, "ai", name, entityClass, String.class, true);
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
		// TODO: Reverse the order since we have fewer players online than offline?
		return this.getAll(new Predictate<E>()
		{
			public boolean apply(E entity)
			{
				return entity.isOnline();
			}
		});
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
