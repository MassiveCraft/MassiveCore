package com.massivecraft.massivecore.cmd.arg;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;

public class ARSenderId extends ARSenderIdAbstract<String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private ARSenderId(SenderIdSource source, SenderPresence presence, SenderType type)
	{
		super(source, presence, type);
	}
	
	private ARSenderId(SenderIdSource source, SenderPresence presence)
	{
		super(source, presence);
	}
	
	private ARSenderId(SenderIdSource source)
	{
		super(source);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static final ARSenderId i = new ARSenderId(SenderIdSourceMixinAllSenderIds.get());
	public static ARSenderId get() { return i; }
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public static ARSenderId get(SenderIdSource source, SenderPresence presence, SenderType type) { return new ARSenderId(source, presence, type); }
	public static ARSenderId get(SenderIdSource source, SenderPresence presence) { return new ARSenderId(source, presence); }
	public static ARSenderId get(SenderIdSource source) { return new ARSenderId(source); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getResultForSenderId(String senderId)
	{
		return senderId;
	}

}
