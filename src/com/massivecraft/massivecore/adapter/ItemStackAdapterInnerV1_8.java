package com.massivecraft.massivecore.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.massivecraft.massivecore.Couple;
import com.massivecraft.massivecore.nms.NmsHead;
import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;

@SuppressWarnings("deprecation")
public class ItemStackAdapterInnerV1_8 extends ItemStackAdapterInnerV1_7
{
	// -------------------------------------------- //
	// CONSTANTS: NAMES
	// -------------------------------------------- //
	
	public static final String UNBREAKABLE = "unbreakable";
	
	public static final String ITEM_FLAGS = "flags";
	
	public static final String SKULL_OWNER_ID = "skullid";
	
	public static final String BANNER_BASE = "banner-base";
	public static final String BANNER_PATTERNS = "banner";
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static ItemStackAdapterInnerV1_8 i = new ItemStackAdapterInnerV1_8();
	public static ItemStackAdapterInnerV1_8 get() { return i; }

	// -------------------------------------------- //
	// UNSPECIFIC META
	// -------------------------------------------- //

	@Override
	public void transferMetaUnspecific(ItemMeta meta, JsonObject json, boolean meta2json)
	{
		super.transferMetaUnspecific(meta, json, meta2json);
		this.transferUnbreakable(meta, json, meta2json);
		this.transferItemFlags(meta, json, meta2json);
	}

	// -------------------------------------------- //
	// UNSPECIFIC META: UNBREAKABLE
	// -------------------------------------------- //

	public void transferUnbreakable(ItemMeta meta, JsonObject json, boolean meta2json)
	{
		if (meta2json)
		{
			boolean unbreakable = meta.spigot().isUnbreakable();
			if ( ! unbreakable) return;
			json.addProperty(UNBREAKABLE, unbreakable);
		}
		else
		{
			JsonElement element = json.get(UNBREAKABLE);
			if (element == null) return;
			meta.spigot().setUnbreakable(element.getAsBoolean());
		}
	}
	
	// -------------------------------------------- //
	// UNSPECIFIC META: ITEM FLAGS
	// -------------------------------------------- //

	public void transferItemFlags(ItemMeta meta, JsonObject json, boolean meta2json)
	{
		if (meta2json)
		{
			JsonArray value = convertItemFlags(meta.getItemFlags());
			if (value == null) return;
			json.add(ITEM_FLAGS, value);
		}
		else
		{
			JsonElement element = json.get(ITEM_FLAGS);
			if (element == null) return;
			Set<ItemFlag> flags = convertItemFlags(element);
			meta.addItemFlags(flags.toArray(new ItemFlag[0]));
		}
	}
	
	public static JsonArray convertItemFlags(Set<ItemFlag> flags)
	{
		// Null
		if (flags == null) return null;
		if (flags.isEmpty()) return null;
		
		// Create Ret
		JsonArray ret = new JsonArray();
		
		// Fill Ret
		for (ItemFlag flag : flags)
		{
			ret.add(new JsonPrimitive(flag.name()));
		}
		
		// Return Ret
		return ret;
	}
	
	public static Set<ItemFlag> convertItemFlags(JsonElement jsonElement)
	{
		// Create Ret
		Set<ItemFlag> ret = new HashSet<ItemFlag>();
		
		// Fill Ret
		JsonArray json = jsonElement.getAsJsonArray();
		for (JsonElement element : json)
		{
			try
			{
				ItemFlag flag = ItemFlag.valueOf(element.getAsString());
				ret.add(flag);
			}
			catch (IllegalArgumentException ex)
			{
				// Ignore when we got a old String which does not map to a Enum value anymore.
			}
		}
		
		// Return Ret
		return ret;
	}
	
	// -------------------------------------------- //
	// SPECIFIC META
	// -------------------------------------------- //

	@Override
	public void transferMetaSpecific(ItemMeta meta, JsonObject json, boolean meta2json)
	{
		if (meta instanceof BannerMeta)
		{
			this.transferBanner((BannerMeta) meta, json, meta2json);
		}
		else
		{
			super.transferMetaSpecific(meta, json, meta2json);
		}
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: SKULL
	// -------------------------------------------- //

	@Override
	public void transferSkull(SkullMeta meta, JsonObject json, boolean meta2json)
	{
		if (meta2json)
		{
			if ( ! meta.hasOwner()) return;
			
			// Resolve to avoid MStore sync bouncing.
			Couple<String, UUID> resolved = NmsHead.resolve(meta);
			String name = resolved.getFirst();
			UUID id = resolved.getSecond();
			
			if (name != null) json.addProperty(SKULL_OWNER, name);
			if (id != null) json.addProperty(SKULL_OWNER_ID, id.toString());
		}
		else
		{
			JsonElement element;
			
			String name = null;
			element = json.get(SKULL_OWNER);
			if (element != null) name = element.getAsString();
			
			UUID id = null;
			element = json.get(SKULL_OWNER_ID);
			if (element != null) id = UUID.fromString(element.getAsString());
			
			// Resolve to avoid MStore sync bouncing.
			Couple<String, UUID> resolved = NmsHead.resolve(name, id);
			name = resolved.getFirst();
			id = resolved.getSecond();
			
			if (name != null || id != null)
			{
				NmsHead.set(meta, name, id);
			}
		}
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: BANNER
	// -------------------------------------------- //

	public void transferBanner(BannerMeta meta, JsonObject json, boolean meta2json)
	{
		this.transferBannerBase(meta, json, meta2json);
		this.transferBannerPatterns(meta, json, meta2json);
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: BANNER BASE
	// -------------------------------------------- //
	
	public void transferBannerBase(BannerMeta meta, JsonObject json, boolean meta2json)
	{
		if (meta2json)
		{
			DyeColor baseColor = meta.getBaseColor();
			
			// The default base color is null.
			// This occurs when no patterns are set.
			// In those cases the damage value of the item is used to denote color.
			if (baseColor == null) return;
			
			byte data = baseColor.getDyeData();
			json.addProperty(BANNER_BASE, data);
		}
		else
		{
			JsonElement element = json.get(BANNER_BASE);
			if (element == null) return;
			DyeColor baseColor = DyeColor.getByDyeData(element.getAsByte());
			meta.setBaseColor(baseColor);
		}
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: BANNER PATTERNS
	// -------------------------------------------- //
	
	public void transferBannerPatterns(BannerMeta meta, JsonObject json, boolean meta2json)
	{
		if (meta2json)
		{
			JsonArray data = convertBannerPatterns(meta.getPatterns());
			if (data == null) return;
			json.add(BANNER_PATTERNS, data);
		}
		else
		{
			JsonElement element = json.get(BANNER_PATTERNS);
			if (element == null) return;
			List<Pattern> patterns = convertBannerPatterns(element);
			meta.setPatterns(patterns);
		}
	}
	
	public static JsonArray convertBannerPatterns(List<Pattern> patterns)
	{
		// Null
		if (patterns == null) return null;
		if (patterns.isEmpty()) return null;
		
		// Create Ret
		JsonArray ret = new JsonArray();
		
		// Fill Ret
		for (Pattern pattern : patterns)
		{
			String i = pattern.getPattern().getIdentifier();
			Byte c = pattern.getColor().getDyeData();
			
			ret.add(new JsonPrimitive(i));
			ret.add(new JsonPrimitive(c));
		}
		
		// Return Ret
		return ret;
	}
	
	public static List<Pattern> convertBannerPatterns(JsonElement jsonElement)
	{
		// Create Ret
		List<Pattern> ret = new ArrayList<Pattern>();
		
		// Fill Ret
		JsonArray json = jsonElement.getAsJsonArray();
		Iterator<JsonElement> iter = json.iterator();
		while (iter.hasNext())
		{
			JsonElement ie = iter.next();
			if ( ! iter.hasNext()) break;
			JsonElement ce = iter.next();
			
			PatternType type = PatternType.getByIdentifier(ie.getAsString());
			DyeColor color = DyeColor.getByDyeData(ce.getAsByte());
			ret.add(new Pattern(color, type));
		}
		
		// Return Ret
		return ret;
	}
	
}
