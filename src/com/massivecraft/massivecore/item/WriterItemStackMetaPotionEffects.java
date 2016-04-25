package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class WriterItemStackMetaPotionEffects extends WriterAbstractItemStackMetaField<PotionMeta, List<DataPotionEffect>, List<PotionEffect>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaPotionEffects i = new WriterItemStackMetaPotionEffects();
	public static WriterItemStackMetaPotionEffects get() { return i; }
	{
		this.setMaterial(Material.POTION);
		this.setConverterTo(ConverterToPotionEffects.get());
		this.setConverterFrom(ConverterFromPotionEffects.get());
		this.addDependencyClasses(
			WriterPotionEffect.class
		);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public List<DataPotionEffect> getA(DataItemStack ca, ItemStack d)
	{
		return ca.getPotionEffects();
	}
	
	@Override
	public void setA(DataItemStack ca, List<DataPotionEffect> fa, ItemStack d)
	{
		ca.setPotionEffects(fa);
	}
	
	@Override
	public List<PotionEffect> getB(PotionMeta cb, ItemStack d)
	{
		return cb.getCustomEffects();
	}
	
	@Override
	public void setB(PotionMeta cb, List<PotionEffect> fb, ItemStack d)
	{
		for (PotionEffect potionEffect : fb)
		{
			cb.addCustomEffect(potionEffect, false);
		}
	}
	
}
