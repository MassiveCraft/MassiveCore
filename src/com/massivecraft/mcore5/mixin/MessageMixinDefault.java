package com.massivecraft.mcore5.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.SenderUtil;

public class MessageMixinDefault extends MessageMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MessageMixinDefault i = new MessageMixinDefault();
	public static MessageMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean message(CommandSender sender, String message)
	{
		if (sender == null) return false;
		if (message == null) return false;
		sender.sendMessage(message);
		return true;
	}

	@Override
	public boolean message(CommandSender sender, Collection<String> messages)
	{
		if (sender == null) return false;
		if (messages == null) return false;
		for (String message : messages)
		{
			sender.sendMessage(message);
		}
		return true;
	}

	@Override
	public boolean message(String senderId, String message)
	{
		return this.message(SenderUtil.getSender(senderId), message);
	}

	@Override
	public boolean message(String senderId, Collection<String> messages)
	{
		return this.message(SenderUtil.getSender(senderId), messages);
	}

}
