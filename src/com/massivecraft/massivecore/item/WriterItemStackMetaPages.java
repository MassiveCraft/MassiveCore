package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class WriterItemStackMetaPages extends WriterAbstractItemStackMetaField<BookMeta, List<String>, List<String>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaPages i = new WriterItemStackMetaPages();
	public static WriterItemStackMetaPages get() { return i; }
	public WriterItemStackMetaPages()
	{
		super(BookMeta.class);
		this.setMaterial(Material.WRITTEN_BOOK);
	}

	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public List<String> getA(DataItemStack ca, ItemStack d)
	{
		return ca.getPages();
	}
	
	@Override
	public void setA(DataItemStack ca, List<String> fa, ItemStack d)
	{
		ca.setPages(fa);
	}
	
	@Override
	public List<String> getB(BookMeta cb, ItemStack d)
	{
		return cb.getPages();
	}
	
	@Override
	public void setB(BookMeta cb, List<String> fb, ItemStack d)
	{
		cb.setPages(fb);
	}
	
}
