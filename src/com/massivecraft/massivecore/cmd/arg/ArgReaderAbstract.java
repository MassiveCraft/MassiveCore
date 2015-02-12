package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public abstract class ArgReaderAbstract<T> implements ArgReader<T>
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T read(CommandSender sender) throws MassiveException
	{
		return this.read(null, sender);
	}

	@Override
	public T read(String arg) throws MassiveException
	{
		return this.read(arg, null);
	}

	@Override
	public T readArg() throws MassiveException
	{
		return this.read(null, null);
	}
	
}
