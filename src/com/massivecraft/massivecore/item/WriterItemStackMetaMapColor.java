package com.massivecraft.massivecore.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class WriterItemStackMetaMapColor extends WriterAbstractItemStackMetaField<MapMeta, Integer, Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaMapColor i = new WriterItemStackMetaMapColor();
	public static WriterItemStackMetaMapColor get() { return i; }
	
	public WriterItemStackMetaMapColor()
	{
		super(MapMeta.class);
		this.setMaterial(Material.MAP);
		this.setConverterTo(ConverterToColor.get());
		this.setConverterFrom(ConverterFromColor.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca, ItemStack d)
	{
		return ca.getMapColor();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa, ItemStack d)
	{
		ca.setMapColor(fa);
	}

	@Override
	public Color getB(MapMeta cb, ItemStack d)
	{
		return cb.getColor();
	}

	@Override
	public void setB(MapMeta cb, Color fb, ItemStack d)
	{
		cb.setColor(fb);
	}
	
}
