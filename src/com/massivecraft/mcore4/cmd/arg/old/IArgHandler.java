package com.massivecraft.mcore4.cmd.arg.old;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.MPlugin;

public interface IArgHandler<T>
{
	// Parse result returned - or null and something in the error message.
	public T parse(String str, String style, CommandSender sender, MPlugin p);
	
	// Error here - or null.
	public Collection<String> getErrors();
}
