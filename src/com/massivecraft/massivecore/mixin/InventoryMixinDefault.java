package com.massivecraft.massivecore.mixin;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

public class InventoryMixinDefault extends InventoryMixinAbstract
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static InventoryMixinDefault i = new InventoryMixinDefault();
	public static InventoryMixinDefault get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PlayerInventory createPlayerInventory()
	{
		List<World> worlds = Bukkit.getWorlds();
		World world = worlds.get(0);
		
		Location location = world.getSpawnLocation().clone();
		location.setY(999);
		
		Player player = (Player) world.spawnEntity(location, EntityType.PLAYER);
		PlayerInventory ret = player.getInventory();
		player.remove();
		return ret;
	}
	
	@Override
	public Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		return Bukkit.createInventory(holder, size, title);
	}
	
}
