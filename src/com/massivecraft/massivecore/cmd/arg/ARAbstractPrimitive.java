package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

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
	public ArgResult<T> read(String arg, CommandSender sender)
	{
		ArgResult<T> result = new ArgResult<T>();
		
		try
		{
			result.setResult(this.convert(arg));
		}
		catch (Exception e)
		{
			result.getErrors().add("<b>Invalid "+this.typename()+" \"<h>"+arg+"\"<b>.");
		}
		
		return result;
	}
	
}
