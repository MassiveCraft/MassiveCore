package com.massivecraft.massivecore.command.type.sender;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;

public class TypeSender extends TypeSenderIdAbstract<CommandSender>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final TypeSender i = new TypeSender();
	public static TypeSender get() { return i; }
	private TypeSender()
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
