package com.massivecraft.mcore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.MPlugin;
import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.MUtil;

public class SenderColl<E extends SenderEntity<E>> extends Coll<E, String> implements SenderIdSource
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static boolean DEFAULT_LOWERCASING = true;
	public final static boolean DEFAULT_CREATIVE = true;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// "Lowercasing" means that the ids are always converted to lower case when fixed.
	// This is highly recommended for sender colls.
	// The senderIds are case insensitive by nature and some times you simply can't know the correct casing.
	
	protected boolean lowercasing;
	public boolean isLowercasing() { return this.lowercasing; }
	
	protected final SenderIdSource mixinedIdSource = new SenderIdSourceCombined(this, SenderIdSourceMixinAllSenderIds.get());
	public SenderIdSource getMixinedIdSource() { return this.mixinedIdSource; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public SenderColl(Db<?> db, MPlugin mplugin, String name, Class<E> entityClass, boolean creative, boolean lowercasing, Comparator<? super String> idComparator, Comparator<? super E> entityComparator)
	{
		super(db, mplugin, "ai", name, entityClass, String.class, creative, idComparator, entityComparator);
		this.lowercasing = lowercasing;
	}
	
	public SenderColl(Db<?> db, MPlugin mplugin, String name, Class<E> entityClass, boolean creative, boolean lowercasing)
	{
		this(db, mplugin, name, entityClass, creative, lowercasing, null, null);
	}
	
	public SenderColl(Db<?> db, MPlugin mplugin, String name, Class<E> entityClass, boolean creative)
	{
		this(db, mplugin, name, entityClass, creative, DEFAULT_LOWERCASING);
	}
	
	public SenderColl(Db<?> db, MPlugin mplugin, String name, Class<E> entityClass)
	{
		this(db, mplugin, name, entityClass, DEFAULT_CREATIVE);
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
		String ret = MUtil.extract(String.class, "senderId", oid);
		if (ret == null) return ret;
		return this.lowercasing ? ret.toLowerCase() : ret;
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
		for (Coll<?, ?> coll : Coll.instances)
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
