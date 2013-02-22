package com.massivecraft.mcore5.mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.Txt;

public abstract class MessageMixinAbstract implements MessageMixin
{
	@Override
	public boolean message(CommandSender sender, String... messages)
	{
		Collection<String> coll = new ArrayList<String>(Arrays.asList(messages));
		return this.message(sender, coll);
	}

	@Override
	public boolean message(String senderId, String... messages)
	{
		Collection<String> coll = new ArrayList<String>(Arrays.asList(messages));
		return this.message(senderId, coll);
	}

	@Override
	public boolean msg(CommandSender sender, String msg)
	{
		return this.message(sender, Txt.parse(msg));
	}

	@Override
	public boolean msg(CommandSender sender, String msg, Object... args)
	{
		return this.message(sender, Txt.parse(msg, args));
	}

	@Override
	public boolean msg(CommandSender sender, Collection<String> msgs)
	{
		List<String> messages = new ArrayList<String>();
		for (String msg : msgs)
		{
			String message = Txt.parse(msg);
			messages.add(message);
		}
		return this.message(sender, messages);
	}

	@Override
	public boolean msg(String senderId, String msg)
	{
		return this.message(senderId, Txt.parse(msg));
	}

	@Override
	public boolean msg(String senderId, String msg, Object... args)
	{
		return this.message(senderId, Txt.parse(msg, args));
	}

	@Override
	public boolean msg(String senderId, Collection<String> msgs)
	{
		List<String> messages = new ArrayList<String>();
		for (String msg : msgs)
		{
			String message = Txt.parse(msg);
			messages.add(message);
		}
		return this.message(senderId, messages);
	}

}
