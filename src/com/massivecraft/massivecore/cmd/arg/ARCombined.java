package com.massivecraft.massivecore.cmd.arg;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;

public class ARCombined extends ArgReaderAbstract<List<?>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected List<ArgReader<?>> inners;
	public List<ArgReader<?>> getInners() { return this.inners; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ARCombined get(Collection<ArgReader<?>> inners) { return new ARCombined(inners); }
	public static ARCombined get(ArgReader<?>... inners) { return new ARCombined(inners); }
	
	public ARCombined(Collection<ArgReader<?>> inners)
	{
		this.inners = new MassiveList<ArgReader<?>>(inners); 
	}
	public ARCombined(ArgReader<?>... inners)
	{
		this(Arrays.asList(inners));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public List<?> read(String arg, CommandSender sender) throws MassiveException
	{
		// Create Ret
		List<Object> ret = new MassiveList<Object>();
		
		// Fill Ret
		List<String> parts = Arrays.asList(arg.split(","));
		if (parts.size() > this.getInners().size())
		{
			throw new MassiveException().addMsg("<b>Too many parts!");
		}
		
		for (int i = 0; i < parts.size(); i++)
		{
			String part = parts.get(i);
			ArgReader<?> argReader = this.getInners().get(i);
			
			Object asdf = argReader.read(part, sender);
			
			ret.add(asdf);
		}
		
		// Return Ret
		return ret;
	}

}
