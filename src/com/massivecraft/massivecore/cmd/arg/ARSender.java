package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;

public class ARSender extends ARSenderIdAbstractPredsource<CommandSender>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ARSender full = getFull(SenderIdSourceMixinAllSenderIds.get());
	public static ARSender getFull() { return full; }
	
	private static final ARSender start = getStart(SenderIdSourceMixinAllSenderIds.get());
	public static ARSender getStart() { return start; }
	
	public static ARSender getFull(SenderIdSource source)
	{
		return new ARSender(source, "player", ArgPredictateStringEqualsLC.get());
	}
	
	public static ARSender getStart(SenderIdSource source)
	{
		return new ARSender(source, "player", ArgPredictateStringStartsLC.get());
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
		return IdUtil.getSender(senderId);
	}
	
}
