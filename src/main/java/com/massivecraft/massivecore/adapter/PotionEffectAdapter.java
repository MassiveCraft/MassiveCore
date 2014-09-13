package com.massivecraft.massivecore.adapter;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

@SuppressWarnings("deprecation")
public class PotionEffectAdapter
{
	// -------------------------------------------- //
	// FIELD CONSTANTS
	// -------------------------------------------- //
	
	public static final String POTION_EFFECT_ID = "id";
	public static final String POTION_DURATION = "duration";
	public static final String POTION_AMPLIFIER = "amplifier";
	public static final String POTION_AMBIENT = "ambient";
	
	public static final int POTION_DURATION_DEFAULT = 20*3*60;
	public static final int POTION_AMPLIFIER_DEFAULT = 0;
	public static final boolean POTION_AMBIENT_DEFAULT = false;
	
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
		
		int duration = POTION_DURATION_DEFAULT;
		JsonElement durationElement = json.get(POTION_DURATION);
		if (durationElement != null)
		{
			duration = durationElement.getAsInt();
		}
		
		int amplifier = POTION_AMPLIFIER_DEFAULT;
		JsonElement amplifierElement = json.get(POTION_AMPLIFIER);
		if (amplifierElement != null)
		{
			amplifier = amplifierElement.getAsInt();
		}
		
		boolean ambient = POTION_AMBIENT_DEFAULT;
		JsonElement ambientElement = json.get(POTION_AMBIENT);
		if (ambientElement != null)
		{
			ambient = ambientElement.getAsBoolean();
		}
		
		return new PotionEffect(pet, duration, amplifier, ambient);
	}
	
}
