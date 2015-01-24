package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommandException;

public interface ArgReader<T>
{
	public T read(String arg, CommandSender sender) throws MassiveCommandException;
	public T read(CommandSender sender) throws MassiveCommandException;
	public T read(String arg) throws MassiveCommandException;
	public T readArg() throws MassiveCommandException;
}