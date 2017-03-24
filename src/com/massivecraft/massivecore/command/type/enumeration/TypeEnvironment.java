package com.massivecraft.massivecore.command.type.enumeration;

import com.massivecraft.massivecore.collections.MassiveSet;
import org.bukkit.World.Environment;

import java.util.Set;

public class TypeEnvironment extends TypeEnum<Environment>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeEnvironment i = new TypeEnvironment();
	public static TypeEnvironment get() { return i; }
	public TypeEnvironment()
	{
		super(Environment.class);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Set<String> getNamesInner(Environment value)
	{
		Set<String> ret = new MassiveSet<>(super.getNamesInner(value));
		
		if (value == Environment.NORMAL)
		{
			ret.add("normal");
			ret.add("default");
			ret.add("thenormal");
			ret.add("thedefault");
		}
		else if (value == Environment.NETHER)
		{
			ret.add("nether");
			ret.add("thenether");
		}
		else if (value == Environment.THE_END)
		{
			ret.add("end");
		}
		
		return ret;
	}

}
