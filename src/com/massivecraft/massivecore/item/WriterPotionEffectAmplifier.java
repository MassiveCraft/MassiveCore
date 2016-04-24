package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;

public class WriterPotionEffectAmplifier extends WriterAbstractPotionEffect<Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterPotionEffectAmplifier i = new WriterPotionEffectAmplifier();
	public static WriterPotionEffectAmplifier get() { return i; }
	public WriterPotionEffectAmplifier()
	{
		super("amplifier");
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public Integer getA(DataPotionEffect ca)
	{
		return ca.getAmplifier();
	}
	
	@Override
	public void setA(DataPotionEffect ca, Integer fa)
	{
		ca.setAmplifier(fa);
	}
	
	@Override
	public Integer getB(PotionEffect cb)
	{
		return cb.getAmplifier();
	}
	
}
