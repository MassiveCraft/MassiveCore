package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class ARList<T> extends ArgReaderAbstract<List<T>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected ArgReader<T> innerArgReader;
	public ArgReader<T> getInnerArgReader() { return this.innerArgReader; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ARList<T> get(ArgReader<T> innerArgReader)
	{
		return new ARList<T>(innerArgReader);
	}
	
	public ARList(ArgReader<T> innerArgReader)
	{
		this.innerArgReader = innerArgReader;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	// NOTE: Must be used with argConcatFrom and setErrorOnTooManyArgs(false).
	@Override
	public ArgResult<List<T>> read(String arg, CommandSender sender)
	{
		// Split into inner args
		String[] innerArgs = arg.split("\\s+");
		
		// Create Ret
		ArgResult<List<T>> ret = new ArgResult<List<T>>();
		List<T> result = new ArrayList<T>();
		
		// For Each
		for (String innerArg : innerArgs)
		{
			ArgResult<T> innerArgResult = this.getInnerArgReader().read(innerArg, sender);
			
			if (innerArgResult.hasErrors())
			{
				ret.setErrors(innerArgResult.getErrors());
				return ret;
			}
			
			result.add(innerArgResult.getResult());
		}
		
		// Set Result
		ret.setResult(result);
		
		// Return Ret
		return ret;
	}

}
