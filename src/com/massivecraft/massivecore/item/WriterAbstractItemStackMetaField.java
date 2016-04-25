package com.massivecraft.massivecore.item;

import org.bukkit.inventory.meta.ItemMeta;

public abstract class WriterAbstractItemStackMetaField<CB, FA, FB> extends WriterAbstractItemStackMeta<ItemMeta, CB, FA, FB>
{
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public ItemMeta createOB()
	{
		return this.createItemMeta();
	}
	
}
