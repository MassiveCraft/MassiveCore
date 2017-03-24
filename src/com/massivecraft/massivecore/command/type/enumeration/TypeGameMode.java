package com.massivecraft.massivecore.command.type.enumeration;

import com.massivecraft.massivecore.collections.MassiveSet;
import org.bukkit.GameMode;

import java.util.Set;

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
		Set<String> ret = new MassiveSet<>(super.getIdsInner(value));
		
		int idInt = getIntegerId(value);
		String idString = String.valueOf(idInt);
		ret.add(idString);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	// The reason we can not simply do ".ordinal()" is Survival and Creative has the wrong order.
	
	public static int getIntegerId(GameMode gameMode)
	{
		if (gameMode == null) throw new NullPointerException("gameMode");
		
		switch (gameMode)
		{
			case SURVIVAL: return 0;
			case CREATIVE: return 1;
			case ADVENTURE: return 2;
			case SPECTATOR: return 3;
		}
		
		return gameMode.ordinal();
	}
	

}
