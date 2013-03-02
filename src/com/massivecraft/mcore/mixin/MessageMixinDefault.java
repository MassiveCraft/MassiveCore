package com.massivecraft.mcore.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.util.SenderUtil;

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
	public boolean message(Collection<String> messages)
	{
		if (messages == null) return false;
		for (CommandSender sender : SenderUtil.getOnlineSenders())
		{
			this.message(sender, messages);
		}
		return true;
	}
	
	@Override
	public boolean message(Predictate<CommandSender> predictate, Collection<String> messages)
	{
		if (predictate == null) return false;
		if (messages == null) return false;
		for (CommandSender sender : SenderUtil.getOnlineSenders())
		{
			if (!predictate.apply(sender)) continue;
			this.message(sender, messages);
		}
		return true;
	}
	
	@Override
	public boolean message(CommandSender sendee, Collection<String> messages)
	{
		if (sendee == null) return false;
		if (messages == null) return false;
		for (String message : messages)
		{
			sendee.sendMessage(message);
		}
		return true;
	}
	
	@Override
	public boolean message(String sendeeId, Collection<String> messages)
	{
		if (sendeeId == null) return false;
		if (messages == null) return false;
		return this.message(SenderUtil.getSender(sendeeId), messages);
	}

}
