package com.massivecraft.mcore1.cmd;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore1.plugin.MPlugin;

public abstract class AHPrimitive<T> extends AHBase<T>
{
	protected abstract String getPrimitiveName();
	
	protected abstract T unsafeConvert(String str) throws Exception;
	
	@Override
	public T parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error = null;
		if (str == null) return null;
		try
		{
			T ret = this.unsafeConvert(str);
			return ret;
		}
		catch (Exception e)
		{
			this.error = "<b>\"<p>"+str+"<b>\" is not a valid "+this.getPrimitiveName()+".";
		}
		return null;
	}
}
