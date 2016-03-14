package com.massivecraft.massivecore.adapter;

import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

@SuppressWarnings("deprecation")
public class ItemStackAdapterInner19 extends ItemStackAdapterInner18
{
	// -------------------------------------------- //
	// CONSTANTS: POTION
	// -------------------------------------------- //

	public static final String POTION = "potion";
	public static final PotionData POTION_DEFAULT = new PotionData(PotionType.WATER, false, false);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static ItemStackAdapterInner19 i = new ItemStackAdapterInner19();
	public static ItemStackAdapterInner19 get() { return i; }

	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	
	@Override
	public Object provoke()
	{
		return PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT;
	}
	
	// -------------------------------------------- //
	// SPECIFIC META (SHIELD)
	// -------------------------------------------- //

	@Override
	public void transferMetaSpecific(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		if (stack.getType() == Material.SHIELD)
		{
			this.transferShield(stack, json, meta2json, (BlockStateMeta)meta);
		}
		else
		{
			super.transferMetaSpecific(stack, json, meta2json, meta);
		}
	}
	
	public void transferShield(ItemStack stack, JsonObject json, boolean meta2json, BlockStateMeta meta)
	{
		BlockState state = (Banner) meta.getBlockState();
		this.transferBanner(stack, json, meta2json, state);
		
		if (meta2json)
		{
			// TODO: This direction seems to work fine.
			// TODO: Serialization seems to work fine.
		}
		else
		{
			// TODO: This does not seem to work.
			// TODO: Is it because an inferior ItemStack vs CraftItemStack implementation?
			meta.setBlockState(state);
		}
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: POTION
	// -------------------------------------------- //
	// In Minecraft 1.8 the damage value was used to store the potion effects.
	// In Minecraft 1.9 the damage value is no longer used and the potion effect is stored by string instead.
	// 
	// Sticking to the damage value for serialization is not feasible.
	// Minecraft 1.9 adds new potion effects that did not exist in Minecraft 1.9 such as LUCK.
	//
	// Thus we upgrade the database from damage values to the new potion string where possible.
	// Invalid old damage values that does not make any sense are left as is.
	
	@Override
	public void transferPotion(ItemStack stack, JsonObject json, boolean meta2json, PotionMeta meta)
	{
		super.transferPotion(stack, json, meta2json, meta);
		
		if (meta2json)
		{
			// Check Null and Default
			PotionData potionData = meta.getBasePotionData();
			if (potionData != null && ! potionData.equals(POTION_DEFAULT))
			{
				// Check Null (silent on failure)
				String potionString = toPotionString(potionData);
				if (potionString != null)
				{
					json.addProperty(POTION, potionString);
				}
			}
		}
		else
		{
			// Create
			PotionData target = null;
			
			// Get by "potion"
			JsonElement potionElement = json.get(POTION);
			if (potionElement != null)
			{
				String potionString = potionElement.getAsString();
				PotionData potionData = toPotionData(potionString); 
				if (potionData != null)
				{
					target = potionData;
				}
			}
			
			// Get by "damage"
			if (target == null)
			{
				JsonElement damageElement = json.get(DAMAGE);
				if (damageElement != null)
				{
					int damage = damageElement.getAsInt();
					PotionData potionData = toPotionData(damage);
					if (potionData != null)
					{
						stack.setDurability((short) 0);
						target = potionData;
					}
				}
			}
			
			// Get by POTION_DEFAULT
			if (target == null)
			{
				target = POTION_DEFAULT;
			}
			
			// Set
			meta.setBasePotionData(target);
		}
	}

	// -------------------------------------------- //
	// POTION UTIL
	// -------------------------------------------- //

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
