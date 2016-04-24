package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WriterPotionEffectId extends WriterAbstractPotionEffect<Integer, PotionEffectType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterPotionEffectId i = new WriterPotionEffectId();
	public static WriterPotionEffectId get() { return i; }
	public WriterPotionEffectId()
	{
		super("type");
		this.setConverterTo(ConverterToPotionEffectType.get());
		this.setConverterFrom(ConverterFromPotionEffectType.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public Integer getA(DataPotionEffect ca)
	{
		return ca.getId();
	}
	
	@Override
	public void setA(DataPotionEffect ca, Integer fa)
	{
		ca.setId(fa);
	}
	
	@Override
	public PotionEffectType getB(PotionEffect cb)
	{
		return cb.getType();
	}
	
}
