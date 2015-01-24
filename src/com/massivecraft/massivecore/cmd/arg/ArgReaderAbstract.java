package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommandException;

public abstract class ArgReaderAbstract<T> implements ArgReader<T>
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T read(CommandSender sender) throws MassiveCommandException
	{
		return this.read(null, sender);
	}

	@Override
	public T read(String arg) throws MassiveCommandException
	{
		return this.read(arg, null);
	}

	@Override
	public T readArg() throws MassiveCommandException
	{
		return this.read(null, null);
	}
	
}
