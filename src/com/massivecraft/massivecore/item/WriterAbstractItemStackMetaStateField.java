package com.massivecraft.massivecore.item;

import org.bukkit.block.BlockState;

public abstract class WriterAbstractItemStackMetaStateField<S, FA, FB> extends WriterAbstractItemStackMetaState<BlockState, S, FA, FB>
{
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public BlockState createOB()
	{
		return this.createItemMetaState();
	}
	
}
