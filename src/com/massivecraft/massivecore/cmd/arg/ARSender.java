package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;

public class ARSender extends ARSenderIdAbstract<CommandSender>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ARSender i = new ARSender();
	public static ARSender get() { return i; }
	private ARSender()
	{
		super(SenderIdSourceMixinAllSenderIds.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public CommandSender getResultForSenderId(String senderId)
	{
		if (senderId == null) return null;
		return IdUtil.getSender(senderId);
	}
	
}
