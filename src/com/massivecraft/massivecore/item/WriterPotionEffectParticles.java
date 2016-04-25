package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;

public class WriterPotionEffectParticles extends WriterAbstractPotionEffect<Boolean, Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterPotionEffectParticles i = new WriterPotionEffectParticles();
	public static WriterPotionEffectParticles get() { return i; }
	public WriterPotionEffectParticles()
	{
		super("particles");
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public Boolean getA(DataPotionEffect ca, Object d)
	{
		return ca.isParticles();
	}
	
	@Override
	public void setA(DataPotionEffect ca, Boolean fa, Object d)
	{
		ca.setParticles(fa);
	}
	
	@Override
	public Boolean getB(PotionEffect cb, Object d)
	{
		return cb.hasParticles();
	}
	
}
