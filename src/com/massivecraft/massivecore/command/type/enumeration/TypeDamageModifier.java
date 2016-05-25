package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public class TypeDamageModifier extends TypeEnum<DamageModifier>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDamageModifier i = new TypeDamageModifier();
	public static TypeDamageModifier get() { return i; }
	public TypeDamageModifier()
	{
		super(DamageModifier.class);
	}

}
