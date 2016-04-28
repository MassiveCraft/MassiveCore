package com.massivecraft.massivecore.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class WriterItemStackMetaColor extends WriterAbstractItemStackMetaField<LeatherArmorMeta, Integer, Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaColor i = new WriterItemStackMetaColor();
	public static WriterItemStackMetaColor get() { return i; }
	
	public WriterItemStackMetaColor()
	{
		super(LeatherArmorMeta.class);
		this.setMaterial(Material.LEATHER_HELMET);
		this.setConverterTo(ConverterToColor.get());
		this.setConverterFrom(ConverterFromColor.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca, ItemStack d)
	{
		return ca.getColor();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa, ItemStack d)
	{
		ca.setColor(fa);
	}

	@Override
	public Color getB(LeatherArmorMeta cb, ItemStack d)
	{
		return cb.getColor();
	}

	@Override
	public void setB(LeatherArmorMeta cb, Color fb, ItemStack d)
	{
		cb.setColor(fb);
	}
	
}
