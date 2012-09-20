package com.massivecraft.mcore4.cmd.arg;

import com.massivecraft.mcore4.cmd.MCommand;

public abstract class ARAbstractPrimitive<T> implements ArgReader<T>
{
	public abstract String typename();
	public abstract T convert(String str) throws Exception;
	
	@Override
	public ArgResult<T> read(String str, MCommand mcommand)
	{
		ArgResult<T> result = new ArgResult<T>();
		
		try
		{
			result.setResult(this.convert(str));
		}
		catch (Exception e)
		{
			result.getErrors().add("<b>Invalid "+this.typename()+" \"<h>"+str+"\"<b>.");
		}
		
		return result;
	}
}
