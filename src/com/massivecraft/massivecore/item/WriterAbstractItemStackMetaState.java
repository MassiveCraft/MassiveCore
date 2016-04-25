package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class WriterAbstractItemStackMetaState<OB, CB, FA, FB> extends WriterAbstractItemStackMeta<OB, CB, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractItemStackMetaState()
	{
		// For the setup to pass we must set a Material with a BlockStateMeta.
		this.setMaterial(Material.SHIELD);
	}
	
	// -------------------------------------------- //
	// CREATE INNER
	// -------------------------------------------- //
	
	public BlockState createItemMetaState()
	{
		return createItemMetaState(this.createItemMeta());
	}
	
	public static BlockState createItemMetaState(ItemMeta itemMeta)
	{
		if ( ! (itemMeta instanceof BlockStateMeta)) return null;
		BlockStateMeta blockStateMeta = (BlockStateMeta)itemMeta;
		try
		{
			return blockStateMeta.getBlockState();
		}
		catch (Exception e)
		{
			// Catch errors such as: throw new IllegalStateException("Missing blockState for " + material);
			return null;
		}
	}
	
}
