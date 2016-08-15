package com.massivecraft.massivecore.item;

import org.bukkit.block.BlockState;

public abstract class WriterAbstractItemStackMetaStateField<CB, FA, FB> extends WriterAbstractItemStackMetaState<BlockState, CB, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractItemStackMetaStateField(Class<CB> classCB)
	{
		super(classCB);
	}
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public BlockState createOB()
	{
		return this.createItemMetaState();
	}
	
}
