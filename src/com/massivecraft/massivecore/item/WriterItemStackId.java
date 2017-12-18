package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WriterItemStackId extends WriterAbstractItemStackField<String, Material>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackId i = new WriterItemStackId();
	public static WriterItemStackId get() { return i; }
	
	public WriterItemStackId()
	{
		this.setConverterTo(ConverterToMaterial.get());
		this.setConverterFrom(ConverterFromMaterial.get());
	}
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca, ItemStack d)
	{
		return ca.getId();
	}

	@Override
	public void setA(DataItemStack ca, String fa, ItemStack d)
	{
		ca.setId(fa);
	}
	
	@Override
	public Material getB(ItemStack cb, ItemStack d)
	{
		return cb.getType();
	}
	
	@Override
	public void setB(ItemStack cb, Material fb, ItemStack d)
	{
		cb.setType(fb);
	}
	
}
