package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public interface ArgReader<T>
{
	public T read(String arg, CommandSender sender) throws MassiveException;
	public T read(CommandSender sender) throws MassiveException;
	public T read(String arg) throws MassiveException;
	public T readArg() throws MassiveException;
}