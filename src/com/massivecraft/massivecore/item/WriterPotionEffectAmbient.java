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
	public Boolean getA(DataPotionEffect ca)
	{
		return ca.isAmbient();
	}
	
	@Override
	public void setA(DataPotionEffect ca, Boolean fa)
	{
		ca.setAmbient(fa);
	}
	
	@Override
	public Boolean getB(PotionEffect cb)
	{
		return cb.isAmbient();
	}
	
}
