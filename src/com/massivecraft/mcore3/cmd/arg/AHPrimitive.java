package com.massivecraft.mcore3.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore3.MPlugin;

public abstract class AHPrimitive<T> extends AHBase<T>
{
	protected abstract String getPrimitiveName();
	
	protected abstract T unsafeConvert(String str) throws Exception;
	
	@Override
	public T parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		if (str == null) return null;
		try
		{
			T ret = this.unsafeConvert(str);
			return ret;
		}
		catch (Exception e)
		{
			this.error.add("<b>\"<p>"+str+"<b>\" is not a valid "+this.getPrimitiveName()+".");
		}
		return null;
	}
}
