package com.massivecraft.mcore.util;

import net.minecraft.server.v1_7_R3.WorldServer;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;

/**
 * Find the method by going through CraftBlock.setTypeId()
 * The method should look something like this:
 * 
 *
    public boolean t(int i, int j, int k) {
        boolean flag = false;

        if (!this.worldProvider.g) {
            flag |= this.c(EnumSkyBlock.SKY, i, j, k);
        }

        flag |= this.c(EnumSkyBlock.BLOCK, i, j, k);
        return flag;
    }
 * 
 */
public class LightUtil
{
	// -------------------------------------------- //
	// RECALC LIGHT LEVEL AT
	// -------------------------------------------- //
	// This method will update the light level for the block.
	// It will however only work properly if all chunks that are around the chunk the block is in are loaded.
	
	public static void recalcLightLevelAt(Block block)
	{
		recalcLightLevelAt(block.getWorld(), block.getX(), block.getY(), block.getZ());
	}
	
	public static void recalcLightLevelAt(World world, int x, int y, int z)
	{
		CraftWorld cworld = (CraftWorld)world;
		WorldServer worldServer = cworld.getHandle();
		worldServer.t(x, y, z);
	}
	
}
