package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;

import org.bukkit.command.CommandSender;

public abstract class TypeWrapper<T> extends TypeAbstract<T>
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Type<?> getInnerType();
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return this.getInnerType().getTypeName();
	}

	@Override
	public boolean isValid(String arg, CommandSender sender)
	{
		return this.getInnerType().isValid(arg, sender);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.getInnerType().getTabList(sender, arg);
	}

	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getInnerType().allowSpaceAfterTab();
	}
	
}
