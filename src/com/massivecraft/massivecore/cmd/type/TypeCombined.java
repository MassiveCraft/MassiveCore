package com.massivecraft.massivecore.cmd.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;

public class TypeCombined extends TypeAbstract<List<?>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected List<Type<?>> inners;
	public List<Type<?>> getInners() { return this.inners; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static TypeCombined get(Collection<Type<?>> inners) { return new TypeCombined(inners); }
	public static TypeCombined get(Type<?>... inners) { return new TypeCombined(inners); }
	
	public TypeCombined(Collection<Type<?>> inners)
	{
		this.inners = new MassiveList<Type<?>>(inners); 
	}
	public TypeCombined(Type<?>... inners)
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
			Type<?> type = this.getInners().get(i);
			
			Object asdf = type.read(part, sender);
			
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
