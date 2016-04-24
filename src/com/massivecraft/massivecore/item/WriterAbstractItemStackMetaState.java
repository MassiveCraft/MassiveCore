package com.massivecraft.massivecore.item;

import org.bukkit.inventory.meta.BlockStateMeta;

public abstract class WriterAbstractItemStackMetaState<OB, CB, FA, FB> extends WriterAbstractItemStackMeta<OB, CB, FA, FB>
{
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public CB createB()
	{
		BlockStateMeta blockStateMeta = (BlockStateMeta) super.createB();
		return (CB) blockStateMeta.getBlockState();
	}
	
}
