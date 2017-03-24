package com.massivecraft.massivecore;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.type.convert.TypeConverterPotionEffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

/**
 * This class wraps the Bukkit PotionEffect class by reimplementing storage of the data.
 * The purpose of this class is to allow for serialization using GSON.
 * You can not serialize the Bukkit PotionEffect due to some strange GSON bug.
 * Also we get the opportunity to add in some nice utility methods.
 */
public class PotionEffectWrap
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	@EditorType(TypeConverterPotionEffectType.class)
	protected int id;
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	@SuppressWarnings("deprecation")
	public void setPotionEffectType(PotionEffectType potionEffectType) { this.setId(potionEffectType.getId());}
	@SuppressWarnings("deprecation")
	public PotionEffectType getPotionEffectType() { return PotionEffectType.getById(this.getId()); }
	
	protected int amplifier;
	public int getAmplifier() { return this.amplifier; }
	public void setAmplifier(int amplifier) { this.amplifier = amplifier; }
	
	protected int duration;
	public int getDuration() { return this.duration; }
	public void setDuration(int duration) { this.duration = duration; }
	
	protected boolean ambient;
	public boolean isAmbient() { return this.ambient; }
	public void setAmbient(boolean ambient) { this.ambient = ambient; }
	
	// Since Minecraft 1.8
	protected boolean particles;
	public boolean isParticles() { return this.particles; }
	public void setParticles(boolean particles) { this.particles = particles; }
	// TODO: How to backwards compat?
	// TODO: For now we just don't support this 1.8 option...
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PotionEffectWrap(int id, int amplifier, int duration, boolean ambient, boolean particles)
	{
		this.id = id;
		this.amplifier = amplifier;
		this.duration = duration;
		this.ambient = ambient;
		this.particles = particles;
	}
	
	public PotionEffectWrap()
	{
		this.id = 0;
		this.amplifier = 0;
		this.duration = 0;
		this.ambient = false;
		this.particles = true;
	}
	
	// -------------------------------------------- //
	// FROM BUKKIT
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public static PotionEffectWrap valueOf(PotionEffect potionEffect)
	{
		return new PotionEffectWrap(potionEffect.getType().getId(), potionEffect.getAmplifier(), potionEffect.getDuration(), potionEffect.isAmbient(), true);
	}
	
	// -------------------------------------------- //
	// TO BUKKIT
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public PotionEffect asPotionEffect()
	{
		return new PotionEffect(PotionEffectType.getById(id), this.duration, this.amplifier, this.ambient);
	}
	
	public boolean addTo(LivingEntity entity)
	{
		return entity.addPotionEffect(this.asPotionEffect(), true);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static List<PotionEffectWrap> getEffects(LivingEntity entity)
	{
		// Create Ret
		List<PotionEffectWrap> ret = new MassiveList<>();
		
		// Fill Ret
		for (PotionEffect potionEffect : entity.getActivePotionEffects())
		{
			ret.add(PotionEffectWrap.valueOf(potionEffect));
		}
		
		// Return Ret
		return ret;
	}
	
	public static void removeEffects(LivingEntity entity)
	{
		// For each active potion effect ...
		for (PotionEffect potionEffect : entity.getActivePotionEffects())
		{
			// ... remove that type.
			entity.removePotionEffect(potionEffect.getType());
		}
	}
	
	public static void addEffects(LivingEntity entity, Iterable<? extends PotionEffectWrap> potionEffectWraps)
	{
		// For each supplied potion effect wrap ...
		for (PotionEffectWrap potionEffectWrap : potionEffectWraps)
		{
			// ... add it to the entity.
			potionEffectWrap.addTo(entity);
		}
	}
	
	public static void setEffects(LivingEntity entity, Collection<? extends PotionEffectWrap> potionEffectWraps)
	{
		// Remove ...
		removeEffects(entity);
		
		// ... then add.
		addEffects(entity, potionEffectWraps);
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	public String getListLine()
	{
		// Create Ret
		StringBuilder ret = new StringBuilder();
		
		// Type Name (ID)
		ret.append(this.getPotionEffectType().getName());
		ret.append(' ');
		
		// Amplifier
		ret.append(this.amplifier);
		ret.append(' ');
		
		// Duration
		ret.append(this.duration);
		ret.append(' ');
		
		// Ambient
		ret.append(this.ambient);
		ret.append(' ');
		
		// Particles
		ret.append(this.particles);
		
		// Return Ret
		return ret.toString();
	}
	
}
