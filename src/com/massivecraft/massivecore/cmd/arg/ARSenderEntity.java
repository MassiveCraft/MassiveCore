package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.IdUtil;

public class ARSenderEntity<T extends SenderEntity<T>> extends ARSenderIdAbstract<T> implements ARAllAble<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final SenderColl<T> coll;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private ARSenderEntity(SenderColl<T> coll, boolean online)
	{
		super(coll, online);
		this.coll = coll;
	}
	
	private ARSenderEntity(SenderColl<T> coll)
	{
		super(coll);
		this.coll = coll;
	}
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> get(SenderColl<T> coll, boolean online) { return new ARSenderEntity<T>(coll, online); }
	public static <T extends SenderEntity<T>> ARSenderEntity<T> get(SenderColl<T> coll) { return new ARSenderEntity<T>(coll); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T getResultForSenderId(String senderId)
	{
		// Null check is done in SenderColl & IdUtil :)
		return this.coll.get(senderId);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (String name : IdUtil.getOnlineNames())
		{
			if ( ! Mixin.canSee(sender, name)) continue;
			ret.add(name);
		}
		
		return ret;
	}

	@Override
	public Collection<T> getAll(CommandSender sender)
	{
		return coll.getAll();
	}

}
