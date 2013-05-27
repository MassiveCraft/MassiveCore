package com.massivecraft.mcore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.MUtil;

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
	
	public SenderColl(String name, Class<E> entityClass, Db db, Plugin plugin, boolean creative, boolean lowercasing, String idStrategyName, Comparator<? super String> idComparator, Comparator<? super E> entityComparator)
	{
		super(name, entityClass, db, plugin, creative, lowercasing, idStrategyName, idComparator, entityComparator);
	}
	
	public SenderColl(String name, Class<E> entityClass, Db db, Plugin plugin, boolean creative, boolean lowercasing)
	{	
		super(name, entityClass, db, plugin, creative, lowercasing);
	}
	
	public SenderColl(String name, Class<E> entityClass, Db db, Plugin plugin)
	{	
		super(name, entityClass, db, plugin, true, true);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public List<String> getFixedIds()
	{
		List<String> ret = new ArrayList<String>();
		for (String senderId : this.getIds())
		{
			ret.add(Mixin.tryFix(senderId));
		}
		return ret;
	}
	
	@Override
	public Collection<Collection<String>> getSenderIdCollections()
	{
		List<Collection<String>> ret = new ArrayList<Collection<String>>();
		ret.add(this.getFixedIds());
		return ret;
	}
	
	
	@Override
	public String fixId(Object oid)
	{
		if (oid == null) return null;
		
		String ret = null;
		if (oid instanceof String) 
		{
			ret = (String)oid;
		}
		else if (oid.getClass() == this.entityClass)
		{
			ret = this.entity2id.get(oid);
		}
		else
		{
			ret = MUtil.extract(String.class, "senderId", oid);
		}
		
		if (ret == null) return null;
		
		return this.isLowercasing() ? ret.toLowerCase() : ret;
	}
	
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
	// SENDER REFFERENCE MANAGEMENT
	// -------------------------------------------- //
	
	protected void setSenderRefference(String senderId, CommandSender sender)
	{
		 E senderEntity = this.get(senderId, false);
		 if (senderEntity == null) return;
		 senderEntity.sender = sender;
		 senderEntity.senderInitiated = true;
	}
	
	public static void setSenderRefferences(String senderId, CommandSender sender)
	{
		for (Coll<?> coll : Coll.getInstances())
		{
			if (!(coll instanceof SenderColl)) continue;
			SenderColl<?> senderColl = (SenderColl<?>)coll;
			senderColl.setSenderRefference(senderId, sender);
		}
	}
	
	// -------------------------------------------- //
	// ARGUMENT READERS
	// -------------------------------------------- //
	
	/*public ArgReader<E> getARFullAny()
	{
		return ARSenderEntity.getFullAny(this);
	}
	
	public ArgReader<E> getARStartAny()
	{
		return ARSenderEntity.getStartAny(this);
	}*/

}
