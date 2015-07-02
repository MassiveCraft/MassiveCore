package com.massivecraft.massivecore.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.nms.NmsPacket;
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
		for (CommandSender sender : IdUtil.getLocalSenders())
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
		for (CommandSender sender : IdUtil.getLocalSenders())
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
	
	// Raw message aka. JsonString
	@Override
	public boolean messageRawAll(Collection<Mson> msons)
	{
		if (msons == null) return false;
		for (CommandSender sender : IdUtil.getLocalSenders())
		{
			this.messageRawOne(sender, msons);
		}
		return true;
	}
	
	@Override
	public boolean messageRawPredictate(Predictate<CommandSender> predictate, Collection<Mson> msons)
	{
		if (predictate == null) return false;
		if (msons == null) return false;
		for (CommandSender sender : IdUtil.getLocalSenders())
		{
			if ( ! predictate.apply(sender)) continue;
			this.messageRawOne(sender, msons);
		}
		return true;
	}

	@Override
	public boolean messageRawOne(Object sendeeObject, Collection<Mson> msons)
	{
		if (msons == null) return false;
		
		CommandSender sender = IdUtil.getSender(sendeeObject);
		if (sender == null) return false;
		
		if (sender instanceof Player && NmsPacket.get().isAvailable())
		{
			Player player = (Player) sender;
			for (Mson mson : msons)
			{
				NmsPacket.sendRaw(player, mson.toRaw());
			}
		}
		else
		{
			for (Mson mson : msons)
			{
				sender.sendMessage(mson.toPlain());
			}
		}

		return true;
	}

}
