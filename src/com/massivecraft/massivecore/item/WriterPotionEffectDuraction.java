package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;

public class WriterPotionEffectDuraction extends WriterAbstractPotionEffect<Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterPotionEffectDuraction i = new WriterPotionEffectDuraction();
	public static WriterPotionEffectDuraction get() { return i; }
	public WriterPotionEffectDuraction()
	{
		super("duration");
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public Integer getA(DataPotionEffect ca, Object d)
	{
		return ca.getDuration();
	}
	
	@Override
	public void setA(DataPotionEffect ca, Integer fa, Object d)
	{
		ca.setDuration(fa);
	}
	
	@Override
	public Integer getB(PotionEffect cb, Object d)
	{
		return cb.getDuration();
	}
	
}
