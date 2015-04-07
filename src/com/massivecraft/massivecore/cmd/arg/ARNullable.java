package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;

public class ARNullable<T> extends ARWrapper<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected AR<T> innerArgReader;
	@Override public AR<T> getInnerArgReader() { return this.innerArgReader; }
	
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
		this.innerArgReader = inner;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		if (MassiveCore.NOTHING_REMOVE.contains(arg)) return null;
		
		return this.getInnerArgReader().read(arg, sender);
	}

}
