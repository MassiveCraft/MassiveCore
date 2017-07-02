package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.nms.NmsChat;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MixinMessage extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinMessage d = new MixinMessage();
	private static MixinMessage i = d;
	public static MixinMessage get() { return i; }
	
	// -------------------------------------------- //
	// MSG > ALL
	// -------------------------------------------- //
	
	public boolean msgAll(String msg)
	{
		return this.messageAll(Txt.parse(msg));
	}
	
	public boolean msgAll(String msg, Object... args)
	{
		return this.messageAll(Txt.parse(msg, args));
	}
	
	public boolean msgAll(Collection<String> msgs)
	{
		return this.messageAll(Txt.parse(msgs));
	}
	
	// -------------------------------------------- //
	// MSG > PREDICATE
	// -------------------------------------------- //
	
	public boolean msgPredicate(Predicate<CommandSender> predicate, String msg)
	{
		return this.messagePredicate(predicate, Txt.parse(msg));
	}
	
	public boolean msgPredicate(Predicate<CommandSender> predicate, String msg, Object... args)
	{
		return this.messagePredicate(predicate, Txt.parse(msg, args));
	}
	
	public boolean msgPredicate(Predicate<CommandSender> predicate, Collection<String> msgs)
	{
		return this.messagePredicate(predicate, Txt.parse(msgs));
	}
	
	// -------------------------------------------- //
	// MSG > ONE
	// -------------------------------------------- //
	
	public boolean msgOne(Object sendeeObject, String msg)
	{
		return this.messageOne(sendeeObject, Txt.parse(msg));
	}
	
	public boolean msgOne(Object sendeeObject, String msg, Object... args)
	{
		return this.messageOne(sendeeObject, Txt.parse(msg, args));
	}
	
	public boolean msgOne(Object sendeeObject, Collection<String> msgs)
	{
		return this.messageOne(sendeeObject, Txt.parse(msgs));
	}
	
	// -------------------------------------------- //
	// MESSAGE > ALL
	// -------------------------------------------- //
	
	public boolean messageAll(Object message)
	{
		return this.messageAll(asCollection(message));
	}
	
	public boolean messageAll(Object... messages)
	{
		return this.messageAll(asCollection(messages));
	}
	
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
	
	// -------------------------------------------- //
	// MESSAGE > PREDICATE
	// -------------------------------------------- //
	
	public boolean messagePredicate(Predicate<CommandSender> predicate, Object message)
	{
		return this.messagePredicate(predicate, asCollection(message));
	}
	
	public boolean messagePredicate(Predicate<CommandSender> predicate, Object... messages)
	{
		return this.messagePredicate(predicate, asCollection(messages));
	}
	
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
			if ( ! predicate.apply(sender)) continue;
			this.messageOne(sender, messages);
		}
		
		// Return
		return true;
	}
	
	// -------------------------------------------- //
	// MESSAGE > ONE
	// -------------------------------------------- //
	
	public boolean messageOne(Object sendeeObject, Object message)
	{
		return this.messageOne(sendeeObject, asCollection(message));
	}
	
	public boolean messageOne(Object sendeeObject, Object... messages)
	{
		return this.messageOne(sendeeObject, asCollection(messages));
	}
	
	public boolean messageOne(Object sendeeObject, Collection<?> messages)
	{
		// Check Sendee
		CommandSender sendee = IdUtil.getSender(sendeeObject);
		if (sendee == null) return false;
		
		// Check Messages
		if (messages == null) return false;
		if (messages.isEmpty()) return false;
		
		// For each Message
		for (Object message : messages)
		{
			if (message instanceof String)
			{
				String plain = (String)message;
				NmsChat.get().sendChatPlain(sendee, plain);
			}
			else if (message instanceof Mson)
			{
				Mson mson = (Mson)message;
				NmsChat.get().sendChatMson(sendee, mson);
			}
			else
			{
				String desc = (message == null ? "null" : message.getClass().getSimpleName());
				throw new IllegalArgumentException(desc + " is neither String nor Mson.");
			}
		}
		return true;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public Collection<?> asCollection(Object message)
	{
		if (message instanceof Collection) return (Collection<?>) message;
		if (message instanceof Object[]) return Arrays.asList((Object[]) message);
		return Collections.singleton(message);
	}

}
