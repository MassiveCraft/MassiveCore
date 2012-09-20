package com.massivecraft.mcore4.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.Predictate;
import com.massivecraft.mcore4.cmd.arg.ARPlayerEntity;
import com.massivecraft.mcore4.cmd.arg.ARStringMatchFullCI;
import com.massivecraft.mcore4.cmd.arg.ARStringMatchStartCI;
import com.massivecraft.mcore4.cmd.arg.ArgReader;
import com.massivecraft.mcore4.util.MUtil;
import com.massivecraft.mcore4.util.PlayerUtil;

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
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public String idFix(Object oid)
	{
		if (oid == null) return null;
		return MUtil.extract(String.class, "playerName", oid);
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
	
	// -------------------------------------------- //
	// ARGUMENT READERS
	// -------------------------------------------- //
	
	protected Collection<Collection<String>> forgeAltColls()
	{
		Collection<Collection<String>> ret = new ArrayList<Collection<String>>();
		ret.add(this.ids());
		if (this.creative()) ret.add(PlayerUtil.getAllVisitorNames());
		return ret;
	}
	
	public ArgReader<String> argReaderPlayerNameFull()
	{
		return new ARStringMatchFullCI("player", this.forgeAltColls());
	}
	
	public ArgReader<String> argReaderPlayerNameStart()
	{
		return new ARStringMatchStartCI("player", this.forgeAltColls());
	}
	
	public ArgReader<E> argReaderPlayerFull()
	{
		return new ARPlayerEntity<E>(this.argReaderPlayerNameFull(), this);
	}
	
	public ArgReader<E> argReaderPlayerStart()
	{
		return new ARPlayerEntity<E>(this.argReaderPlayerNameStart(), this);
	}

}
