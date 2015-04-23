package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public class ARList<E> extends ARAbstract<List<E>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected AR<E> elementArgReader;
	public AR<E> getElementArgReader() { return this.elementArgReader; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <E> ARList<E> get(AR<E> elementArgReader)
	{
		return new ARList<E>(elementArgReader);
	}
	
	public ARList(AR<E> elementArgReader)
	{
		this.elementArgReader = elementArgReader;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return elementArgReader.getTypeName();
	}

	// NOTE: Must be used with argConcatFrom and setErrorOnTooManyArgs(false).
	@Override
	public List<E> read(String arg, CommandSender sender) throws MassiveException
	{
		// Split into inner args
		String[] elementArgs = arg.split("\\s+");
		
		// Create Ret
		List<E> ret = new ArrayList<E>();
		
		// For Each
		for (String elementArg : elementArgs)
		{
			E element = this.getElementArgReader().read(elementArg, sender);
			
			ret.add(element);
		}
		
		// Return Ret
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.getElementArgReader().getTabList(sender, arg);
	}

}
