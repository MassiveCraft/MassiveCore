package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.SenderEntity;

public class ARSenderEntity<T extends SenderEntity<T>> extends ARSenderIdAbstract<T> implements ARAllAble<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final SenderColl<T> coll;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private ARSenderEntity(SenderColl<T> coll, SenderPresence presence, SenderType type)
	{
		super(coll, presence, type);
		this.coll = coll;
	}
	
	private ARSenderEntity(SenderColl<T> coll, SenderPresence presence)
	{
		super(coll, presence);
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
	
	public static <T extends SenderEntity<T>> ARSenderEntity<T> get(SenderColl<T> coll, SenderPresence presence, SenderType type) { return new ARSenderEntity<T>(coll, presence, type); }
	public static <T extends SenderEntity<T>> ARSenderEntity<T> get(SenderColl<T> coll, SenderPresence presence) { return new ARSenderEntity<T>(coll, presence); }
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
	public Collection<T> getAll(CommandSender sender)
	{
		return coll.getAll();
	}

}
