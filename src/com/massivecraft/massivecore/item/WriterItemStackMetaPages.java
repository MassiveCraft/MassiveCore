package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;

public class WriterItemStackMetaPages extends WriterAbstractItemMeta<BookMeta, List<String>, List<String>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaPages i = new WriterItemStackMetaPages();
	public static WriterItemStackMetaPages get() { return i; }
	{
		this.setMaterial(Material.WRITTEN_BOOK);
	}

	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public List<String> getA(DataItemStack ca)
	{
		return ca.getPages();
	}
	
	@Override
	public void setA(DataItemStack ca, List<String> fa)
	{
		ca.setPages(fa);
	}
	
	@Override
	public List<String> getB(BookMeta cb)
	{
		return cb.getPages();
	}
	
	@Override
	public void setB(BookMeta cb, List<String> fb)
	{
		cb.setPages(fb);
	}
	
}
