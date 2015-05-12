package com.massivecraft.massivecore.cmd.arg;

import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;

public class ARSenderId extends ARSenderIdAbstract<String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private ARSenderId(SenderIdSource source, boolean onlineOnly, boolean playerOnly)
	{
		super(source, onlineOnly, playerOnly);
	}
	
	private ARSenderId(SenderIdSource source, boolean onlineOnly)
	{
		super(source, onlineOnly);
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
	
	public static ARSenderId get(SenderIdSource source, boolean onlineOnly, boolean playerOnly) { return new ARSenderId(source, onlineOnly, playerOnly); }
	public static ARSenderId get(SenderIdSource source, boolean onlineOnly) { return new ARSenderId(source, onlineOnly); }
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
