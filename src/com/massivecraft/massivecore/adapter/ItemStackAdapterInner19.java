package com.massivecraft.massivecore.adapter;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class ItemStackAdapterInner19 extends ItemStackAdapterInner18
{
	// -------------------------------------------- //
	// CONSTANTS: POTION
	// -------------------------------------------- //

	public static final String POTION = "potion";
	public static final PotionData DATA_NULL = new PotionData(PotionType.UNCRAFTABLE, false, false);
	
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
	// SPECIFIC META: POTION
	// -------------------------------------------- //
	
	@Override @SuppressWarnings("deprecation")
	public void transferPotion(ItemStack stack, JsonObject json, boolean meta2json, PotionMeta meta)
	{
		super.transferPotion(stack, json, meta2json, meta);

		// Note: We serialize/deserialize PotionData if available, otherwise fall back to damage values
		// NOTE: Damage values are NOT safe and thus should not be used to serialize data in 1.9.
		if (meta2json)
		{
			PotionData data = meta.getBasePotionData();
			json.addProperty(POTION, fromBukkit(data));
		}
		else
		{
			JsonElement element = json.get(POTION);
			if (element != null)
			{
				meta.setBasePotionData(toBukkit(element.getAsString()));
				return;
			}

			// NOTE: PotionData is initialized with UNCRAFTABLE. If is freshly initialized, try from damage values.
			PotionData data = meta.getBasePotionData();
			if ( ! data.equals(DATA_NULL)) return;

			int damage = DEFAULT_DAMAGE;
			JsonElement damageElement = json.get(DAMAGE);
			if (damageElement != null) damage = damageElement.getAsInt();
			Potion.fromDamage(damage).apply(stack);
		}
	}

	// -------------------------------------------- //
	// CRAFT POTION UTIL
	// -------------------------------------------- //

	public static String fromBukkit(PotionData data)
	{
		if (data.isUpgraded()) return upgradeable.get(data.getType());
		if (data.isExtended()) return extendable.get(data.getType());
		return regular.get(data.getType());
	}

	public static PotionData toBukkit(String type)
	{
		PotionType potionType = null;

		// Extended
		potionType = extendable.inverse().get(type);
		if (potionType != null) return new PotionData(potionType, true, false);

		// Upgraded
		potionType = upgradeable.inverse().get(type);
		if (potionType != null) return new PotionData(potionType, false, true);

		// Regular
		return new PotionData(regular.inverse().get(type), false, false);
	}

	private static final BiMap<PotionType, String> regular = ImmutableBiMap.<PotionType, String>builder()
		.put(PotionType.UNCRAFTABLE, "empty")
		.put(PotionType.WATER, "water")
		.put(PotionType.MUNDANE, "mundane")
		.put(PotionType.THICK, "thick")
		.put(PotionType.AWKWARD, "awkward")
		.put(PotionType.NIGHT_VISION, "night_vision")
		.put(PotionType.INVISIBILITY, "invisibility")
		.put(PotionType.JUMP, "leaping")
		.put(PotionType.FIRE_RESISTANCE, "fire_resistance")
		.put(PotionType.SPEED, "swiftness")
		.put(PotionType.SLOWNESS, "slowness")
		.put(PotionType.WATER_BREATHING, "water_breathing")
		.put(PotionType.INSTANT_HEAL, "healing")
		.put(PotionType.INSTANT_DAMAGE, "harming")
		.put(PotionType.POISON, "poison")
		.put(PotionType.REGEN, "regeneration")
		.put(PotionType.STRENGTH, "strength")
		.put(PotionType.WEAKNESS, "weakness")
		.put(PotionType.LUCK, "luck")
		.build();

	private static final BiMap<PotionType, String> upgradeable = ImmutableBiMap.<PotionType, String>builder()
		.put(PotionType.JUMP, "strong_leaping")
		.put(PotionType.SPEED, "strong_swiftness")
		.put(PotionType.INSTANT_HEAL, "strong_healing")
		.put(PotionType.INSTANT_DAMAGE, "strong_harming")
		.put(PotionType.POISON, "strong_poison")
		.put(PotionType.REGEN, "strong_regeneration")
		.put(PotionType.STRENGTH, "strong_strength")
		.build();

	private static final BiMap<PotionType, String> extendable = ImmutableBiMap.<PotionType, String>builder()
		.put(PotionType.NIGHT_VISION, "long_night_vision")
		.put(PotionType.INVISIBILITY, "long_invisibility")
		.put(PotionType.JUMP, "long_leaping")
		.put(PotionType.FIRE_RESISTANCE, "long_fire_resistance")
		.put(PotionType.SPEED, "long_swiftness")
		.put(PotionType.SLOWNESS, "long_slowness")
		.put(PotionType.WATER_BREATHING, "long_water_breathing")
		.put(PotionType.POISON, "long_poison")
		.put(PotionType.REGEN, "long_regeneration")
		.put(PotionType.STRENGTH, "long_strength")
		.put(PotionType.WEAKNESS, "long_weakness")
		.build();

}
