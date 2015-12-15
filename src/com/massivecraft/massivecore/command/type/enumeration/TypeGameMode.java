package com.massivecraft.massivecore.command.type.enumeration;

import java.util.Set;

import org.bukkit.GameMode;

import com.massivecraft.massivecore.collections.MassiveSet;

public class TypeGameMode extends TypeEnum<GameMode>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeGameMode i = new TypeGameMode();
	public static TypeGameMode get() { return i; }
	public TypeGameMode()
	{
		super(GameMode.class);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Set<String> getIdsInner(GameMode value)
	{
		Set<String> ret = new MassiveSet<String>(super.getIdsInner(value));
		
		String id = String.valueOf(value.ordinal());
		ret.add(id);
		
		return ret;
	}

}
