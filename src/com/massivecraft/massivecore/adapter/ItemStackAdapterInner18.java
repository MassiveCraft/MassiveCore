package com.massivecraft.massivecore.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
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
public class ItemStackAdapterInner18 extends ItemStackAdapterInner17
{
	// -------------------------------------------- //
	// CONSTANTS: NAMES
	// -------------------------------------------- //
	
	public static final String UNBREAKABLE = "unbreakable";
	
	public static final String ITEM_FLAGS = "flags";
	
	public static final String BANNER_BASE = "banner-base";
	public static final String BANNER_PATTERNS = "banner";
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static ItemStackAdapterInner18 i = new ItemStackAdapterInner18();
	public static ItemStackAdapterInner18 get() { return i; }

	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	
	@Override
	public Object provoke()
	{
		ItemStack stack = new ItemStack(Material.STONE);
		ItemMeta meta = stack.getItemMeta();
		return meta.spigot().isUnbreakable();
	}
	
	// -------------------------------------------- //
	// UNSPECIFIC META
	// -------------------------------------------- //

	@Override
	public void transferMetaUnspecific(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		super.transferMetaUnspecific(stack, json, meta2json, meta);
		this.transferUnbreakable(stack, json, meta2json, meta);
		this.transferItemFlags(stack, json, meta2json, meta);
	}

	// -------------------------------------------- //
	// UNSPECIFIC META: UNBREAKABLE
	// -------------------------------------------- //

	public void transferUnbreakable(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
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

	public void transferItemFlags(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
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
	public void transferMetaSpecific(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		if (meta instanceof BannerMeta)
		{
			this.transferBanner(stack, json, meta2json, (BannerMeta)meta);
		}
		else
		{
			super.transferMetaSpecific(stack, json, meta2json, meta);
		}
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: SKULL
	// -------------------------------------------- //
	// When we serialize we store the minimal information possible.
	// We then deserialize using cached information.
	// This is done to avoid sync bouncing.
	// Different servers might serialize different heads differently.
	
	@Override
	public void transferSkull(ItemStack stack, JsonObject json, boolean meta2json, SkullMeta meta)
	{
		if (meta2json)
		{
			if ( ! meta.hasOwner()) return;
			String name = meta.getOwner();
			if (name == null) return;
			name = name.toLowerCase();
			json.addProperty(SKULL_OWNER, name);
		}
		else
		{
			JsonElement element = json.get(SKULL_OWNER);
			if (element == null) return;
			
			String name = element.getAsString();
			UUID id = null;
			
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

	public void transferBanner(ItemStack stack, JsonObject json, boolean meta2json, Object meta)
	{
		this.transferBannerBase(stack, json, meta2json, meta);
		this.transferBannerPatterns(stack, json, meta2json, meta);
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: BANNER BASE
	// -------------------------------------------- //
	
	public void transferBannerBase(ItemStack stack, JsonObject json, boolean meta2json, Object meta)
	{
		if (meta2json)
		{
			DyeColor baseColor = null;
			if (meta instanceof BannerMeta) baseColor = ((BannerMeta)meta).getBaseColor();
			if (meta instanceof Banner) baseColor = ((Banner)meta).getBaseColor();
			
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
			
			if (meta instanceof BannerMeta) ((BannerMeta)meta).setBaseColor(baseColor);
			if (meta instanceof Banner) ((Banner)meta).setBaseColor(baseColor);
		}
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: BANNER PATTERNS
	// -------------------------------------------- //
	
	public void transferBannerPatterns(ItemStack stack, JsonObject json, boolean meta2json, Object meta)
	{
		if (meta2json)
		{
			List<Pattern> patterns = null;
			if (meta instanceof BannerMeta) patterns = ((BannerMeta)meta).getPatterns();
			if (meta instanceof Banner) patterns = ((Banner)meta).getPatterns();
			
			JsonArray data = convertBannerPatterns(patterns);
			if (data == null) return;
			json.add(BANNER_PATTERNS, data);
		}
		else
		{
			JsonElement element = json.get(BANNER_PATTERNS);
			if (element == null) return;
			List<Pattern> patterns = convertBannerPatterns(element);
			
			if (meta instanceof BannerMeta) ((BannerMeta)meta).setPatterns(patterns);
			if (meta instanceof Banner) ((Banner)meta).setPatterns(patterns);
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
