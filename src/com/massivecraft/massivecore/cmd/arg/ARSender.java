package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
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
		super(SenderIdSourceMixinAllSenderIds.get(), SenderPresence.LOCAL, SenderType.ANY);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public CommandSender getResultForSenderId(String senderId)
	{
		// Null check is done in IdUtil
		return IdUtil.getSender(senderId);
	}

}
