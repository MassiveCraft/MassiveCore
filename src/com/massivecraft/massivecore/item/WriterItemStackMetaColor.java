package com.massivecraft.massivecore.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class WriterItemStackMetaColor extends WriterAbstractItemMeta<LeatherArmorMeta, Integer, Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaColor i = new WriterItemStackMetaColor();
	public static WriterItemStackMetaColor get() { return i; }
	{
		this.setMaterial(Material.LEATHER_HELMET);
		this.setConverterTo(ConverterToColor.get());
		this.setConverterFrom(ConverterFromColor.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca)
	{
		return ca.getColor();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa)
	{
		ca.setColor(fa);
	}

	@Override
	public Color getB(LeatherArmorMeta cb)
	{
		return cb.getColor();
	}

	@Override
	public void setB(LeatherArmorMeta cb, Color fb)
	{
		cb.setColor(fb);
	}
	
}
