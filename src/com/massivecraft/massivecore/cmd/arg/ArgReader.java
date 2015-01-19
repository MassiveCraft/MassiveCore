package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public interface ArgReader<T>
{
	public ArgResult<T> read(String arg, CommandSender sender);
	public ArgResult<T> read(CommandSender sender);
	public ArgResult<T> read(String arg);
	public ArgResult<T> read();
}