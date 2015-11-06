package com.massivecraft.massivecore.command.type;

import java.util.Arrays;
import java.util.Collection;
import org.bukkit.potion.PotionEffectType;

public class TypePotionEffectType extends TypeAbstractChoice<PotionEffectType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePotionEffectType i = new TypePotionEffectType();
	public static TypePotionEffectType get() { return i; }
	
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

	@Override
	public Collection<PotionEffectType> getAll()
	{
		return Arrays.asList(PotionEffectType.values());
	}
	
}
