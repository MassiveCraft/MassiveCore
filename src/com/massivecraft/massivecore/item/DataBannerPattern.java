package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.command.editor.annotation.EditorMethods;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.type.convert.TypeConverterBannerPatternType;
import com.massivecraft.massivecore.command.type.convert.TypeConverterDyeColor;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.block.banner.Pattern;

import java.util.Objects;

import static com.massivecraft.massivecore.item.DataItemStack.get;
import static com.massivecraft.massivecore.item.DataItemStack.set;

@EditorMethods(true)
public class DataBannerPattern implements Comparable<DataBannerPattern>
{
	// -------------------------------------------- //
	// DEFAULTS
	// -------------------------------------------- //
	
	public static final transient String DEFAULT_ID = null;
	public static final transient Integer DEFAULT_COLOR = null;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	@EditorType(TypeConverterBannerPatternType.class)
	private String id = null;
	public String getId() { return get(this.id, DEFAULT_ID); }
	public DataBannerPattern setId(String id) { this.id = set(id, DEFAULT_ID); return this; }
	
	@EditorType(TypeConverterDyeColor.class)
	private Integer color = null;
	public Integer getColor() { return get(this.color, DEFAULT_COLOR); }
	public DataBannerPattern setColor(Integer color) { this.color = set(color, DEFAULT_ID); return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DataBannerPattern()
	{
		
	}
	
	// Minecraft 1.7 Compatibility
	public DataBannerPattern(Object pattern)
	{
		if ( ! (pattern instanceof Pattern)) throw new IllegalArgumentException("pattern");
		this.write((Pattern)pattern, false);
	}
	
	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //
	
	// Minecraft 1.7 Compatibility
	public void write(Object pattern, boolean a2b)
	{
		if ( ! (pattern instanceof Pattern)) throw new IllegalArgumentException("pattern");
		WriterBannerPattern.get().write(this, (Pattern)pattern, a2b);
	}
	
	// -------------------------------------------- //
	// TO BUKKIT
	// -------------------------------------------- //
	
	// Minecraft 1.7 Compatibility
	@SuppressWarnings("unchecked")
	public <T> T toBukkit()
	{
		// Create
		Pattern ret = WriterBannerPattern.get().createOB();
		
		// Fill
		this.write(ret, true);
		
		// Return
		return (T) ret;
	}
	
	// -------------------------------------------- //
	// COMPARE & EQUALS & HASHCODE 
	// -------------------------------------------- //
	
	@Override
	public int compareTo(DataBannerPattern that)
	{
		return ComparatorSmart.get().compare(
			this.getId(), that.getId(),
			this.getColor(), that.getColor()
		);
	}
	
	// TODO: Use compare instead to avoid bugs?
	@Override
	public boolean equals(Object object)
	{
		if ( ! (object instanceof DataBannerPattern)) return false;
		DataBannerPattern that = (DataBannerPattern)object;
		
		return MUtil.equals(
			this.getId(), that.getId(),
			this.getColor(), that.getColor()
		);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.getId(),
			this.getColor()
		);
	}
	
}
