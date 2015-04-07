package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

public class ARSet<E> extends ARCollection<Set<E>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final AR<E> elementArgReader;
	public AR<E> getElementArgReader() { return this.elementArgReader; }
	@Override public AR<E> getInnerArgReader() { return this.getElementArgReader(); }
	
	protected final boolean warnOnDuplicates;
	public boolean getWarnOnDuplicate() { return warnOnDuplicates; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <E> ARSet<E> get(AR<E> elementArgReader, boolean warnOnDuplicates)
	{
		return new ARSet<E>(elementArgReader, warnOnDuplicates);
	}
	
	public ARSet(AR<E> elementArgReader, boolean warnOnDuplicates)
	{
		this.elementArgReader = elementArgReader;
		this.warnOnDuplicates = warnOnDuplicates;
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
	public Set<E> read(String arg, CommandSender sender) throws MassiveException
	{
		// Split into inner args
		String[] elementArgs = Txt.REGEX_WHITESPACE.split(arg);
		
		// Create Ret
		Set<E> ret = new LinkedHashSet<E>();
		
		boolean duplicates = false;
		
		// For Each
		for (String elementArg : elementArgs)
		{
			E element = this.getElementArgReader().read(elementArg, sender);
			
			duplicates = ( ! ret.add(element) || duplicates);
		}
		
		if (warnOnDuplicates && duplicates)
		{
			Mixin.msgOne(sender, "<i>Some duplicate command input were removed.");
		}
		
		// Return Ret
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return elementArgReader.getTabList(sender, arg);
	}

}
