package com.massivecraft.massivecore.command.type.combined;

import java.util.List;

import org.bukkit.potion.PotionEffectType;

import com.massivecraft.massivecore.PotionEffectWrap;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.TypePotionEffectType;
import com.massivecraft.massivecore.command.type.primitive.TypeBoolean;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class TypePotionEffectWrap extends TypeCombined<PotionEffectWrap>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePotionEffectWrap i = new TypePotionEffectWrap();
	public static TypePotionEffectWrap get() { return i; }

	public TypePotionEffectWrap()
	{
		super(
			TypePotionEffectType.get(),
			TypeInteger.get(),
			TypeInteger.get(),
			TypeBoolean.getYes(),
			TypeBoolean.getYes()
		);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<Object> split(PotionEffectWrap value)
	{
		return new MassiveList<Object>(
			value.getPotionEffectType(),
			value.getAmplifier(),
			value.getDuration(),
			value.isAmbient(),
			value.isParticles()
		);
	}
	
	@Override
	public PotionEffectWrap combine(List<Object> parts)
	{
		// Create
		PotionEffectWrap ret = new PotionEffectWrap();

		// Fill
		for (int i = 0 ; i < parts.size() ; i++)
		{
			Object part = parts.get(i);
			
			if (i == 0)
			{
				PotionEffectType potionEffectType = (PotionEffectType)part;
				ret.setPotionEffectType(potionEffectType);
			}
			else if (i == 1)
			{
				Integer amplifier = (Integer) part;
				ret.setAmplifier(amplifier);
			}
			else if (i == 2)
			{
				Integer duration = (Integer) part;
				ret.setDuration(duration);
			}
			else if (i == 3)
			{
				Boolean ambient = (Boolean) part;
				ret.setAmbient(ambient);
			}
			else if (i == 4)
			{
				Boolean particles = (Boolean) part;
				ret.setParticles(particles);
			}
		}
		
		// Return
		return ret;
	}
	
}
