package com.massivecraft.mcore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.util.IdUtil;

public class SenderColl<E extends SenderEntity<E>> extends Coll<E> implements SenderIdSource
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final SenderIdSource mixinedIdSource = new SenderIdSourceCombined(this, SenderIdSourceMixinAllSenderIds.get());
	public SenderIdSource getMixinedIdSource() { return this.mixinedIdSource; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public SenderColl(String name, Class<E> entityClass, Db db, Plugin plugin, boolean lazy, boolean creative, boolean lowercasing, Comparator<? super String> idComparator, Comparator<? super E> entityComparator)
	{
		super(name, entityClass, db, plugin, lazy, creative, lowercasing, idComparator, entityComparator);
	}
	
	public SenderColl(String name, Class<E> entityClass, Db db, Plugin plugin, boolean lazy, boolean creative, boolean lowercasing)
	{	
		super(name, entityClass, db, plugin, lazy, creative, lowercasing);
	}
	
	public SenderColl(String name, Class<E> entityClass, Db db, Plugin plugin)
	{	
		super(name, entityClass, db, plugin, true, true, true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: Coll
	// -------------------------------------------- //
	
	@Override
	public String fixId(Object oid)
	{
		if (oid == null) return null;
		
		if (oid instanceof String) 
		{
			String ret = (String)oid;
			return this.isLowercasing() ? ret.toLowerCase() : ret;
		}
		
		if (oid.getClass() == this.entityClass)
		{
			return fixId(this.entity2id.get(oid));
		}
		
		return fixId(IdUtil.getId(oid));
	}
	
	// -------------------------------------------- //
	// OVERRIDE: SenderIdSource
	// -------------------------------------------- //
	
	@Override
	public Collection<Collection<String>> getSenderIdCollections()
	{
		List<Collection<String>> ret = new ArrayList<Collection<String>>();
		ret.add(this.getIds());
		
		List<String> names = new ArrayList<String>();
		for (String id : this.getIds())
		{
			String name = IdUtil.getName(id);
			if (name == null) continue;
			names.add(name);
		}
		ret.add(names);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public Collection<E> getAllOnline()
	{
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
	
	// -------------------------------------------- //
	// SENDER REFERENCE MANAGEMENT
	// -------------------------------------------- //
	
	protected void setSenderReference(String senderId, CommandSender sender)
	{
		 E senderEntity = this.get(senderId, false);
		 if (senderEntity == null) return;
		 senderEntity.sender = sender;
		 senderEntity.senderInitiated = true;
	}
	
	public static void setSenderReferences(String senderId, CommandSender sender)
	{
		for (Coll<?> coll : Coll.getInstances())
		{
			if (!(coll instanceof SenderColl)) continue;
			SenderColl<?> senderColl = (SenderColl<?>)coll;
			senderColl.setSenderReference(senderId, sender);
		}
	}
	
	// -------------------------------------------- //
	// ARGUMENT READERS
	// -------------------------------------------- //
	// TODO: Why were these removed?
	
	/*public ArgReader<E> getARFullAny()
	{
		return ARSenderEntity.getFullAny(this);
	}
	
	public ArgReader<E> getARStartAny()
	{
		return ARSenderEntity.getStartAny(this);
	}*/

}
