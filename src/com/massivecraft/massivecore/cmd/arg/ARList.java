package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public class ARList<T> extends ARAbstract<List<T>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected AR<T> innerArgReader;
	public AR<T> getInnerArgReader() { return this.innerArgReader; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ARList<T> get(AR<T> innerArgReader)
	{
		return new ARList<T>(innerArgReader);
	}
	
	public ARList(AR<T> innerArgReader)
	{
		this.innerArgReader = innerArgReader;
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

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.getInnerArgReader().getTabList(sender, arg);
	}

}
