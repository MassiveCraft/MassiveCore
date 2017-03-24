package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.mixin.MixinInventory;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandEditItemStacksOpen<O> extends CommandEditItemStacksAbstract<O> implements Listener
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditItemStacksOpen(EditSettings<O> settings, Property<O, List<ItemStack>> property)
	{
		// Super	
		super(settings, property);
		
		// Requirements
		this.addRequirements(RequirementIsPlayer.get());
		
		// Listener
		Bukkit.getPluginManager().registerEvents(this, MassiveCore.get());
	}
	
	// -------------------------------------------- //
	// EDITING
	// -------------------------------------------- //
	
	protected Set<UUID> playerIds = new MassiveSet<>();
	
	public void setEditing(Player player, boolean editing)
	{
		UUID playerId = player.getUniqueId();
		
		if (editing)
		{
			this.playerIds.add(playerId);
		}
		else
		{
			this.playerIds.remove(playerId);
		}
	}
	
	public boolean isEditing(Player player)
	{
		UUID playerId = player.getUniqueId();
		
		return this.playerIds.contains(playerId);
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	// Not Cancellable
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClose(InventoryCloseEvent event)
	{
		// If a player closes an inventory ...
		if (MUtil.isntPlayer(event.getPlayer())) return;
		Player player = (Player)event.getPlayer();
		
		// ... and that player is editing ...
		if ( ! this.isEditing(player)) return;
		
		// ... set the player as not editing ...
		this.setEditing(player, false);
	
		// ... load the item stacks into a list ...
		List<ItemStack> after = asList(event.getInventory());
		
		// ... attempt set.
		this.senderFieldsOuter(player);
		this.attemptSet(after);
		this.senderFieldsOuter(null);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Get Before
		List<ItemStack> before = this.getProperty().getRaw(this.getObject());
		
		// Open Chest
		Inventory chest = asChest(before);
		this.setEditing(me, true);
		me.openInventory(chest);
	}
	
	// -------------------------------------------- //
	// CONVERT LIST <--> CHEST
	// -------------------------------------------- //
	
	public Inventory asChest(List<ItemStack> itemStacks)
	{
		// Dodge Null
		if (itemStacks == null) return null;
		
		// Create Ret
		Inventory ret = MixinInventory.get().createInventory(me, 54, this.getProperty().getName());
		
		// Fill Ret
		for (int i = 0; i < itemStacks.size(); i++)
		{
			ItemStack itemStack = itemStacks.get(i);
			if (InventoryUtil.isNothing(itemStack)) continue;
			itemStack = new ItemStack(itemStack);
			
			ret.setItem(i, itemStack);
		}
		
		// Return Ret
		return ret;
	}
	
	public List<ItemStack> asList(Inventory inventory)
	{
		// Dodge Null
		if (inventory == null) return null;
		
		// Create Ret
		List<ItemStack> ret = new MassiveList<>();
		
		// Fill Ret
		for (int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack itemStack = inventory.getItem(i);
			if (InventoryUtil.isNothing(itemStack)) continue;
			itemStack = new ItemStack(itemStack);
			
			ret.add(itemStack);
		}
		
		// Return Ret
		return ret;
	}
	
}
