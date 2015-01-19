package com.massivecraft.massivecore.cmd.arg;

import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;

public class ARSenderId extends ARSenderIdAbstract<String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private ARSenderId(SenderIdSource source, boolean online)
	{
		super(source, online);
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
	
	public static ARSenderId get(SenderIdSource source, boolean online) { return new ARSenderId(source, online); }
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
