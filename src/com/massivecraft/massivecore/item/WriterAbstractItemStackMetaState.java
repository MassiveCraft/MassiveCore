package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.BlockStateMeta;

public abstract class WriterAbstractItemStackMetaState<OB, CB, FA, FB> extends WriterAbstractItemStackMeta<OB, CB, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractItemStackMetaState()
	{
		// For the initial provoke to pass we must set a Material with a BlockStateMeta.
		this.setMaterial(Material.SHIELD);
	}
	
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
