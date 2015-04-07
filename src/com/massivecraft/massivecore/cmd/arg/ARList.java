package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;

public class ARList<E> extends ARCollection<List<E>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected AR<E> elementArgReader;
	public AR<E> getElementArgReader() { return this.elementArgReader; }
	@Override public AR<E> getInnerArgReader() { return this.getElementArgReader(); }
	
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

	// NOTE: Must be used with argConcatFrom and setErrorOnTooManyArgs(false).
	@Override
	public List<E> read(String arg, CommandSender sender) throws MassiveException
	{
		// Split into inner args
		String[] elementArgs = Txt.REGEX_WHITESPACE.split(arg);
		
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

}
