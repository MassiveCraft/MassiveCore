package com.massivecraft.massivecore.cmd.arg;

import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;

public class ARSet<T> extends ArgReaderAbstract<Set<T>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected ArgReader<T> innerArgReader;
	public ArgReader<T> getInnerArgReader() { return this.innerArgReader; }
	
	protected boolean warnOnDuplicates;
	public boolean getWarnOnDuplicate() { return warnOnDuplicates; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ARSet<T> get(ArgReader<T> innerArgReader, boolean warnOnDuplicates)
	{
		return new ARSet<T>(innerArgReader, warnOnDuplicates);
	}
	
	public ARSet(ArgReader<T> innerArgReader, boolean warnOnDuplicates)
	{
		this.innerArgReader = innerArgReader;
		this.warnOnDuplicates = warnOnDuplicates;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
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
			sender.sendMessage(Txt.parse("<i>Some duplicate arguments were removed"));
		}
		
		// Return Ret
		return ret;
	}

}
