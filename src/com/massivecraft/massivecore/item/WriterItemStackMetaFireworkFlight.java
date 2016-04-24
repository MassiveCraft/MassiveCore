package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;

public class WriterItemStackMetaFireworkFlight extends WriterAbstractItemMeta<FireworkMeta, Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaFireworkFlight i = new WriterItemStackMetaFireworkFlight();
	public static WriterItemStackMetaFireworkFlight get() { return i; }
	{
		this.setMaterial(Material.FIREWORK);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca)
	{
		return ca.getFireworkFlight();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa)
	{
		ca.setFireworkFlight(fa);
	}

	@Override
	public Integer getB(FireworkMeta cb)
	{
		return cb.getPower();
	}

	@Override
	public void setB(FireworkMeta cb, Integer fb)
	{
		cb.setPower(fb);
	}
	
}
