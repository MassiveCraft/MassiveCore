package com.massivecraft.massivecore.command.type.enumeration;

import java.util.Set;

import org.bukkit.WorldType;

import com.massivecraft.massivecore.collections.MassiveSet;

public class TypeWorldType extends TypeEnum<WorldType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeWorldType i = new TypeWorldType();
	public static TypeWorldType get() { return i; }
	public TypeWorldType()
	{
		super(WorldType.class);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Set<String> getNamesInner(WorldType value)
	{
		Set<String> ret = new MassiveSet<String>(super.getNamesInner(value));
		
		if (value == WorldType.NORMAL)
		{
			ret.add("normal");
			ret.add("default");
		}
		else if (value == WorldType.VERSION_1_1)
		{
			ret.add("11");
		}
		
		return ret;
	}

}
