package com.massivecraft.massivecore.cmd.arg;

import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

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
	public ArgResult<Set<T>> read(String arg, CommandSender sender)
	{
		// Split into inner args
		String[] innerArgs = arg.split("\\s+");
		
		// Create Ret
		ArgResult<Set<T>> ret = new ArgResult<Set<T>>();
		Set<T> result = new LinkedHashSet<T>();
		
		// For Each
		for (String innerArg : innerArgs)
		{
			ArgResult<T> innerArgResult = this.getInnerArgReader().read(innerArg, sender);
			
			if (innerArgResult.hasErrors())
			{
				ret.setErrors(innerArgResult.getErrors());
				return ret;
			}
			
			if (warnOnDuplicates && ! result.add(innerArgResult.getResult()))
			{
				sender.sendMessage(Txt.parse("<i>An argument was passed in twice and got removed."));
			}
		}
		
		// Set Result
		ret.setResult(result);
		
		// Return Ret
		return ret;
	}

}
