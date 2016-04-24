package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;

public class WriterItemStackMetaAuthor extends WriterAbstractItemStackMetaField<BookMeta, String, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaAuthor i = new WriterItemStackMetaAuthor();
	public static WriterItemStackMetaAuthor get() { return i; }
	{
		this.setMaterial(Material.WRITTEN_BOOK);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca)
	{
		return ca.getAuthor();
	}

	@Override
	public void setA(DataItemStack ca, String fa)
	{
		ca.setAuthor(fa);
	}

	@Override
	public String getB(BookMeta cb)
	{
		return cb.getAuthor();
	}

	@Override
	public void setB(BookMeta cb, String fb)
	{
		cb.setAuthor(fb);
	}
	
}
