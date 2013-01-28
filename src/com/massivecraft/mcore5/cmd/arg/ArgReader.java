package com.massivecraft.mcore5.cmd.arg;

import org.bukkit.command.CommandSender;

public interface ArgReader<T>
{
	public ArgResult<T> read(String str, CommandSender sender);
}