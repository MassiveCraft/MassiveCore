package com.massivecraft.massivecore.chestgui;

import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.util.MUtil;

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
	
	protected Map<Integer, ChestAction> indexToAction = new MassiveMap<Integer, ChestAction>();
	public Map<Integer, ChestAction> getIndexToAction() { return this.indexToAction; }
	public ChestAction removeAction(ItemStack item) { return this.indexToAction.remove(item); }
	public ChestAction setAction(int index, ChestAction action) { return this.indexToAction.put(index, action); }
	public ChestAction setAction(int index, String command) { return this.setAction(index, new ChestActionCommand(command)); }
	public ChestAction getAction(int index) { return this.indexToAction.get(index); }
	
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
		if (MUtil.isntPlayer(human)) return;
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
