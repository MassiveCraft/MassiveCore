package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.command.editor.annotation.EditorMethods;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.type.convert.TypeConverterColor;
import com.massivecraft.massivecore.command.type.convert.TypeConverterPotionEffectType;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

import static com.massivecraft.massivecore.item.DataItemStack.get;
import static com.massivecraft.massivecore.item.DataItemStack.set;

@EditorMethods(true)
public class DataPotionEffect implements Comparable<DataPotionEffect>
{
	// -------------------------------------------- //
	// DEFAULTS
	// -------------------------------------------- //
	
	public static final transient Integer DEFAULT_ID = null;
	public static final transient Integer DEFAULT_DURATION = 20 * 3 * 60;
	public static final transient Integer DEFAULT_AMPLIFIER = 0;
	public static final transient Boolean DEFAULT_AMBIENT = false;
	public static final transient Boolean DEFAULT_PARTICLES = true;
	public static final transient Integer DEFAULT_COLOR = null;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	@EditorType(value = TypeConverterPotionEffectType.class)
	private Integer id = null;
	public Integer getId() { return get(this.id, DEFAULT_ID); }
	public DataPotionEffect setId(Integer id) { this.id = set(id, DEFAULT_ID); return this; }
	
	private Integer duration = null;
	public int getDuration() { return get(this.duration, DEFAULT_DURATION); }
	public DataPotionEffect setDuration(int duration) { this.duration = set(duration, DEFAULT_DURATION); return this; }
	
	private Integer amplifier = null;
	public int getAmplifier() { return get(this.amplifier, DEFAULT_AMPLIFIER); }
	public DataPotionEffect setAmplifier(int amplifier) { this.amplifier = set(amplifier, DEFAULT_AMPLIFIER); return this; }
	
	private Boolean ambient = null; 
	public boolean isAmbient() { return get(this.ambient, DEFAULT_AMBIENT); }
	public DataPotionEffect setAmbient(boolean ambient) { this.ambient = set(ambient, DEFAULT_AMBIENT); return this; }
	
	// SINCE: 1.8
	private Boolean particles = null;
	public boolean isParticles() { return get(this.particles, DEFAULT_PARTICLES); }
	public DataPotionEffect setParticles(boolean particles) { this.particles = set(particles, DEFAULT_PARTICLES); return this; }
	
	// SINCE: 1.9
	@EditorType(TypeConverterColor.class)
	private Integer color = null;
	public Integer getColor() { return get(this.color, DEFAULT_COLOR); }
	public DataPotionEffect setColor(Integer color) { this.color = set(color, DEFAULT_COLOR); return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DataPotionEffect()
	{
		
	}
	
	public DataPotionEffect(PotionEffect potionEffect)
	{
		this.write(potionEffect, false);
	}
	
	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //
	
	public void write(PotionEffect potionEffect, boolean a2b)
	{
		WriterPotionEffect.get().write(this, potionEffect, a2b);
	}
	
	// -------------------------------------------- //
	// TO BUKKIT
	// -------------------------------------------- //
	
	public PotionEffect toBukkit()
	{
		// Create
		PotionEffect ret = WriterPotionEffect.get().createOB();
		
		// Fill
		this.write(ret, true);
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// COMPARE & EQUALS & HASHCODE 
	// -------------------------------------------- //
	
	@Override
	public int compareTo(DataPotionEffect that)
	{
		return ComparatorSmart.get().compare(
			this.getId(), that.getId(),
			this.getDuration(), that.getDuration(),
			this.getAmplifier(), that.getAmplifier(),
			this.isAmbient(), that.isAmbient(),
			this.isParticles(), that.isParticles(),
			this.getColor(), that.getColor()
		);
	}
	
	// TODO: Use compare instead to avoid bugs?
	@Override
	public boolean equals(Object object)
	{
		if ( ! (object instanceof DataPotionEffect)) return false;
		DataPotionEffect that = (DataPotionEffect)object;
		
		return MUtil.equals(
			this.getId(), that.getId(),
			this.getDuration(), that.getDuration(),
			this.getAmplifier(), that.getAmplifier(),
			this.isAmbient(), that.isAmbient(),
			this.isParticles(), that.isParticles(),
			this.getColor(), that.getColor()
		);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.getId(),
			this.getDuration(),
			this.getAmplifier(),
			this.isAmbient(),
			this.isParticles(),
			this.getColor()
		);
	}
	
}
