package com.massivecraft.massivecore.item;

import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class WriterAbstractItemStackMetaStateMorph<FA, FB> extends WriterAbstractItemStackMetaState<ItemMeta, BlockState, FA, FB>
{
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public ItemMeta createOB()
	{
		return this.createItemMeta();
	}
	
	// -------------------------------------------- //
	// MORPH
	// -------------------------------------------- //
	
	@Override
	public BlockState morphB(ItemMeta ob)
	{
		return createItemMetaState(ob);
	}
	
	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //
	
	@Override
	public void writeInner(DataItemStack oa, ItemMeta ob, DataItemStack ca, BlockState cb, ItemStack d, boolean a2b)
	{
		super.writeInner(oa, ob, ca, cb, d, a2b);
		
		// Write Back 
		if (a2b)
		{
			BlockStateMeta blockStateMeta = (BlockStateMeta)ob;
			blockStateMeta.setBlockState(cb);
		}
	}
	
}
