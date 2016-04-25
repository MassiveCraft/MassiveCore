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
	public Integer getA(DataPotionEffect ca, Object d)
	{
		return ca.getId();
	}
	
	@Override
	public void setA(DataPotionEffect ca, Integer fa, Object d)
	{
		ca.setId(fa);
	}
	
	@Override
	public PotionEffectType getB(PotionEffect cb, Object d)
	{
		return cb.getType();
	}
	
}
