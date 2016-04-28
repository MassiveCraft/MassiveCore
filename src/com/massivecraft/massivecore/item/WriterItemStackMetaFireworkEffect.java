package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

public class WriterItemStackMetaFireworkEffect extends WriterAbstractItemStackMetaField<FireworkEffectMeta, DataFireworkEffect, FireworkEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaFireworkEffect i = new WriterItemStackMetaFireworkEffect();
	public static WriterItemStackMetaFireworkEffect get() { return i; }
	public WriterItemStackMetaFireworkEffect()
	{
		super(FireworkEffectMeta.class);
		this.setMaterial(Material.FIREWORK_CHARGE);
		this.setConverterTo(ConverterToFireworkEffect.get());
		this.setConverterFrom(ConverterFromFireworkEffect.get());
		this.addDependencyClasses(
			WriterFireworkEffect.class
		);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public DataFireworkEffect getA(DataItemStack ca, ItemStack d)
	{
		return ca.getFireworkEffect();
	}

	@Override
	public void setA(DataItemStack ca, DataFireworkEffect fa, ItemStack d)
	{
		ca.setFireworkEffect(fa);
	}

	@Override
	public FireworkEffect getB(FireworkEffectMeta cb, ItemStack d)
	{
		return cb.getEffect();
	}

	@Override
	public void setB(FireworkEffectMeta cb, FireworkEffect fb, ItemStack d)
	{
		cb.setEffect(fb);
	}
	
}
