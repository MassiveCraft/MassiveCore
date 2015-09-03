package com.massivecraft.massivecore.mixin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Predictate;
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
	
	// Predictate
	@Override
	public boolean msgPredictate(Predictate<CommandSender> predictate, String msg)
	{
		return this.messagePredictate(predictate, Txt.parse(msg));
	}
	
	@Override
	public boolean msgPredictate(Predictate<CommandSender> predictate, String msg, Object... args)
	{
		return this.messagePredictate(predictate, Txt.parse(msg, args));
	}
	
	@Override
	public boolean msgPredictate(Predictate<CommandSender> predictate, Collection<String> msgs)
	{
		return this.messagePredictate(predictate, Txt.parse(msgs));
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
	public boolean messagePredictate(Predictate<CommandSender> predictate, Object message)
	{
		return this.messagePredictate(predictate, Collections.singleton(message));
	}
	
	@Override
	public boolean messagePredictate(Predictate<CommandSender> predictate, Object... messages)
	{
		return this.messagePredictate(predictate, Arrays.asList(messages));
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
