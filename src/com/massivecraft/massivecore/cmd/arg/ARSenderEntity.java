package com.massivecraft.massivecore.cmd.arg;

import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.SenderEntity;

public class ARSenderEntity<T extends SenderEntity<T>> extends ARSenderIdAbstract<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final SenderColl<T> coll;
	
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
		if (senderId == null) return null;
		return this.coll.get(senderId);
	}
	
}
