package com.massivecraft.mcore5.adapter;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.mcore5.xlib.gson.JsonElement;
import com.massivecraft.mcore5.xlib.gson.JsonObject;

public class PotionEffectAdapter
{
	// -------------------------------------------- //
	// FIELD CONSTANTS
	// -------------------------------------------- //
	
	public static final String POTION_EFFECT_ID = "id";
	public static final String POTION_DURATION = "duration";
	public static final String POTION_AMPLIFIER = "amplifier";
	public static final String POTION_AMBIENT = "ambient";
	
	// -------------------------------------------- //
	// TO JSON
	// -------------------------------------------- //
	
	public static JsonObject toJson(PotionEffect potionEffect)
	{
		if (potionEffect == null) return null;
		
		JsonObject ret = new JsonObject();
		
		ret.addProperty(POTION_EFFECT_ID, potionEffect.getType().getId());
		ret.addProperty(POTION_DURATION, potionEffect.getDuration());
		ret.addProperty(POTION_AMPLIFIER, potionEffect.getAmplifier());
		ret.addProperty(POTION_AMBIENT, potionEffect.isAmbient());
		
		return ret;
	}
	
	// -------------------------------------------- //
	// FROM JSON
	// -------------------------------------------- //
	
	public static PotionEffect fromJson(JsonElement jsonElement)
	{
		if (jsonElement == null) return null;
		if ( ! jsonElement.isJsonObject()) return null;
		
		JsonObject json = jsonElement.getAsJsonObject();
		
		PotionEffectType pet = PotionEffectType.getById(json.get(POTION_EFFECT_ID).getAsInt());
		int duration = json.get(POTION_DURATION).getAsInt();
		int amplifier = json.get(POTION_AMPLIFIER).getAsInt();
		boolean ambient = json.get(POTION_AMBIENT).getAsBoolean();
		
		return new PotionEffect(pet, duration, amplifier, ambient);
	}
}
