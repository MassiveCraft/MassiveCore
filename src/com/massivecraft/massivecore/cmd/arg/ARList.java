package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

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
	public List<T> read(String arg, CommandSender sender) throws MassiveException
	{
		// Split into inner args
		String[] innerArgs = arg.split("\\s+");
		
		// Create Ret
		List<T> ret = new ArrayList<T>();
		
		// For Each
		for (String innerArg : innerArgs)
		{
			T innerArgResult = this.getInnerArgReader().read(innerArg, sender);
			
			ret.add(innerArgResult);
		}
		
		// Return Ret
		return ret;
	}

}
