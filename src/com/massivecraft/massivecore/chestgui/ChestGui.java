package com.massivecraft.massivecore.chestgui;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;

public class ChestGui
{
	// -------------------------------------------- //
	// REGISTRY
	// -------------------------------------------- //
	
	private static final Map<Inventory, ChestGui> inventoryToGui = new MassiveMap<Inventory, ChestGui>();
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
	// ACTIONS
	// -------------------------------------------- //
	
	private Map<Integer, ChestAction> indexToAction = new MassiveMap<Integer, ChestAction>();
	public Map<Integer, ChestAction> getIndexToAction() { return this.indexToAction; }
	public ChestAction removeAction(ItemStack item) { return this.indexToAction.remove(item); }
	public ChestAction setAction(int index, ChestAction action) { return this.indexToAction.put(index, action); }
	public ChestAction setAction(int index, String command) { return this.setAction(index, new ChestActionCommand(command)); }
	public ChestAction getAction(int index) { return this.indexToAction.get(index); }
	
	// -------------------------------------------- //
	// LAST ACTION
	// -------------------------------------------- //
	
	private ChestAction lastAction = null;
	public ChestAction getLastAction() { return this.lastAction; }
	public void setLastAction(ChestAction lastAction) { this.lastAction = lastAction; }
	
	// -------------------------------------------- //
	// META DATA
	// -------------------------------------------- //
	
	private final Map<String, Object> meta = new MassiveMap<>();
	public Map<String, Object> getMeta() { return this.meta; }
	
	// -------------------------------------------- //
	// RUNNABLES
	// -------------------------------------------- //
	
	private final List<Runnable> runnablesOpen = new MassiveList<>();
	public List<Runnable> getRunnablesOpen() { return this.runnablesOpen; }
	
	private final List<Runnable> runnablesClose = new MassiveList<>();
	public List<Runnable> getRunnablesClose() { return this.runnablesClose; }
	
	// -------------------------------------------- //
	// SOUND
	// -------------------------------------------- //
	
	private SoundEffect soundEffect = MassiveCoreMConf.get().clickSound;
	public SoundEffect getSoundEffect() { return this.soundEffect; }
	public ChestGui setSoundEffect(SoundEffect soundEffect) { this.soundEffect = soundEffect; return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ChestGui()
	{
		
	}
	
}
