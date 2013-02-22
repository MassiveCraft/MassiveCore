package com.massivecraft.mcore.cmd.arg;

import org.bukkit.command.CommandSender;

public interface ArgReader<T>
{
	public ArgResult<T> read(String arg, CommandSender sender);
}