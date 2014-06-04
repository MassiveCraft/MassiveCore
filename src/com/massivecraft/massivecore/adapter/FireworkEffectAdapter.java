package com.massivecraft.massivecore.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;

public class FireworkEffectAdapter
{
	// -------------------------------------------- //
	// FIELD CONSTANTS
	// -------------------------------------------- //
	
	public static final String FLICKER = "flicker";
	public static final String TRAIL = "trail";
	public static final String COLORS = "colors";
	public static final String FADE_COLORS = "fade-colors";
	public static final String TYPE = "type";
	
	public static final boolean FLICKER_DEFAULT = false;
	public static final boolean TRAIL_DEFAULT = false;
	public static final List<Color> COLORS_DEFAULT = Collections.unmodifiableList(MUtil.list(Color.GREEN));
	public static final List<Color> FADE_COLORS_DEFAULT = Collections.unmodifiableList(new ArrayList<Color>());
	public static final Type TYPE_DEFAULT = Type.BALL_LARGE;
	
	// -------------------------------------------- //
	// TO JSON
	// -------------------------------------------- //
	
	public static JsonObject toJson(FireworkEffect fireworkEffect)
	{
		if (fireworkEffect == null) return null;
		
		JsonObject ret = new JsonObject();
		
		ret.addProperty(FLICKER, fireworkEffect.hasFlicker());
		ret.addProperty(TRAIL, fireworkEffect.hasTrail());
		ret.add(COLORS, fromColorCollection(fireworkEffect.getColors()));
		ret.add(FADE_COLORS, fromColorCollection(fireworkEffect.getFadeColors()));
		ret.addProperty(TYPE, fireworkEffect.getType().name());
		
		return ret;
	}
	
	// -------------------------------------------- //
	// FROM JSON
	// -------------------------------------------- //
	
	public static FireworkEffect fromJson(JsonElement jsonElement)
	{
		if (jsonElement == null) return null;
		if ( ! jsonElement.isJsonObject()) return null;
		
		JsonObject json = jsonElement.getAsJsonObject();
		
		boolean flicker = FLICKER_DEFAULT;
		boolean trail = TRAIL_DEFAULT;
		List<Color> colors = COLORS_DEFAULT;
		List<Color> fadeColors = FADE_COLORS_DEFAULT;
		Type type = TYPE_DEFAULT;
		
		JsonElement element;
		
		element = json.get(FLICKER);
		if (element != null)
		{
			flicker = element.getAsBoolean();
		}
		
		element = json.get(TRAIL);
		if (element != null)
		{
			trail = element.getAsBoolean();
		}
		
		element = json.get(COLORS);
		if (element != null)
		{
			colors = toColorCollection(element);
		}
		
		element = json.get(FADE_COLORS);
		if (element != null)
		{
			fadeColors = toColorCollection(element);
		}
		
		element = json.get(TYPE);
		if (element != null)
		{
			type = Type.valueOf(element.getAsString());
		}
		
		FireworkEffect ret = FireworkEffect.builder()
		.flicker(flicker)
		.trail(trail)
		.withColor(colors)
		.withFade(fadeColors)
		.with(type)
		.build();
		
		return ret;
	}
	
	// -------------------------------------------- //
	// MINI UTILS
	// -------------------------------------------- //
	
	public static JsonArray fromColorCollection(Collection<Color> colors)
	{
		JsonArray ret = new JsonArray();
		for (Color color : colors)
		{
			ret.add(new JsonPrimitive(color.asRGB()));
		}
		return ret;
	}
	
	public static List<Color> toColorCollection(JsonElement json)
	{
		JsonArray array = json.getAsJsonArray();
		List<Color> ret = new ArrayList<Color>();
		
		Iterator<JsonElement> iter = array.iterator();
		while (iter.hasNext())
		{
			JsonElement element = iter.next();
			ret.add(Color.fromRGB(element.getAsInt()));
		}
		
		return ret;
	}
	
}
