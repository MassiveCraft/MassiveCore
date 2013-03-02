package com.massivecraft.mcore.mixin;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.Txt;

public abstract class MessageMixinAbstract implements MessageMixin
{
	// -------------------------------------------- //
	// RAW MESSAGE
	// -------------------------------------------- //
	
	// All
	@Override
	public boolean message(String message)
	{
		return this.message(MUtil.list(message));
	}
	
	@Override
	public boolean message(String... messages)
	{
		return this.message(Arrays.asList(messages));
	}
	
	// Predictate
	@Override
	public boolean message(Predictate<CommandSender> predictate, String message)
	{
		return this.message(predictate, MUtil.list(message));
	}
	
	@Override
	public boolean message(Predictate<CommandSender> predictate, String... messages)
	{
		return this.message(predictate, Arrays.asList(messages));
	}
	
	// One
	@Override
	public boolean message(CommandSender sendee, String message)
	{
		return this.message(sendee, MUtil.list(message));
	}
	
	@Override
	public boolean message(CommandSender sendee, String... messages)
	{
		return this.message(sendee, Arrays.asList(messages));
	}
	
	// One by id
	@Override
	public boolean message(String sendeeId, String message)
	{
		return this.message(sendeeId, MUtil.list(message));
	}
	
	@Override
	public boolean message(String sendeeId, String... messages)
	{
		return this.message(sendeeId, Arrays.asList(messages));
	}

	// -------------------------------------------- //
	// PARSE MESSAGE
	// -------------------------------------------- //
	// They are all in abstract!
	
	// All
	@Override
	public boolean msg(String msg)
	{
		return this.message(Txt.parse(msg));
	}
	
	@Override
	public boolean msg(String msg, Object... args)
	{
		return this.message(Txt.parse(msg, args));
	}
	
	@Override
	public boolean msg(Collection<String> msgs)
	{
		return this.message(Txt.parse(msgs));
	}
	
	// Predictate
	@Override
	public boolean msg(Predictate<CommandSender> predictate, String msg)
	{
		return this.message(predictate, Txt.parse(msg));
	}
	
	@Override
	public boolean msg(Predictate<CommandSender> predictate, String msg, Object... args)
	{
		return this.message(predictate, Txt.parse(msg, args));
	}
	
	@Override
	public boolean msg(Predictate<CommandSender> predictate, Collection<String> msgs)
	{
		return this.message(predictate, Txt.parse(msgs));
	}
	
	// One
	@Override
	public boolean msg(CommandSender sendee, String msg)
	{
		return this.message(sendee, Txt.parse(msg));
	}
	
	@Override
	public boolean msg(CommandSender sendee, String msg, Object... args)
	{
		return this.message(sendee, Txt.parse(msg, args));
	}
	
	@Override
	public boolean msg(CommandSender sendee, Collection<String> msgs)
	{
		return this.message(sendee, Txt.parse(msgs));
	}
	
	// One by id
	@Override
	public boolean msg(String sendeeId, String msg)
	{
		return this.message(sendeeId, Txt.parse(msg));
	}
	
	@Override
	public boolean msg(String sendeeId, String msg, Object... args)
	{
		return this.message(sendeeId, Txt.parse(msg, args));
	}
	
	@Override
	public boolean msg(String sendeeId, Collection<String> msgs)
	{
		return this.message(sendeeId, Txt.parse(msgs));
	}

}
