package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;

public class WriterPotionEffectAmbient extends WriterAbstractPotionEffect<Boolean, Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterPotionEffectAmbient i = new WriterPotionEffectAmbient();
	public static WriterPotionEffectAmbient get() { return i; }
	public WriterPotionEffectAmbient()
	{
		super("ambient");
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public Boolean getA(DataPotionEffect ca, Object d)
	{
		return ca.isAmbient();
	}
	
	@Override
	public void setA(DataPotionEffect ca, Boolean fa, Object d)
	{
		ca.setAmbient(fa);
	}
	
	@Override
	public Boolean getB(PotionEffect cb, Object d)
	{
		return cb.isAmbient();
	}
	
}
