package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;

public class WriterItemStackMetaTitle extends WriterAbstractItemMeta<BookMeta, String, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaTitle i = new WriterItemStackMetaTitle();
	public static WriterItemStackMetaTitle get() { return i; }
	{
		this.setMaterial(Material.WRITTEN_BOOK);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca)
	{
		return ca.getTitle();
	}

	@Override
	public void setA(DataItemStack ca, String fa)
	{
		ca.setTitle(fa);
	}

	@Override
	public String getB(BookMeta cb)
	{
		return cb.getTitle();
	}

	@Override
	public void setB(BookMeta cb, String fb)
	{
		cb.setTitle(fb);
	}
	
}
