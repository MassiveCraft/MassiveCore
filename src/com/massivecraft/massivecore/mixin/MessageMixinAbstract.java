package com.massivecraft.massivecore.mixin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.util.Txt;

public abstract class MessageMixinAbstract implements MessageMixin
{
	// -------------------------------------------- //
	// MSG STRING
	// -------------------------------------------- //
	// All implemented in abstract!
	
	// All
	@Override
	public boolean msgAll(String msg)
	{
		return this.messageAll(Txt.parse(msg));
	}
	
	@Override
	public boolean msgAll(String msg, Object... args)
	{
		return this.messageAll(Txt.parse(msg, args));
	}
	
	@Override
	public boolean msgAll(Collection<String> msgs)
	{
		return this.messageAll(Txt.parse(msgs));
	}
	
	// Predicate
	@Override
	public boolean msgPredicate(Predicate<CommandSender> predicate, String msg)
	{
		return this.messagePredicate(predicate, Txt.parse(msg));
	}
	
	@Override
	public boolean msgPredicate(Predicate<CommandSender> predicate, String msg, Object... args)
	{
		return this.messagePredicate(predicate, Txt.parse(msg, args));
	}
	
	@Override
	public boolean msgPredicate(Predicate<CommandSender> predicate, Collection<String> msgs)
	{
		return this.messagePredicate(predicate, Txt.parse(msgs));
	}
	
	// One
	@Override
	public boolean msgOne(Object sendeeObject, String msg)
	{
		return this.messageOne(sendeeObject, Txt.parse(msg));
	}
	
	@Override
	public boolean msgOne(Object sendeeObject, String msg, Object... args)
	{
		return this.messageOne(sendeeObject, Txt.parse(msg, args));
	}
	
	@Override
	public boolean msgOne(Object sendeeObject, Collection<String> msgs)
	{
		return this.messageOne(sendeeObject, Txt.parse(msgs));
	}
	
	// -------------------------------------------- //
	// MESSAGE
	// -------------------------------------------- //
	
	// All
	@Override
	public boolean messageAll(Object message)
	{
		return this.messageAll(Collections.singleton(message));
	}
	
	@Override
	public boolean messageAll(Object... messages)
	{
		return this.messageAll(Arrays.asList(messages));
	}
	
	// Predicate
	@Override
	public boolean messagePredicate(Predicate<CommandSender> predicate, Object message)
	{
		return this.messagePredicate(predicate, Collections.singleton(message));
	}
	
	@Override
	public boolean messagePredicate(Predicate<CommandSender> predicate, Object... messages)
	{
		return this.messagePredicate(predicate, Arrays.asList(messages));
	}
	
	// One
	@Override
	public boolean messageOne(Object sendeeObject, Object message)
	{
		return this.messageOne(sendeeObject, Collections.singleton(message));
	}
	
	@Override
	public boolean messageOne(Object sendeeObject, Object... messages)
	{
		return this.messageOne(sendeeObject, Arrays.asList(messages));
	}
	
}
