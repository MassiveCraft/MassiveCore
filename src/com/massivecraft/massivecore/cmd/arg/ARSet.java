package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mixin.Mixin;

public class ARSet<T> extends ARAbstract<Set<T>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final AR<T> innerArgReader;
	public AR<T> getInnerArgReader() { return this.innerArgReader; }
	
	private final boolean warnOnDuplicates;
	public boolean getWarnOnDuplicate() { return warnOnDuplicates; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ARSet<T> get(AR<T> innerArgReader, boolean warnOnDuplicates)
	{
		return new ARSet<T>(innerArgReader, warnOnDuplicates);
	}
	
	public ARSet(AR<T> innerArgReader, boolean warnOnDuplicates)
	{
		this.innerArgReader = innerArgReader;
		this.warnOnDuplicates = warnOnDuplicates;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return innerArgReader.getTypeName();
	}

	// NOTE: Must be used with argConcatFrom and setErrorOnTooManyArgs(false).
	@Override
	public Set<T> read(String arg, CommandSender sender) throws MassiveException
	{
		// Split into inner args
		String[] innerArgs = arg.split("\\s+");
		
		// Create Ret
		Set<T> ret = new LinkedHashSet<T>();
		
		boolean duplicates = false;
		
		// For Each
		for (String innerArg : innerArgs)
		{
			T innerArgResult = this.getInnerArgReader().read(innerArg, sender);
			
			duplicates = ( ! ret.add(innerArgResult) || duplicates);
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
		return innerArgReader.getTabList(sender, arg);
	}

}
