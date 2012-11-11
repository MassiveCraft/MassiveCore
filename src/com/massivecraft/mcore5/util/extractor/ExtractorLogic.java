package com.massivecraft.mcore5.util.extractor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.massivecraft.mcore5.PS;
import com.massivecraft.mcore5.store.PlayerEntity;

public class ExtractorLogic
{
	// -------------------------------------------- //
	// PLAYER
	// -------------------------------------------- //
	
	public static Player player(String o) { return Bukkit.getPlayerExact(o); }
	public static Player player(PlayerEvent o) { return o.getPlayer(); }
	public static Player player(BlockBreakEvent o) { return o.getPlayer(); }
	public static Player player(BlockDamageEvent o) { return o.getPlayer(); }
	public static Player player(BlockIgniteEvent o) { return o.getPlayer(); }
	public static Player player(BlockPlaceEvent o) { return o.getPlayer(); }
	public static Player player(SignChangeEvent o) { return o.getPlayer(); }
	public static Player player(EnchantItemEvent o) { return o.getEnchanter(); }
	public static Player player(PrepareItemEnchantEvent o) { return o.getEnchanter(); }
	public static Player player(Entity o) { if (o instanceof Player) return (Player)o; return null; }
	public static Player player(EntityEvent o) { return player(o.getEntity()); }
	public static Player player(InventoryClickEvent o) { return player(o.getWhoClicked()); }
	public static Player player(InventoryCloseEvent o) { return player(o.getPlayer()); }
	public static Player player(InventoryOpenEvent o) { return player(o.getPlayer()); }
	public static Player player(HangingBreakByEntityEvent o) { return player(o.getRemover()); }
	public static Player player(VehicleDamageEvent o) { return player(o.getAttacker()); }
	public static Player player(VehicleDestroyEvent o) { return player(o.getAttacker()); }
	public static Player player(VehicleEnterEvent o) { return player(o.getEntered()); }
	public static Player player(VehicleExitEvent o) { return player(o.getExited()); }
	public static Player player(VehicleEvent o) { return player(o.getVehicle().getPassenger()); }
	
	public static Player playerFromObject(Object o)
	{
		if (o instanceof Player) return (Player)o;
		
		if (o instanceof String) return player((String)o);
		if (o instanceof PlayerEvent) return player((PlayerEvent)o);
		if (o instanceof BlockBreakEvent) return player((BlockBreakEvent)o);
		if (o instanceof BlockDamageEvent) return player((BlockDamageEvent)o);
		if (o instanceof BlockIgniteEvent) return player((BlockIgniteEvent)o);
		if (o instanceof BlockPlaceEvent) return player((BlockPlaceEvent)o);
		if (o instanceof SignChangeEvent) return player((SignChangeEvent)o);
		if (o instanceof EnchantItemEvent) return player((EnchantItemEvent)o);
		if (o instanceof PrepareItemEnchantEvent) return player((PrepareItemEnchantEvent)o);
		if (o instanceof Entity) return player((Entity)o);
		if (o instanceof EntityEvent) return player((EntityEvent)o);
		if (o instanceof InventoryClickEvent) return player((InventoryClickEvent)o);
		if (o instanceof InventoryCloseEvent) return player((InventoryCloseEvent)o);
		if (o instanceof InventoryOpenEvent) return player((InventoryOpenEvent)o);
		if (o instanceof HangingBreakByEntityEvent) return player((HangingBreakByEntityEvent)o);
		if (o instanceof VehicleDamageEvent) return player((VehicleDamageEvent)o);
		if (o instanceof VehicleDestroyEvent) return player((VehicleDestroyEvent)o);
		if (o instanceof VehicleEnterEvent) return player((VehicleEnterEvent)o);
		if (o instanceof VehicleExitEvent) return player((VehicleExitEvent)o);
		if (o instanceof VehicleEvent) return player((VehicleEvent)o);
		
		return null;
	}
	
	// -------------------------------------------- //
	// PLAYER NAME
	// -------------------------------------------- //
	
	public static String playerNameFromObject(Object o)
	{
		if (o instanceof String) return (String)o;
		if (o instanceof PlayerEntity) return ((PlayerEntity<?>)o).getId();
		Player player = playerFromObject(o);
		if (player == null) return null;
		return player.getName();
	}
	
	// -------------------------------------------- //
	// WORLD
	// -------------------------------------------- //
	
	public static World world(Block o) { return o.getWorld(); }
	public static World world(Location o) { return o.getWorld(); }
	public static World world(Entity o) { return o.getWorld(); }
	public static World world(PlayerEvent o) { return world(o.getPlayer()); }
	public static World world(PS o) { return o.getWorld(); }
	
	public static World worldFromObject(Object o)
	{
		if (o instanceof World) return (World)o;
		
		if (o instanceof Block) return world((Block)o);
		if (o instanceof Location) return world((Location)o);
		if (o instanceof Entity) return world((Entity)o);
		if (o instanceof PlayerEvent) return world((PlayerEvent)o);
		if (o instanceof PS) return world((PS)o);
		
		return null;
	}
	
	// -------------------------------------------- //
	// WORLD NAME
	// -------------------------------------------- //
	
	public static String worldNameFromObject(Object o)
	{
		if (o instanceof String) return (String)o;
		if (o instanceof PS) return ((PS)o).getWorldName();
		World world = worldFromObject(o);
		if (world == null) return null;
		return world.getName();
	}
}
