package com.massivecraft.massivecore.chestgui;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class ChestGui
{
	// -------------------------------------------- //
	// REGISTRY
	// -------------------------------------------- //
	
	protected static final Map<Inventory, ChestGui> inventoryToGui = new MassiveMap<>();
	public static Map<Inventory, ChestGui> getInventoryToGui() { return inventoryToGui; }
	
	public static ChestGui get(Inventory inventory) { return inventoryToGui.get(inventory); }
	public static ChestGui getCreative(Inventory inventory)
	{
		if (inventory == null) throw new NullPointerException("inventory");
		
		ChestGui gui = get(inventory);
		if (gui != null) return gui;
		
		gui = new ChestGui();
		gui.setInventory(inventory);
		gui.add();
		
		return gui;
	}
	
	private static void add(Inventory inventory, ChestGui gui) { inventoryToGui.put(inventory, gui); }
	private static void remove(Inventory inventory) { inventoryToGui.remove(inventory); }
	
	// -------------------------------------------- //
	// ADD & REMOVE
	// -------------------------------------------- //
	// Done through instance for override possibilities.
	
	public void add()
	{
		add(this.getInventory(), this);
	}
	
	public void remove()
	{
		remove(this.getInventory());
	}
	
	// -------------------------------------------- //
	// INVENTORY
	// -------------------------------------------- //
	// It is useful to provide a link back from the GUI to the inventory.
	// This way we have can look up between Inventory and ChestGui both ways.
	
	private Inventory inventory = null;
	public Inventory getInventory() { return this.inventory; }
	public void setInventory(Inventory inventory) { this.inventory = inventory; }
	
	// -------------------------------------------- //
	// ACTIONS
	// -------------------------------------------- //
	// Actions are assigned to indexes in the inventory.
	// This means the system does not care about what item is in the slot.
	// It just cares about which slot it is.
	// One could have imagined an approach where we looked at the item instead.
	// That is however not feasible since the Bukkit ItemStack equals method is not reliable.
	
	private Map<Integer, ChestAction> indexToAction = new MassiveMap<>();
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
	// SOUNDS
	// -------------------------------------------- //
	// This section contains all kinds of sounds.
	// You can disable a sound by setting it to null.
	
	// The sound you should hear when clicking an action slot.
	private SoundEffect soundClick = MassiveCoreMConf.get().clickSound;
	public SoundEffect getSoundClick() { return this.soundClick; }
	public void setSoundClick(SoundEffect soundClick) { this.soundClick = soundClick; }
	
	// The sound you should hear when opening the GUI.
	private SoundEffect soundOpen = SoundEffect.valueOf("CHEST_OPEN", 0.75f, 1.0f);
	public SoundEffect getSoundOpen() { return this.soundOpen; }
	public void setSoundOpen(SoundEffect soundOpen) { this.soundOpen = soundOpen; }
	
	// The sound you should hear when closing the GUI.
	// This sound will be skipped if another inventory was opened by the GUI action.
	private SoundEffect soundClose = SoundEffect.valueOf("CHEST_CLOSE", 0.75f, 1.0f);
	public SoundEffect getSoundClose() { return this.soundClose; }
	public void setSoundClose(SoundEffect soundClose) { this.soundClose= soundClose; }
	
	// -------------------------------------------- //
	// AUTOCLOSING
	// -------------------------------------------- //
	// Should the GUI be automatically closed upon clicking an action?
	
	private boolean autoclosing = true;
	public boolean isAutoclosing() { return this.autoclosing; }
	public void setAutoclosing(boolean autoclosing) { this.autoclosing = autoclosing; }
	
	// -------------------------------------------- //
	// AUTOREMOVING
	// -------------------------------------------- //
	// Should the GUI be automatically removed upon the inventory closing?
	
	private boolean autoremoving = true;
	public boolean isAutoremoving() { return this.autoremoving; }
	public void setAutoremoving(boolean autoremoving) { this.autoremoving = autoremoving; }
	
	// -------------------------------------------- //
	// ALLOWBOTTOMINVENTORY
	// -------------------------------------------- //
	
	private boolean allowBottomInventory = false;
	public boolean isBottomInventoryAllowed() { return this.allowBottomInventory; }
	public void setBottomInventoryAllow(boolean allowBottomInventory) { this.allowBottomInventory = allowBottomInventory; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ChestGui()
	{
		
	}
	
}
