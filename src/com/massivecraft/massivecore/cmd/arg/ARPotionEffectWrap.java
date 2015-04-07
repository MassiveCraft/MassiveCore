package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.PotionEffectWrap;

public class ARPotionEffectWrap extends ARAbstract<PotionEffectWrap>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARPotionEffectWrap i = new ARPotionEffectWrap();
	public static ARPotionEffectWrap get() { return i; }

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final ARCombined combined = new ARCombined(
		ARPotionEffectType.get(),
		ARInteger.get(),
		ARInteger.get(),
		ARBoolean.get(),
		ARBoolean.get()
	);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PotionEffectWrap read(String arg, CommandSender sender) throws MassiveException
	{
		// Create Ret
		PotionEffectWrap ret = new PotionEffectWrap();
		
		// Fill Ret
		List<?> parts = combined.read(arg, sender);
		
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
		
		// Return Ret
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return ARPotionEffectType.get().getTabList(sender, arg);
	}
	
}
