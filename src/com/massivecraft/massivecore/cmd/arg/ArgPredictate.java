package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public interface ArgPredictate<T>
{
	public boolean apply(T type, String arg, CommandSender sender);
}
