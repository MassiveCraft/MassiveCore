package com.massivecraft.mcore1.cmd;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore1.plugin.MPlugin;

public abstract class AHBase<T> implements IArgHandler<T>
{
	protected String error = null;
	
	@Override
	public abstract T parse(String str, String style, CommandSender sender, MPlugin p);

	@Override
	public String getError()
	{
		return this.error;
	}
}
