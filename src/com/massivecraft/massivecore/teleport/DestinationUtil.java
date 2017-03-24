package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.MassiveException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.Iterator;

public class DestinationUtil
{
	public static Player getPlayer(CommandSender sender) throws MassiveException
	{
		if ( ! (sender instanceof Player)) throw new MassiveException().addMsg("<b>You must be a player to use this destination.");
		return (Player)sender;
	}
	
	// We strictly avoid blocks since they have a tendency to not accept outside world coordinates.
	
	public static Location getThatLocation(LivingEntity livingEntity)
	{
		BlockIterator iter = createHeadlessIterator(livingEntity);
		Block block = nextSolid(iter);
		
		// Nothing solid in sight
		if (block == null) return null;
		
		Location oldLocation = livingEntity.getLocation();
		Location targetLocation = moveLocationToBlock(oldLocation, block);
		return targetLocation;
	}
	
	public static Location getThereLocation(LivingEntity livingEntity)
	{
		BlockIterator iter = createHeadlessIterator(livingEntity);
		Block block = nextBeforeSolid(iter);
		
		// Nothing solid in sight
		if (block == null) return null;
		
		Location oldLocation = livingEntity.getLocation();
		Location targetLocation = moveLocationToBlock(oldLocation, block);
		return targetLocation;
	}
	
	public static Location getJumpLocation(LivingEntity livingEntity)
	{
		BlockIterator iter = createHeadlessIterator(livingEntity);
		Block block = nextSolid(iter);
		
		// Nothing solid in sight
		if (block == null) return null;
		
		Location oldLocation = livingEntity.getLocation();
		Location targetLocation = moveUp(moveLocationToBlock(oldLocation, block));
		return targetLocation;
	}
	
	public static BlockIterator createHeadlessIterator(LivingEntity livingEntity)
	{
		BlockIterator ret = new BlockIterator(livingEntity, 300);
		ret.next();
		return ret;
	}
	
	public static Block nextSolid(Iterator<Block> iter)
	{
		if (iter == null) return null;
		while (iter.hasNext())
		{
			Block block = iter.next();
			if (block.getType().isSolid()) return block;
		}
		return null;
	}
	
	public static Block nextBeforeSolid(Iterator<Block> iter)
	{
		if (iter == null) return null;
		Block ret = null;
		while (iter.hasNext())
		{
			Block block = iter.next();
			if (block.getType().isSolid()) break;
			ret = block;
		}
		return ret;
	}
	
	public static Location moveUp(Location location)
	{
		Location ret = location.clone();
		while (!canStandIn(ret))
		{
			ret.add(0, 1, 0);
		}
		return ret;
	}
	
	public static boolean canStandIn(Location location)
	{
		return canStandIn(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	public static boolean canStandIn(World world, int x, int y, int z)
	{
		if (isSolid(world, x, y, z)) return false;
		if (isSolid(world, x, y+1, z)) return false;
		if (!isSolid(world, x, y-1, z)) return false;
		return true;
	}
	
	public static boolean isSolid(World world, int x, int y, int z)
	{
		if (y > world.getMaxHeight()) return false;
		if (y < 0) return false;
		return world.getBlockAt(x, y, z).getType().isSolid();
	}
	
	public static Location moveLocationToBlock(Location location, Block block)
	{
		return moveLocationToBlockCoords(location, block.getX(), block.getY(), block.getZ());
	}
	
	public static Location moveLocationToBlockCoords(Location location, int x, int y, int z)
	{
		Location ret = location.clone();
		
		ret.setX(x + location.getX() - location.getBlockX());
		ret.setY(y + location.getY() - location.getBlockY());
		ret.setZ(z + location.getZ() - location.getBlockZ());
		
		return ret;
	}
}
