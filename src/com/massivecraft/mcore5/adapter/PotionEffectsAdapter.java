package com.massivecraft.mcore5.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.potion.PotionEffect;

import com.massivecraft.mcore5.xlib.gson.JsonArray;
import com.massivecraft.mcore5.xlib.gson.JsonElement;

public class PotionEffectsAdapter
{
	// -------------------------------------------- //
	// TO JSON
	// -------------------------------------------- //
	
	public static JsonArray toJson(Collection<PotionEffect> potionEffects)
	{
		JsonArray ret = new JsonArray();
		for (PotionEffect pe : potionEffects)
		{
			ret.add(PotionEffectAdapter.toJson(pe));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// FROM JSON
	// -------------------------------------------- //
	
	public static List<PotionEffect> fromJson(JsonElement jsonElement)
	{
		if (jsonElement == null) return null;
		if ( ! jsonElement.isJsonArray()) return null;
		JsonArray array = jsonElement.getAsJsonArray();
		
		List<PotionEffect> ret = new ArrayList<PotionEffect>();
		
		Iterator<JsonElement> iter = array.iterator();
		while(iter.hasNext())
		{
			PotionEffect pe = PotionEffectAdapter.fromJson(iter.next());
			if (pe == null) continue;
			ret.add(pe);
		}
		
		return ret;
	}
	
}
