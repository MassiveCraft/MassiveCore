package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;

public class ARNullable<T> extends ARAbstract<T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ARNullable<T> get(AR<T> inner)
	{
		return new ARNullable<T>(inner);
	}
	
	public ARNullable(AR<T> inner)
	{
		if (inner == null) throw new IllegalArgumentException("inner param is null");
		this.inner = inner;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected AR<T> inner;
	public AR<T> getInner() { return this.inner; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getTypeName()
	{
		return this.getInner().getTypeName();
	}
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		if (MassiveCore.NOTHING_REMOVE.contains(arg)) return null;
		
		return this.getInner().read(arg, sender);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.getInner().getTabList(sender, arg);
	}

}
