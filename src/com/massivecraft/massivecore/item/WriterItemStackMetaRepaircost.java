package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.Repairable;

public class WriterItemStackMetaRepaircost extends WriterAbstractItemMeta<Repairable, Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaRepaircost i = new WriterItemStackMetaRepaircost();
	public static WriterItemStackMetaRepaircost get() { return i; }
	{
		this.setMaterial(Material.IRON_CHESTPLATE);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca)
	{
		return ca.getRepaircost();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa)
	{
		ca.setRepaircost(fa);
	}

	@Override
	public Integer getB(Repairable cb)
	{
		return cb.getRepairCost();
	}

	@Override
	public void setB(Repairable cb, Integer fb)
	{
		cb.setRepairCost(fb);
	}
	
}
