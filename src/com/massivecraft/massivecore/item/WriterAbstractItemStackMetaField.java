package com.massivecraft.massivecore.item;

import org.bukkit.inventory.meta.ItemMeta;

public abstract class WriterAbstractItemStackMetaField<CB, FA, FB> extends WriterAbstractItemStackMeta<ItemMeta, CB, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractItemStackMetaField(Class<CB> classCB)
	{
		super(classCB);
	}
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public ItemMeta createOB()
	{
		return this.createItemMeta();
	}
	
}
