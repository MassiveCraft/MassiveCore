package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

public abstract class ARWrapper<T> extends ARAbstract<T>
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract AR<?> getInnerArgReader();
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return this.getInnerArgReader().getTypeName();
	}

	@Override
	public boolean isValid(String arg, CommandSender sender)
	{
		return this.getInnerArgReader().isValid(arg, sender);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.getInnerArgReader().getTabList(sender, arg);
	}

	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getInnerArgReader().allowSpaceAfterTab();
	}
	
}
