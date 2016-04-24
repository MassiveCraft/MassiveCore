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
	public Boolean getA(DataPotionEffect ca)
	{
		return ca.isParticles();
	}
	
	@Override
	public void setA(DataPotionEffect ca, Boolean fa)
	{
		ca.setParticles(fa);
	}
	
	@Override
	public Boolean getB(PotionEffect cb)
	{
		return cb.hasParticles();
	}
	
}
