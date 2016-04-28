package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.FireworkEffect.Type;

public class TypeFireworkEffectType extends TypeEnum<Type>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeFireworkEffectType i = new TypeFireworkEffectType();
	public static TypeFireworkEffectType get() { return i; }
	public TypeFireworkEffectType()
	{
		super(Type.class);
	}

}
