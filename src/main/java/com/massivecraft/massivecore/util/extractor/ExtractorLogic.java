package com.massivecraft.massivecore.util.extractor;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
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

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;

public class ExtractorLogic
{
	// -------------------------------------------- //
	// SENDER
	// -------------------------------------------- //
	
	public static CommandSender sender(String o) { return IdUtil.getSender(o); }
	
	public static CommandSender sender(PlayerEvent o) { return o.getPlayer(); }
	public static CommandSender sender(BlockBreakEvent o) { return o.getPlayer(); }
	public static CommandSender sender(BlockDamageEvent o) { return o.getPlayer(); }
	public static CommandSender sender(BlockIgniteEvent o) { return o.getPlayer(); }
	public static CommandSender sender(BlockPlaceEvent o) { return o.getPlayer(); }
	public static CommandSender sender(SignChangeEvent o) { return o.getPlayer(); }
	public static CommandSender sender(EnchantItemEvent o) { return o.getEnchanter(); }
	public static CommandSender sender(PrepareItemEnchantEvent o) { return o.getEnchanter(); }
	public static CommandSender sender(Entity o) { if (o instanceof CommandSender) return (CommandSender)o; return null; }
	public static CommandSender sender(EntityEvent o) { return sender(o.getEntity()); }
	public static CommandSender sender(InventoryClickEvent o) { return sender(o.getWhoClicked()); }
	public static CommandSender sender(InventoryCloseEvent o) { return sender(o.getPlayer()); }
	public static CommandSender sender(InventoryOpenEvent o) { return sender(o.getPlayer()); }
	public static CommandSender sender(HangingBreakByEntityEvent o) { return sender(o.getRemover()); }
	public static CommandSender sender(VehicleDamageEvent o) { return sender(o.getAttacker()); }
	public static CommandSender sender(VehicleDestroyEvent o) { return sender(o.getAttacker()); }
	public static CommandSender sender(VehicleEnterEvent o) { return sender(o.getEntered()); }
	public static CommandSender sender(VehicleExitEvent o) { return sender(o.getExited()); }
	public static CommandSender sender(VehicleEvent o) { return sender(o.getVehicle().getPassenger()); }
	
	public static CommandSender senderFromObject(Object o)
	{
		if (o == null) return null;
		
		if (o instanceof CommandSender) return (CommandSender)o;
		
		if (o instanceof String) return sender((String)o);
		if (o instanceof PlayerEvent) return sender((PlayerEvent)o);
		if (o instanceof BlockBreakEvent) return sender((BlockBreakEvent)o);
		if (o instanceof BlockDamageEvent) return sender((BlockDamageEvent)o);
		if (o instanceof BlockIgniteEvent) return sender((BlockIgniteEvent)o);
		if (o instanceof BlockPlaceEvent) return sender((BlockPlaceEvent)o);
		if (o instanceof SignChangeEvent) return sender((SignChangeEvent)o);
		if (o instanceof EnchantItemEvent) return sender((EnchantItemEvent)o);
		if (o instanceof PrepareItemEnchantEvent) return sender((PrepareItemEnchantEvent)o);
		if (o instanceof Entity) return sender((Entity)o);
		if (o instanceof EntityEvent) return sender((EntityEvent)o);
		if (o instanceof InventoryClickEvent) return sender((InventoryClickEvent)o);
		if (o instanceof InventoryCloseEvent) return sender((InventoryCloseEvent)o);
		if (o instanceof InventoryOpenEvent) return sender((InventoryOpenEvent)o);
		if (o instanceof HangingBreakByEntityEvent) return sender((HangingBreakByEntityEvent)o);
		if (o instanceof VehicleDamageEvent) return sender((VehicleDamageEvent)o);
		if (o instanceof VehicleDestroyEvent) return sender((VehicleDestroyEvent)o);
		if (o instanceof VehicleEnterEvent) return sender((VehicleEnterEvent)o);
		if (o instanceof VehicleExitEvent) return sender((VehicleExitEvent)o);
		if (o instanceof VehicleEvent) return sender((VehicleEvent)o);
		
		return null;
	}
	
	// -------------------------------------------- //
	// PLAYER
	// -------------------------------------------- //
	
	public static Player playerFromObject(Object o)
	{
		CommandSender sender = senderFromObject(o);
		if (sender instanceof Player) return (Player)sender;
		return null;
	}
	
	// -------------------------------------------- //
	// SENDER ID
	// -------------------------------------------- //
	
	public static String senderIdFromObject(Object o)
	{
		if (o == null) return null;
		
		String id = IdUtil.getId(o);
		if (id != null) return id;
		
		CommandSender sender = senderFromObject(o);
		if (sender == null) return null;
		
		return IdUtil.getId(sender);
	}
	
	// -------------------------------------------- //
	// SENDER NAME
	// -------------------------------------------- //
	
	public static String senderNameFromObject(Object o)
	{
		if (o == null) return null;
		
		String name = IdUtil.getName(o);
		if (name != null) return name;
		
		CommandSender sender = senderFromObject(o);
		if (sender == null) return null;
		
		return IdUtil.getName(sender);
	}
	
	// -------------------------------------------- //
	// PLAYER NAME
	// -------------------------------------------- //
	
	public static String playerNameFromObject(Object o)
	{
		String senderId = senderNameFromObject(o);
		//if (SenderUtil.isPlayerId(senderId)) return senderId;
		//return null;
		return senderId;
	}
	
	// -------------------------------------------- //
	// WORLD
	// -------------------------------------------- //
	
	public static World world(Block o) { return o.getWorld(); }
	public static World world(Location o) { return o.getWorld(); }
	public static World world(Entity o) { return o.getWorld(); }
	public static World world(PlayerEvent o) { return world(o.getPlayer()); }
	public static World world(PS o) { try { return o.asBukkitWorld(true); } catch (Exception e) { return null; }}
	
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
		if (o instanceof String)
		{
			String string = (String)o;
			if (MUtil.isValidUUID(string))
			{
				String ret = worldNameViaPsMixin(string);
				if (ret != null) return ret;
			}
			return string;
		}
		
		if (o instanceof PS) return ((PS)o).getWorld();
		
		World world = worldFromObject(o);
		if (world != null) return world.getName();
		
		String ret = worldNameViaPsMixin(o);
		if (ret != null) return ret;

		return null;
	}
	
	public static String worldNameViaPsMixin(Object senderObject)
	{
		if (senderObject == null) return null;
		
		PS ps = Mixin.getSenderPs(senderObject);
		if (ps == null) return null;
		
		return ps.getWorld();
	}
	
}

