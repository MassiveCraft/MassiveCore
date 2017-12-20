package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.nms.NmsItemStackMetaUnbreakable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WriterItemStackMetaUnbreakable extends WriterAbstractItemStackMetaField<ItemMeta, Boolean, Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaUnbreakable i = new WriterItemStackMetaUnbreakable();
	public static WriterItemStackMetaUnbreakable get() { return i; }
	public WriterItemStackMetaUnbreakable()
	{
		super(ItemMeta.class);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Boolean getA(DataItemStack ca, ItemStack d)
	{
		return ca.isUnbreakable();
	}

	@Override
	public void setA(DataItemStack ca, Boolean fa, ItemStack d)
	{
		ca.setUnbreakable(fa);
	}

	@Override
	public Boolean getB(ItemMeta cb, ItemStack d)
	{
		return NmsItemStackMetaUnbreakable.get().isUnbreakable(cb);
	}

	@Override
	public void setB(ItemMeta cb, Boolean fb, ItemStack d)
	{
		NmsItemStackMetaUnbreakable.get().setUnbreakable(cb,fb);
	}
	
}
