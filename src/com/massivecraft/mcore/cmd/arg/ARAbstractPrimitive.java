package com.massivecraft.mcore.cmd.arg;

import org.bukkit.command.CommandSender;

public abstract class ARAbstractPrimitive<T> implements ArgReader<T>
{
	public abstract String typename();
	public abstract T convert(String arg) throws Exception;
	
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
