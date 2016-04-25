package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class WriterAbstractPotionEffect<FA, FB> extends WriterAbstractReflect<DataPotionEffect, PotionEffect, DataPotionEffect, PotionEffect, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractPotionEffect(String fieldName)
	{
		super(PotionEffect.class, fieldName);
	}
	
	public WriterAbstractPotionEffect()
	{
		this(null); 
	}
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public DataPotionEffect createOA()
	{
		return new DataPotionEffect();
	}
	
	@Override
	public PotionEffect createOB()
	{
		return new PotionEffect(PotionEffectType.SPEED, 1, 1);
	}
	
}
