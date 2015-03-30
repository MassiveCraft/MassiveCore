package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;

public class ARNullable<T> extends ArgReaderAbstract<T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ARNullable<T> get(ArgReader<T> inner)
	{
		return new ARNullable<T>(inner);
	}
	
	public ARNullable(ArgReader<T> inner)
	{
		this.inner = inner;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected ArgReader<T> inner;
	public ArgReader<T> getInner() { return this.inner; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		if (MassiveCore.NOTHING_REMOVE.contains(arg)) return null;
		
		return this.getInner().read(arg, sender);
	}

}
