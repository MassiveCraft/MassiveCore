package com.massivecraft.mcore1.persist.gson;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import com.massivecraft.mcore1.Predictate;
import com.massivecraft.mcore1.lib.gson.Gson;
import com.massivecraft.mcore1.persist.PlayerEntity;

public abstract class GsonPlayerEntityManager<T extends PlayerEntity> extends GsonClassManager<T>
{
	public GsonPlayerEntityManager(Gson gson, File folder, boolean creative, boolean lazy)
	{
		super(gson, folder, creative, lazy);
	}
	
	public GsonPlayerEntityManager(Gson gson, File folder, boolean creative, boolean lazy, Set<String> ids, Set<T> entities, Map<String, T> id2entity, Map<T, String> entity2id)
	{
		super(gson, folder, creative, lazy, ids, entities, id2entity, entity2id);
	}

	@Override
	public String idFix(Object oid)
	{
		if (oid == null) return null;
		if (oid instanceof String) return (String) oid;
		if (oid instanceof Player) return ((Player)oid).getName();
		return null;
	}
	
	@Override
	public boolean idCanFix(Class<?> clazz)
	{
		if (clazz == null) return false;
		if (String.class.equals(clazz)) return true;
		if (Player.class.equals(clazz)) return true;
		return false;
	}
	
	public Collection<T> getAllOnline()
	{
		return this.getAll(new Predictate<T>()
		{
			public boolean apply(T entity)
			{
				return entity.isOnline();
			}
		});
	}
	
	public Collection<T> getAllOffline()
	{
		return this.getAll(new Predictate<T>()
		{
			public boolean apply(T entity)
			{
				return entity.isOffline();
			}
		});
	}
}
