package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkEffectMeta;

public class WriterItemStackMetaFireworkEffect extends WriterAbstractItemMeta<FireworkEffectMeta, DataFireworkEffect, FireworkEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaFireworkEffect i = new WriterItemStackMetaFireworkEffect();
	public static WriterItemStackMetaFireworkEffect get() { return i; }
	{
		this.setMaterial(Material.FIREWORK_CHARGE);
		this.setConverterTo(ConverterToFireworkEffect.get());
		this.setConverterFrom(ConverterFromFireworkEffect.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public DataFireworkEffect getA(DataItemStack ca)
	{
		return ca.getFireworkEffect();
	}

	@Override
	public void setA(DataItemStack ca, DataFireworkEffect fa)
	{
		ca.setFireworkEffect(fa);
	}

	@Override
	public FireworkEffect getB(FireworkEffectMeta cb)
	{
		return cb.getEffect();
	}

	@Override
	public void setB(FireworkEffectMeta cb, FireworkEffect fb)
	{
		cb.setEffect(fb);
	}
	
}
