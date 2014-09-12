package com.massivecraft.massivecore.mixin;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public abstract class MessageMixinAbstract implements MessageMixin
{
	// -------------------------------------------- //
	// RAW MESSAGE
	// -------------------------------------------- //
	
	// All
	@Override
	public boolean messageAll(String message)
	{
		return this.messageAll(MUtil.list(message));
	}
	
	@Override
	public boolean messageAll(String... messages)
	{
		return this.messageAll(Arrays.asList(messages));
	}
	
	// Predictate
	@Override
	public boolean messagePredictate(Predictate<CommandSender> predictate, String message)
	{
		return this.messagePredictate(predictate, MUtil.list(message));
	}
	
	@Override
	public boolean messagePredictate(Predictate<CommandSender> predictate, String... messages)
	{
		return this.messagePredictate(predictate, Arrays.asList(messages));
	}
	
	// One
	@Override
	public boolean messageOne(Object sendeeObject, String message)
	{
		return this.messageOne(sendeeObject, MUtil.list(message));
	}
	
	@Override
	public boolean messageOne(Object sendeeObject, String... messages)
	{
		return this.messageOne(sendeeObject, Arrays.asList(messages));
	}

	// -------------------------------------------- //
	// PARSE MESSAGE
	// -------------------------------------------- //
	// They are all in abstract!
	
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

}
