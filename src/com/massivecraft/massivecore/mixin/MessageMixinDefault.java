package com.massivecraft.massivecore.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.nms.NmsPacket;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.util.IdUtil;

public class MessageMixinDefault extends MessageMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MessageMixinDefault i = new MessageMixinDefault();
	public static MessageMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// MESSAGE
	// -------------------------------------------- //
	
	// All
	// NOTE: Targets messageOne
	@Override
	public boolean messageAll(Collection<?> messages)
	{
		// Check Messages
		if (messages == null) return false;
		if (messages.isEmpty()) return false;
		
		// Here
		for (CommandSender sender : IdUtil.getLocalSenders())
		{
			this.messageOne(sender, messages);
		}
		
		// Return
		return true;
	}
	
	// Predicate
	// NOTE: Targets messageOne
	@Override
	public boolean messagePredicate(Predicate<CommandSender> predicate, Collection<?> messages)
	{
		// Check Predicate
		if (predicate == null) return false;
		
		// Check Messages
		if (messages == null) return false;
		if (messages.isEmpty()) return false;
		
		// Here
		for (CommandSender sender : IdUtil.getLocalSenders())
		{
			if (!predicate.apply(sender)) continue;
			this.messageOne(sender, messages);
		}
		
		// Return
		return true;
	}
	
	// One
	// NOTE: The core implementation
	@SuppressWarnings("unchecked")
	@Override
	public boolean messageOne(Object sendeeObject, Collection<?> messages)
	{
		// Check Sendee
		CommandSender sendee = IdUtil.getSender(sendeeObject);
		if (sendee == null) return false;
		
		// Check Messages
		if (messages == null) return false;
		if (messages.isEmpty()) return false;
		
		// Type Switch
		Object first = messages.iterator().next();
		if (first instanceof String)
		{
			// String
			Collection<String> strings = (Collection<String>) messages;
			sendee.sendMessage(strings.toArray(new String[0]));
			return true;
		}
		else if (first instanceof Mson)
		{
			// Mson
			Collection<Mson> msons = (Collection<Mson>) messages;
			if (sendee instanceof Player && NmsPacket.get().isAvailable())
			{
				Player player = (Player) sendee;
				for (Mson mson : msons)
				{
					NmsPacket.sendRaw(player, mson.toRaw());
				}
			}
			else
			{
				for (Mson mson : msons)
				{
					sendee.sendMessage(mson.toPlain(true));
				}
			}
			return true;
		}
		else
		{
			throw new IllegalArgumentException("The messages must be either String or Mson.");
		}
	}

}
