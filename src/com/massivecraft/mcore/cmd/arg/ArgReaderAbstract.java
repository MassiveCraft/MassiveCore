package com.massivecraft.mcore.cmd.arg;

import org.bukkit.command.CommandSender;

public abstract class ArgReaderAbstract<T> implements ArgReader<T>
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ArgResult<T> read(CommandSender sender)
	{
		return this.read(null, sender);
	}

	@Override
	public ArgResult<T> read(String arg)
	{
		return this.read(arg, null);
	}

	@Override
	public ArgResult<T> read()
	{
		return this.read(null, null);
	}
	
}
