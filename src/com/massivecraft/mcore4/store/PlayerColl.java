package com.massivecraft.mcore4.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.Predictate;
import com.massivecraft.mcore4.cmd.arg.ARStringEntity;
import com.massivecraft.mcore4.cmd.arg.ARStringMatchFullCI;
import com.massivecraft.mcore4.cmd.arg.ARStringMatchStartCI;
import com.massivecraft.mcore4.cmd.arg.ArgReader;
import com.massivecraft.mcore4.util.MUtil;
import com.massivecraft.mcore4.util.PlayerUtil;

public class PlayerColl<E extends PlayerEntity<E>> extends Coll<E, String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Note that the lowercasing should be kept at either true or false.
	protected boolean lowercasing = false;
	public boolean isLowercasing() { return this.lowercasing; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerColl(Db<?> db, MPlugin mplugin, String name, Class<E> entityClass, boolean creative, boolean lowercasing)
	{
		super(db, mplugin, "ai", name, entityClass, String.class, creative);
		this.lowercasing = lowercasing;
	}
	
	public PlayerColl(Db<?> db, MPlugin mplugin, String name, Class<E> entityClass, boolean creative)
	{
		this(db, mplugin, name, entityClass, creative, false);
	}
	
	public PlayerColl(Db<?> db, MPlugin mplugin, String name, Class<E> entityClass)
	{
		this(db, mplugin, name, entityClass, true);
	}
	
	public PlayerColl(MPlugin mplugin, String name, Class<E> entityClass, boolean creative, boolean lowercasing)
	{
		this(MCore.getDb(), mplugin, name, entityClass, creative, lowercasing);
	}
	
	public PlayerColl(MPlugin mplugin, String name, Class<E> entityClass, boolean creative)
	{
		this(MCore.getDb(), mplugin, name, entityClass, creative);
	}
	
	public PlayerColl(MPlugin mplugin, String name, Class<E> entityClass)
	{
		this(MCore.getDb(), mplugin, name, entityClass);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public String idFix(Object oid)
	{
		if (oid == null) return null;
		String ret = MUtil.extract(String.class, "playerName", oid);
		if (ret == null) return ret;
		return this.lowercasing ? ret.toLowerCase() : ret;
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
		return new ARStringEntity<E>(this.argReaderPlayerNameFull(), this);
	}
	
	public ArgReader<E> argReaderPlayerStart()
	{
		return new ARStringEntity<E>(this.argReaderPlayerNameStart(), this);
	}

}
