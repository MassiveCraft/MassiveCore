package com.massivecraft.mcore5.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.store.MixinSenderIdSource;
import com.massivecraft.mcore5.store.SenderIdSource;
import com.massivecraft.mcore5.util.SenderUtil;

public class ARSender extends ARSenderIdAbstractPredsource<CommandSender>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ARSender full = getFull(MixinSenderIdSource.get());
	public static ARSender getFull() { return full; }
	
	private static final ARSender start = getStart(MixinSenderIdSource.get());
	public static ARSender getStart() { return start; }
	
	public static ARSender getFull(SenderIdSource source)
	{
		return new ARSender(source, "sender", ArgPredictateStringEqualsLC.get());
	}
	
	public static ARSender getStart(SenderIdSource source)
	{
		return new ARSender(source, "sender", ArgPredictateStringStartsLC.get());
	}
	
	private ARSender(SenderIdSource source, String typename, ArgPredictate<String> argPredictate)
	{
		super(source, typename, argPredictate);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public CommandSender getResultForSenderId(String senderId)
	{
		return SenderUtil.getSender(senderId);
	}
	
}
