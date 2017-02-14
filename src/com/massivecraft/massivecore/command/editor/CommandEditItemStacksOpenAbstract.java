package com.massivecraft.massivecore.command.editor;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.requirement.RequirementEditorPropertyCreated;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.mixin.MixinInventory;
import com.massivecraft.massivecore.util.MUtil;

public abstract class CommandEditItemStacksOpenAbstract<O, V> extends CommandEditAbstract<O, V> implements Listener
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditItemStacksOpenAbstract(EditSettings<O> settings, Property<O, V> property)
	{
		// Super
		super(settings, property, true);
		
		// Aliases
		String alias = this.createCommandAlias();
		this.setAliases(alias);
		
		// Desc
		this.setDesc(alias + " " + this.getPropertyName());
		
		// Requirements
		this.addRequirements(RequirementEditorPropertyCreated.get(true));
		this.addRequirements(RequirementIsPlayer.get());
		
		// Listener
		Bukkit.getPluginManager().registerEvents(this, MassiveCore.get());
	}
	
	// -------------------------------------------- //
	// EDITING
	// -------------------------------------------- //
	
	protected Set<UUID> playerIds = new MassiveSet<UUID>();
	
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
		V after = asContainer(event.getInventory());
		
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
		V before = this.getProperty().getRaw(this.getObject());
		
		// Open Chest
		Inventory chest = asChest(before);
		this.setEditing(me, true);
		me.openInventory(chest);
	}
	
	// -------------------------------------------- //
	// CONVERT CONTAINER <--> CHEST
	// -------------------------------------------- //
	
	private Inventory asChest(V itemStacks)
	{
		// Create Ret
		Inventory ret = MixinInventory.get().createInventory(me, 54, this.getProperty().getName());
		
		// Dodge Null
		return itemStacks == null ? ret : this.asChestInner(itemStacks, ret);
	}
	
	protected abstract Inventory asChestInner(V itemStacks, Inventory inventory);
	protected abstract V asContainer(Inventory inventory);
	
}
