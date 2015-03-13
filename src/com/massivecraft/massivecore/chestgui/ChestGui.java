package com.massivecraft.massivecore.chestgui;

import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.collections.MassiveMap;

public class ChestGui
{
	// -------------------------------------------- //
	// STATIC REGISTRY
	// -------------------------------------------- //
	
	protected static Map<Inventory, ChestGui> inventoryToGui = new MassiveMap<Inventory, ChestGui>();
	public static Map<Inventory, ChestGui> getInventoryToGui() { return inventoryToGui; }
	public static ChestGui remove(Inventory inventory) { return inventoryToGui.remove(inventory); }
	public static ChestGui set(Inventory inventory, ChestGui gui) { return inventoryToGui.put(inventory, gui); }
	public static ChestGui get(Inventory inventory) { return inventoryToGui.get(inventory); }
	public static ChestGui getCreative(Inventory inventory)
	{
		ChestGui gui = get(inventory);
		if (gui != null) return gui;
		gui = new ChestGui();
		set(inventory, gui);
		return gui;
	}
	
	// -------------------------------------------- //
	// FIELDS: ACTIONS
	// -------------------------------------------- //
	
	protected Map<ItemStack, ChestAction> itemToAction = new MassiveMap<ItemStack, ChestAction>();
	public Map<ItemStack, ChestAction> getItemToAction() { return this.itemToAction; }
	public ChestAction removeAction(ItemStack item) { return this.itemToAction.remove(item); }
	public ChestAction setAction(ItemStack item, ChestAction action) { return this.itemToAction.put(item, action); }
	public ChestAction setAction(ItemStack item, String command) { return this.setAction(item, new ChestActionCommand(command)); }
	public ChestAction getAction(ItemStack item) { return this.itemToAction.get(item); }
	
	// -------------------------------------------- //
	// FIELDS: SOUND
	// -------------------------------------------- //
	
	protected Sound sound = Sound.CLICK;
	public Sound getSound() { return this.sound; }
	
	protected float volume = 1.0f;
	public float getVolume() { return this.volume; }
	public ChestGui setVolume(float volume) { this.volume = volume; return this; }
	
	protected float pitch = 1.0f;
	public float getPitch() { return this.pitch; }
	public ChestGui setPitch(float pitch) { this.pitch = pitch; return this; }
	
	public void playSound(Player player)
	{
		player.playSound(player.getEyeLocation(), this.getSound(), this.getVolume(), this.getPitch());
	}
	
	public void playSound(HumanEntity human)
	{
		if ( ! (human instanceof Player)) return;
		Player player = (Player)human;
		this.playSound(player);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ChestGui()
	{
		
	}
	
}
