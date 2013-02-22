package com.massivecraft.mcore5.cmd.arg;

import com.massivecraft.mcore5.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.mcore5.store.SenderIdSource;

public class ARSenderId extends ARSenderIdAbstractPredsource<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ARSenderId full = getFull(SenderIdSourceMixinAllSenderIds.get());
	public static ARSenderId getFull() { return full; }
	
	private static final ARSenderId start = getStart(SenderIdSourceMixinAllSenderIds.get());
	public static ARSenderId getStart() { return start; }
	
	public static ARSenderId getFull(SenderIdSource source)
	{
		return new ARSenderId(source, "player", ArgPredictateStringEqualsLC.get());
	}
	
	public static ARSenderId getStart(SenderIdSource source)
	{
		return new ARSenderId(source, "player", ArgPredictateStringStartsLC.get());
	}
	
	private ARSenderId(SenderIdSource source, String typename, ArgPredictate<String> argPredictate)
	{
		super(source, typename, argPredictate);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getResultForSenderId(String senderId)
	{
		return senderId;
	}
	
}
