package com.massivecraft.massivecore.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class WriterItemStackMetaPotionColor extends WriterAbstractItemStackMetaField<PotionMeta, Integer, Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaPotionColor i = new WriterItemStackMetaPotionColor();
	public static WriterItemStackMetaPotionColor get() { return i; }
	
	public WriterItemStackMetaPotionColor()
	{
		super(PotionMeta.class);
		this.setMaterial(Material.POTION);
		this.setConverterTo(ConverterToColor.get());
		this.setConverterFrom(ConverterFromColor.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca, ItemStack d)
	{
		return ca.getPotionColor();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa, ItemStack d)
	{
		ca.setPotionColor(fa);
	}

	@Override
	public Color getB(PotionMeta cb, ItemStack d)
	{
		return cb.getColor();
	}

	@Override
	public void setB(PotionMeta cb, Color fb, ItemStack d)
	{
		cb.setColor(fb);
	}
	
}
