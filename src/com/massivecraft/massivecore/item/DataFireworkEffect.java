package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.collections.MassiveListDef;
import com.massivecraft.massivecore.command.editor.annotation.EditorMethods;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.editor.annotation.EditorTypeInner;
import com.massivecraft.massivecore.command.type.convert.TypeConverterColor;
import com.massivecraft.massivecore.command.type.convert.TypeConverterFireworkEffectType;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.annotations.SerializedName;
import org.bukkit.FireworkEffect;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.massivecraft.massivecore.item.DataItemStack.get;
import static com.massivecraft.massivecore.item.DataItemStack.set;

@EditorMethods(true)
public class DataFireworkEffect implements Comparable<DataFireworkEffect>
{
	// -------------------------------------------- //
	// DEFAULTS
	// -------------------------------------------- //
	
	public static final transient Boolean DEFAULT_FLICKER = false;
	public static final transient Boolean DEFAULT_TRAIL = false;
	public static final transient List<Integer> DEFAULT_COLORS = Collections.emptyList();
	public static final transient List<Integer> DEFAULT_FADE_COLORS = Collections.emptyList();
	public static final transient String DEFAULT_TYPE = "BALL_LARGE";
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	// According to Bukkit the colors are indeed lists with explicit order.
	// I have not researched if that is true. For now I am assuming it is.
	
	private Boolean flicker = null;
	public boolean hasFlicker() { return get(this.flicker, DEFAULT_FLICKER); }
	public DataFireworkEffect setFlicker(boolean flicker) { this.flicker = set(flicker, DEFAULT_FLICKER); return this; }
	
	private Boolean trail = null;
	public boolean hasTrail() { return get(this.trail, DEFAULT_TRAIL); }
	public DataFireworkEffect setTrail(boolean trail) { this.trail = set(trail, DEFAULT_TRAIL); return this; }
	
	@EditorTypeInner(TypeConverterColor.class)
	private MassiveListDef<Integer> colors = null;
	public List<Integer> getColors() { return get(this.colors, DEFAULT_COLORS); }
	public DataFireworkEffect setColors(List<Integer> colors) { this.colors = set(colors, DEFAULT_COLORS); return this; }
	
	@EditorTypeInner(TypeConverterColor.class)
	@SerializedName("fade-colors")
	private MassiveListDef<Integer> fadeColors = null;
	public List<Integer> getFadeColors() { return get(this.fadeColors, DEFAULT_FADE_COLORS); }
	public DataFireworkEffect setFadeColors(List<Integer> fadeColors) { this.fadeColors = set(fadeColors, DEFAULT_FADE_COLORS); return this; }
	
	@EditorType(TypeConverterFireworkEffectType.class)
	private String type = null;
	public String getType() { return get(this.type, DEFAULT_TYPE); }
	public DataFireworkEffect setType(String type) { this.type = set(type, DEFAULT_TYPE); return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DataFireworkEffect()
	{
		
	}
	
	public DataFireworkEffect(FireworkEffect fireworkEffect)
	{
		this.write(fireworkEffect, false);
	}
	
	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //
	
	public void write(FireworkEffect fireworkEffect, boolean a2b)
	{
		WriterFireworkEffect.get().write(this, fireworkEffect, a2b);
	}
	
	// -------------------------------------------- //
	// TO BUKKIT
	// -------------------------------------------- //
	
	public FireworkEffect toBukkit()
	{
		// Create
		FireworkEffect ret = WriterFireworkEffect.get().createOB();
		
		// Fill
		this.write(ret, true);
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// COMPARE & EQUALS & HASHCODE 
	// -------------------------------------------- //
	
	@Override
	public int compareTo(DataFireworkEffect that)
	{
		return ComparatorSmart.get().compare(
			this.hasFlicker(), that.hasFlicker(),
			this.hasTrail(), that.hasTrail(),
			this.getColors(), that.getColors(),
			this.getColors(), that.getColors(),
			this.getFadeColors(), that.getFadeColors(),
			this.getType(), that.getType()
		);
	}
	
	// TODO: Use compare instead to avoid bugs?
	@Override
	public boolean equals(Object object)
	{
		if ( ! (object instanceof DataFireworkEffect)) return false;
		DataFireworkEffect that = (DataFireworkEffect)object;
		
		return MUtil.equals(
			this.hasFlicker(), that.hasFlicker(),
			this.hasTrail(), that.hasTrail(),
			this.getColors(), that.getColors(),
			this.getColors(), that.getColors(),
			this.getFadeColors(), that.getFadeColors(),
			this.getType(), that.getType()
		);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.hasFlicker(),
			this.hasTrail(),
			this.getColors(),
			this.getColors(),
			this.getFadeColors(),
			this.getType()
		);
	}
	
}
