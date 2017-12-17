package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.xlib.guava.collect.BiMap;
import com.massivecraft.massivecore.xlib.guava.collect.ImmutableBiMap;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

@SuppressWarnings("deprecation")
public class PotionUtil
{
	public static PotionData toPotionData(int damage)
	{
		try
		{
			Potion potion = Potion.fromDamage(damage);
			PotionType type = potion.getType();
			boolean extended = potion.hasExtendedDuration();
			boolean upgraded = (potion.getLevel() >= 2);
			
			// This section serves two purposes:
			// 1. Avoid slow exceptions over for invalid damage values.
			// 2. Lenient upgrade to Minecraft 1.9. Keep what we can.
			// If a potion was both upgraded and extended we keep the upgraded and remove the extended.
			if (type == null) return null;
			if (extended && ! type.isExtendable()) return null;
			if (upgraded && ! type.isUpgradeable()) return null;
			if (upgraded && extended) extended = false;
			
			return new PotionData(type, extended, upgraded);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static PotionData toPotionData(String potionString)
	{
		if (potionString == null) return null;
		return POTION_ID_TO_DATA.get(potionString);
	}
	
	public static String toPotionString(PotionData potionData)
	{
		if (potionData == null) return null;
		return POTION_ID_TO_DATA.inverse().get(potionData);
	}
	
	private static final BiMap<String, PotionData> POTION_ID_TO_DATA = ImmutableBiMap.<String, PotionData>builder()
		// REGULAR
		.put("empty", new PotionData(PotionType.UNCRAFTABLE, false, false))
		.put("water", new PotionData(PotionType.WATER, false, false))
		.put("mundane", new PotionData(PotionType.MUNDANE, false, false))
		.put("thick", new PotionData(PotionType.THICK, false, false))
		.put("awkward", new PotionData(PotionType.AWKWARD, false, false))
		.put("night_vision", new PotionData(PotionType.NIGHT_VISION, false, false))
		.put("invisibility", new PotionData(PotionType.INVISIBILITY, false, false))
		.put("leaping", new PotionData(PotionType.JUMP, false, false))
		.put("fire_resistance", new PotionData(PotionType.FIRE_RESISTANCE, false, false))
		.put("swiftness", new PotionData(PotionType.SPEED, false, false))
		.put("slowness", new PotionData(PotionType.SLOWNESS, false, false))
		.put("water_breathing", new PotionData(PotionType.WATER_BREATHING, false, false))
		.put("healing", new PotionData(PotionType.INSTANT_HEAL, false, false))
		.put("harming", new PotionData(PotionType.INSTANT_DAMAGE, false, false))
		.put("poison", new PotionData(PotionType.POISON, false, false))
		.put("regeneration", new PotionData(PotionType.REGEN, false, false))
		.put("strength", new PotionData(PotionType.STRENGTH, false, false))
		.put("weakness", new PotionData(PotionType.WEAKNESS, false, false))
		.put("luck", new PotionData(PotionType.LUCK, false, false))
		
		// UPGRADABLE
		.put("strong_leaping", new PotionData(PotionType.JUMP, false, true))
		.put("strong_swiftness", new PotionData(PotionType.SPEED, false, true))
		.put("strong_healing", new PotionData(PotionType.INSTANT_HEAL, false, true))
		.put("strong_harming", new PotionData(PotionType.INSTANT_DAMAGE, false, true))
		.put("strong_poison", new PotionData(PotionType.POISON, false, true))
		.put("strong_regeneration", new PotionData(PotionType.REGEN, false, true))
		.put("strong_strength", new PotionData(PotionType.STRENGTH, false, true))
		
		// EXTENDABLE
		.put("long_night_vision", new PotionData(PotionType.NIGHT_VISION, true, false))
		.put("long_invisibility", new PotionData(PotionType.INVISIBILITY, true, false))
		.put("long_leaping", new PotionData(PotionType.JUMP, true, false))
		.put("long_fire_resistance", new PotionData(PotionType.FIRE_RESISTANCE, true, false))
		.put("long_swiftness", new PotionData(PotionType.SPEED, true, false))
		.put("long_slowness", new PotionData(PotionType.SLOWNESS, true, false))
		.put("long_water_breathing", new PotionData(PotionType.WATER_BREATHING, true, false))
		.put("long_poison", new PotionData(PotionType.POISON, true, false))
		.put("long_regeneration", new PotionData(PotionType.REGEN, true, false))
		.put("long_strength", new PotionData(PotionType.STRENGTH, true, false))
		.put("long_weakness", new PotionData(PotionType.WEAKNESS, true, false))
	
	// BUILD
	.build();
	
}
