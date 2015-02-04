package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommandException;

public abstract class ARAbstractPrimitive<T> extends ArgReaderAbstract<T>
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract String typename();
	public abstract T convert(String arg) throws Exception;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveCommandException
	{
		T result;
		
		try
		{
			result = this.convert(arg);
		}
		catch (Exception e)
		{
			throw new MassiveCommandException().addMsg("<b>Invalid %s \"<h>%s\"<b>.", this.typename(), arg);
		}
		
		return result;
	}
	
}
