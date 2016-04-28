package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class WriterItemStackMetaAuthor extends WriterAbstractItemStackMetaField<BookMeta, String, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaAuthor i = new WriterItemStackMetaAuthor();
	public static WriterItemStackMetaAuthor get() { return i; }
	
	public WriterItemStackMetaAuthor()
	{
		super(BookMeta.class);
		this.setMaterial(Material.WRITTEN_BOOK);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca, ItemStack d)
	{
		return ca.getAuthor();
	}

	@Override
	public void setA(DataItemStack ca, String fa, ItemStack d)
	{
		ca.setAuthor(fa);
	}

	@Override
	public String getB(BookMeta cb, ItemStack d)
	{
		return cb.getAuthor();
	}

	@Override
	public void setB(BookMeta cb, String fb, ItemStack d)
	{
		cb.setAuthor(fb);
	}
	
}
