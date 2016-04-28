package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

public class WriterItemStackMetaRepaircost extends WriterAbstractItemStackMetaField<Repairable, Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaRepaircost i = new WriterItemStackMetaRepaircost();
	public static WriterItemStackMetaRepaircost get() { return i; }
	public WriterItemStackMetaRepaircost()
	{
		super(Repairable.class);
		this.setMaterial(Material.IRON_CHESTPLATE);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca, ItemStack d)
	{
		return ca.getRepaircost();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa, ItemStack d)
	{
		ca.setRepaircost(fa);
	}

	@Override
	public Integer getB(Repairable cb, ItemStack d)
	{
		return cb.getRepairCost();
	}

	@Override
	public void setB(Repairable cb, Integer fb, ItemStack d)
	{
		cb.setRepairCost(fb);
	}
	
}
