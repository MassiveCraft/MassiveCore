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
	
	private static final Map<Inventory, ChestGui> inventoryToGui = new MassiveMap<>();
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
	// Actions are assigned to indexes in the inventory.
	// This means the system does not care about what item is in the slot.
	// It just cares about which slot it is.
	// One could have imagined an approach where we looked at the item instead.
	// That is however not feasible since the Bukkit ItemStack equals method is not reliable.
	
	private Map<Integer, ChestAction> indexToAction = new MassiveMap<Integer, ChestAction>();
	public Map<Integer, ChestAction> getIndexToAction() { return this.indexToAction; }
	public ChestAction removeAction(ItemStack item) { return this.indexToAction.remove(item); }
	public ChestAction setAction(int index, ChestAction action) { return this.indexToAction.put(index, action); }
	public ChestAction setAction(int index, String command) { return this.setAction(index, new ChestActionCommand(command)); }
	public ChestAction getAction(int index) { return this.indexToAction.get(index); }
	
	// -------------------------------------------- //
	// LAST ACTION
	// -------------------------------------------- //
	// The last executed action is stored here.
	// This can for example be useful in the inventory close task.
	
	private ChestAction lastAction = null;
	public ChestAction getLastAction() { return this.lastAction; }
	public void setLastAction(ChestAction lastAction) { this.lastAction = lastAction; }
	
	// -------------------------------------------- //
	// META DATA
	// -------------------------------------------- //
	// Store your arbitrary stuff here. Might come in handy in the future.
	// I don't think we are currently using this ourselves.
	
	private final Map<String, Object> meta = new MassiveMap<>();
	public Map<String, Object> getMeta() { return this.meta; }
	
	// -------------------------------------------- //
	// RUNNABLES
	// -------------------------------------------- //
	// Runnables to be executed after certain events.
	// They are all delayed with one (or is it zero) ticks.
	// This way we don't bug out if you open a new GUI after close.
	
	private final List<Runnable> runnablesOpen = new MassiveList<>();
	public List<Runnable> getRunnablesOpen() { return this.runnablesOpen; }
	
	private final List<Runnable> runnablesClose = new MassiveList<>();
	public List<Runnable> getRunnablesClose() { return this.runnablesClose; }
	
	// -------------------------------------------- //
	// SOUND
	// -------------------------------------------- //
	// The click sound you should hear when clicking an action slot.
	// You can disable it by setting null.
	
	private SoundEffect soundEffect = MassiveCoreMConf.get().clickSound;
	public SoundEffect getSoundEffect() { return this.soundEffect; }
	public ChestGui setSoundEffect(SoundEffect soundEffect) { this.soundEffect = soundEffect; return this; }
	
	// -------------------------------------------- //
	// AUTOCLOSING
	// -------------------------------------------- //
	// Should the GUI be automatically closed upon clicking an action?
	
	private boolean autoclosing = true;
	public boolean isAutoclosing() { return this.autoclosing; }
	public ChestGui setAutoclosing(boolean autoclosing) { this.autoclosing = autoclosing; return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ChestGui()
	{
		
	}
	
}
