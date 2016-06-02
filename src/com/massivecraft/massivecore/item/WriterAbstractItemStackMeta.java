package com.massivecraft.massivecore.item;

import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class WriterAbstractItemStackMeta<OB, CB, FA, FB> extends WriterAbstractItemStack<OB, CB, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractItemStackMeta(Class<CB> classCB)
	{
		super(classCB);
	}
	
	// -------------------------------------------- //
	// CREATE INNER
	// -------------------------------------------- //
	
	public ItemMeta createItemMeta()
	{
		return createItemMeta(this.createItemStack());
	}
	
	public static ItemMeta createItemMeta(ItemStack itemStack)
	{
		return itemStack.getItemMeta();
	}
	
	// -------------------------------------------- //
	// BANNER
	// -------------------------------------------- //
	
	public static Banner getBanner(BlockStateMeta meta, boolean creative)
	{
		// Null
		if (meta == null) return null;
		
		// Creative
		if (!meta.hasBlockState() && !creative) return null;
		
		// Try
		try
		{
			BlockState ret = meta.getBlockState();
			if ( ! (ret instanceof Banner)) return null;
			return (Banner)ret;
		}
		catch (Exception e)
		{
			// Catch errors such as: throw new IllegalStateException("Missing blockState for " + material);
			return null;
		}
	}
	
	public static void setBanner(BlockStateMeta meta, Banner banner)
	{
		// Null
		if (meta == null) return;
		if (banner == null) return;
		
		// Dodge Update NPE
		if (banner.getBaseColor() == null) return;
		
		// Update with Force without Physics
		banner.update(true, false);
		
		// Apply
		meta.setBlockState(banner);
	}
	
}
