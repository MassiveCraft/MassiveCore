package com.massivecraft.massivecore.cmd.arg;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;

public class ARCombined extends ARAbstract<List<?>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected List<AR<?>> inners;
	public List<AR<?>> getInners() { return this.inners; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ARCombined get(Collection<AR<?>> inners) { return new ARCombined(inners); }
	public static ARCombined get(AR<?>... inners) { return new ARCombined(inners); }
	
	public ARCombined(Collection<AR<?>> inners)
	{
		this.inners = new MassiveList<AR<?>>(inners); 
	}
	public ARCombined(AR<?>... inners)
	{
		this(Arrays.asList(inners));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	
	//TODO: How to do this?
	/*@Override
	public String getTypeName()
	{
		
	}*/
	
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
			AR<?> argReader = this.getInners().get(i);
			
			Object asdf = argReader.read(part, sender);
			
			ret.add(asdf);
		}
		
		// Return Ret
		return ret;
	}
	
	//TODO: How to do this?
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return null; // ???
	}

}
