package com.massivecraft.massivecore.item;

import java.util.Set;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public class WriterItemStackMetaFlags extends WriterAbstractItemStackMetaField<ItemMeta, Set<String>, Set<ItemFlag>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaFlags i = new WriterItemStackMetaFlags();
	public static WriterItemStackMetaFlags get() { return i; }
	public WriterItemStackMetaFlags()
	{
		this.setConverterTo(ConverterToItemFlags.get());
		this.setConverterFrom(ConverterFromItemFlags.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Set<String> getA(DataItemStack ca)
	{
		return ca.getFlags();
	}

	@Override
	public void setA(DataItemStack ca, Set<String> fa)
	{
		ca.setFlags(fa);
	}

	@Override
	public Set<ItemFlag> getB(ItemMeta cb)
	{
		return cb.getItemFlags();
	}

	@Override
	public void setB(ItemMeta cb, Set<ItemFlag> fb)
	{
		cb.addItemFlags(fb.toArray(new ItemFlag[0]));
	}
	
}
