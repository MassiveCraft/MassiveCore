package com.massivecraft.massivecore.chestgui;

import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.SoundEffect;
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
	
	protected Map<Integer, ChestAction> indexToAction = new MassiveMap<Integer, ChestAction>();
	public Map<Integer, ChestAction> getIndexToAction() { return this.indexToAction; }
	public ChestAction removeAction(ItemStack item) { return this.indexToAction.remove(item); }
	public ChestAction setAction(int index, ChestAction action) { return this.indexToAction.put(index, action); }
	public ChestAction setAction(int index, String command) { return this.setAction(index, new ChestActionCommand(command)); }
	public ChestAction getAction(int index) { return this.indexToAction.get(index); }
	
	// -------------------------------------------- //
	// FIELDS: SOUND
	// -------------------------------------------- //
	
	protected SoundEffect soundEffect = SoundEffect.valueOf("CLICK", 1.0f, 1.0f);
	public SoundEffect getSoundEffect() { return this.soundEffect; }
	public ChestGui setSoundEffect(SoundEffect soundEffect) { this.soundEffect = soundEffect; return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ChestGui()
	{
		
	}
	
}
