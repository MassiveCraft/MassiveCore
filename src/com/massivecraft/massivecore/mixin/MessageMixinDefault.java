package com.massivecraft.massivecore.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.util.IdUtil;

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
	public boolean messageAll(Collection<String> messages)
	{
		if (messages == null) return false;
		for (CommandSender sender : IdUtil.getOnlineSenders())
		{
			this.messageOne(sender, messages);
		}
		return true;
	}
	
	@Override
	public boolean messagePredictate(Predictate<CommandSender> predictate, Collection<String> messages)
	{
		if (predictate == null) return false;
		if (messages == null) return false;
		for (CommandSender sender : IdUtil.getOnlineSenders())
		{
			if (!predictate.apply(sender)) continue;
			this.messageOne(sender, messages);
		}
		return true;
	}
	
	@Override
	public boolean messageOne(Object sendeeObject, Collection<String> messages)
	{
		CommandSender sendee = IdUtil.getSender(sendeeObject);
		if (sendee == null) return false;
		if (messages == null) return false;
		sendee.sendMessage(messages.toArray(new String[0]));
		return true;
	}

}
