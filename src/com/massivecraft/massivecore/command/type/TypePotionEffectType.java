package com.massivecraft.massivecore.command.type;

import org.bukkit.potion.PotionEffectType;

public class TypePotionEffectType extends TypeAbstractChoice<PotionEffectType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePotionEffectType i = new TypePotionEffectType();
	public static TypePotionEffectType get() { return i; }
	public TypePotionEffectType()
	{
		super(PotionEffectType.class);
		this.setAll(PotionEffectType.values());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getNameInner(PotionEffectType value)
	{
		return value.getName();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getIdInner(PotionEffectType value)
	{
		return String.valueOf(value.getId());
	}
	
}
