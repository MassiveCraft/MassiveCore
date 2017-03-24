package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.event.EventMassiveCoreLorePriority;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map.Entry;

public class EngineMassiveCoreLorePriority extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreLorePriority i = new EngineMassiveCoreLorePriority();
	public static EngineMassiveCoreLorePriority get() { return i; }
	
	// -------------------------------------------- //
	// SORT LORE
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void sortItemLore(InventoryClickEvent event)
	{
		if (!MassiveCoreMConf.get().loreSortOnInventoryClick) return;

		HumanEntity human = event.getWhoClicked();
		if (MUtil.isntPlayer(human)) return;

		ItemStack item = event.getCurrentItem();
		if (InventoryUtil.isNothing(item)) return;

		InventoryUtil.sortLore(item);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void sortItemLore(InventoryOpenEvent event)
	{
		if (!MassiveCoreMConf.get().loreSortOnInventoryOpen) return;

		HumanEntity human = event.getPlayer();
		if (MUtil.isntPlayer(human)) return;

		InventoryUtil.sortLore(event.getView().getTopInventory());
		InventoryUtil.sortLore(event.getView().getBottomInventory());
	}

	// -------------------------------------------- //
	// CONFIG PRIORITIES
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.HIGHEST)
	public void setLorePriorities(EventMassiveCoreLorePriority event)
	{
		this.setPrioritiesPrefix(event);
		this.setPrioritiesRegex(event);
	}

	public void setPrioritiesRegex(EventMassiveCoreLorePriority event)
	{
		for (Entry<String, Integer> prefixEntry : MassiveCoreMConf.get().lorePrioritiesRegex.entrySet())
		{
			String regex = prefixEntry.getKey();
			int priority = prefixEntry.getValue();
			event.setPriorityByRegex(regex, priority);
		}
	}

	public void setPrioritiesPrefix(EventMassiveCoreLorePriority event)
	{
		for (Entry<String, Integer> prefixEntry : MassiveCoreMConf.get().lorePrioritiesPrefix.entrySet())
		{
			String prefix = prefixEntry.getKey();
			int priority = prefixEntry.getValue();
			event.setPriorityByPrefix(prefix, priority);
		}
	}

}
