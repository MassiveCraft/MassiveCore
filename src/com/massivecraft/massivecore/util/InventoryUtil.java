package com.massivecraft.massivecore.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.mixin.Mixin;

public class InventoryUtil
{
	// -------------------------------------------- //
	// UPDATES
	// -------------------------------------------- //
	
	public static void update(HumanEntity human)
	{
		if (MUtil.isntPlayer(human)) return;
		Player player = (Player)human;
		player.updateInventory();
	}
	
	public static void updateSoon(final HumanEntity human)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				update(human);
			}
		});
	}
	
	public static void updateLater(final HumanEntity human)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				update(human);
			}
		}, 1);
	}
	
	// -------------------------------------------- //
	// EVENT INTERPRETATION
	// -------------------------------------------- //
	
	public static boolean isOutside(int rawSlot)
	{
		return rawSlot < 0; 
	}
	public static boolean isTopInventory(int rawSlot, Inventory inventory)
	{
		if (isOutside(rawSlot)) return false;
		return rawSlot < inventory.getSize();
	}
	public static boolean isBottomInventory(int rawSlot, Inventory inventory)
	{
		if (isOutside(rawSlot)) return false;
		return rawSlot >= inventory.getSize();
	}
	
	public static boolean isOutside(InventoryClickEvent event)
	{
		return isOutside(event.getRawSlot());
	}
	public static boolean isTopInventory(InventoryClickEvent event)
	{
		return isTopInventory(event.getRawSlot(), event.getInventory());
	}
	public static boolean isBottomInventory(InventoryClickEvent event)
	{
		return isBottomInventory(event.getRawSlot(), event.getInventory());
	}
	
	@Deprecated
	public static boolean isGiving(InventoryClickEvent event)
	{
		return getAlter(event).isGiving();
	}
	
	@Deprecated
	public static boolean isTaking(InventoryClickEvent event)
	{
		return getAlter(event).isTaking();
	}
	
	public static boolean isAltering(InventoryClickEvent event)
	{
		return getAlter(event).isAltering();
	}
	
	public static InventoryAlter getAlter(InventoryClickEvent event)
	{
		if (isOutside(event)) return InventoryAlter.NONE;
		boolean topClicked = isTopInventory(event);
		InventoryAction action = event.getAction();
		
		if (topClicked)
		{
			switch (action)
			{
				// TODO
				case UNKNOWN:
					break;
				
				// Possibly both
				case HOTBAR_SWAP:
					ItemStack hotbar = event.getView().getBottomInventory().getItem(event.getHotbarButton());
					ItemStack current = event.getCurrentItem();
					boolean give = isSomething(hotbar);
					boolean take = isSomething(current);
					
					return getAlter(give, take);
					
				// Neither give nor take
				case NOTHING: return InventoryAlter.NONE;
				case CLONE_STACK: return InventoryAlter.NONE;
				case DROP_ALL_CURSOR: return InventoryAlter.NONE;
				case DROP_ALL_SLOT: return InventoryAlter.NONE;
				case DROP_ONE_CURSOR: return InventoryAlter.NONE;
				case DROP_ONE_SLOT: return InventoryAlter.NONE;
	
				// Take
				case PICKUP_ALL: return InventoryAlter.TAKE;
				case PICKUP_HALF: return InventoryAlter.TAKE;
				case PICKUP_ONE: return InventoryAlter.TAKE;
				case PICKUP_SOME: return InventoryAlter.TAKE;
				case MOVE_TO_OTHER_INVENTORY: return InventoryAlter.TAKE;
				case COLLECT_TO_CURSOR:return InventoryAlter.TAKE;
				case HOTBAR_MOVE_AND_READD: return InventoryAlter.TAKE;
				
				// Give
				case PLACE_ALL: return InventoryAlter.GIVE;
				case PLACE_ONE: return InventoryAlter.GIVE;
				case PLACE_SOME: return InventoryAlter.GIVE;
				case SWAP_WITH_CURSOR: return InventoryAlter.BOTH;

			}
			throw new RuntimeException("Unsupported action: " + action);
		}
		else
		{
			if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) return InventoryAlter.GIVE;
			
			// This one will possibly take, but we cannot be 100% sure.
			// We will return TAKE for security reasons.
			if (action == InventoryAction.COLLECT_TO_CURSOR) return InventoryAlter.TAKE;
	
			return InventoryAlter.NONE;
		}
	}
	
	private static InventoryAlter getAlter(boolean give, boolean take)
	{
		if (give && take) return InventoryAlter.BOTH;
		if (give) return InventoryAlter.GIVE;
		if (take) return InventoryAlter.TAKE;
		return InventoryAlter.NONE;
	}
	
	public enum InventoryAlter
	{
		GIVE,
		TAKE,
		NONE,
		BOTH,
		
		;
		
		public boolean isAltering()
		{
			return this != NONE;
		}
		public boolean isGiving()
		{
			return this == GIVE || this == BOTH;
		}
		public boolean isTaking()
		{
			return this == TAKE || this == BOTH;
		}
	}
	
	/**
	 * This method will return the ItemStack the player is trying to equip.
	 * If the click event would not result in equipping something null will be returned.
	 * Note that this algorithm is not perfect. It's an adequate guess.
	 * 
	 * @param event The InventoryClickEvent to analyze.
	 * @return The ItemStack the player is trying to equip.
	 */
	public static ItemStack isEquipping(InventoryClickEvent event)
	{
		boolean isShiftClick = event.isShiftClick();
		InventoryType inventoryType = event.getInventory().getType();
		SlotType slotType = event.getSlotType();
		ItemStack cursor = event.getCursor();
		ItemStack currentItem = event.getCurrentItem();
		
		if (isShiftClick)
		{
			if (inventoryType != InventoryType.CRAFTING) return null;
			if (slotType == SlotType.CRAFTING) return null;
			if (slotType == SlotType.ARMOR) return null;
			if (slotType == SlotType.RESULT) return null;
			if (currentItem.getType() == Material.AIR) return null;
			return currentItem;
		}
		else
		{
			if (slotType == SlotType.ARMOR)
			{
				return cursor;
			}
			return null;
		}
	}
	
	// -------------------------------------------- //
	// DEBUG
	// -------------------------------------------- //
	
	public static void debug(InventoryClickEvent event)
	{
		System.out.println("===== DEBUG START =====");
		System.out.println("event.getAction() " + event.getAction());
		System.out.println("event.isLeftClick() " + event.isLeftClick());
		System.out.println("event.isRightClick() " + event.isRightClick());
		System.out.println("event.isShiftClick() " + event.isShiftClick());
		System.out.println("event.getClick() " + event.getClick());
		System.out.println("event.getCurrentItem() " + event.getCurrentItem());
		System.out.println("event.getCursor() " + event.getCursor());
		System.out.println("event.getHotbarButton() " + event.getHotbarButton());
		System.out.println("getInventory().getType() "+event.getInventory().getType());
		System.out.println("event.getRawSlot() " + event.getRawSlot());
		System.out.println("event.getResult() " + event.getResult());
		System.out.println("event.getSlot() " + event.getSlot());
		System.out.println("event.getSlotType() " + event.getSlotType());
		System.out.println("getView().getTopInventory().getType() "+event.getView().getTopInventory().getType());
		System.out.println("getView().getType() "+event.getView().getType());
		System.out.println("getView().getBottomInventory().getType() "+event.getView().getBottomInventory().getType());
		System.out.println("event.getWhoClicked() " + event.getWhoClicked());
		System.out.println("-----");
		System.out.println("isOutside(event) " + isOutside(event));
		System.out.println("isTopInventory(event) " + isTopInventory(event));
		System.out.println("isBottomInventory(event) " + isBottomInventory(event));
		System.out.println("getAlter(event) " + getAlter(event));
		System.out.println("isAltering(event) " + isAltering(event));
		System.out.println("isEquipping(event) " + isEquipping(event));
		System.out.println("===== DEBUG END =====");
	}
	
	// -------------------------------------------- //
	// IS EMPTY?
	// -------------------------------------------- //
	
	public static boolean isEmpty(Inventory inv)
	{
		if (inv == null) return true;
		
		for (ItemStack itemStack : inv.getContents())
		{
			if (isSomething(itemStack)) return false;
		}
		
		if (inv instanceof PlayerInventory)
		{
			PlayerInventory pinv = (PlayerInventory)inv;
			
			if (isSomething(pinv.getHelmet())) return false;
			if (isSomething(pinv.getChestplate())) return false;
			if (isSomething(pinv.getLeggings())) return false;
			if (isSomething(pinv.getBoots())) return false;
		}
		
		return true;
	}
	
	// -------------------------------------------- //
	// TYPE CHECKING
	// -------------------------------------------- //
	
	public static boolean isNothing(ItemStack itemStack)
	{
		if (itemStack == null) return true;
		if (itemStack.getAmount() == 0) return true;
		if (itemStack.getType() == Material.AIR) return true;
		return false;
	}
	
	public static boolean isSomething(ItemStack itemStack)
	{
		return !isNothing(itemStack);
	}
	
	public static void repair(ItemStack itemStack)
	{
		// Check Null
		if (isNothing(itemStack)) return;
		
		// Check Repairable
		Material material = itemStack.getType();
		if ( ! isRepairable(material)) return;
		
		// Repair
		itemStack.setDurability((short) 0);
	}
	
	public static boolean isRepairable(Material material)
	{
		// Blocks are never repairable.
		// Only items take damage in Minecraft.
		if (material.isBlock()) return false;
		
		// This list was created by checking for the "B" notation on:
		// http://minecraft.gamepedia.com/Data_values
		if (material == Material.COAL) return false;
		if (material == Material.GOLDEN_APPLE) return false;
		if (material == Material.RAW_FISH) return false;
		if (material == Material.COOKED_FISH) return false;
		if (material == Material.INK_SACK) return false;
		if (material == Material.MAP) return false;
		if (material == Material.POTION) return false;
		if (material == Material.MONSTER_EGG) return false;
		if (material == Material.SKULL_ITEM) return false;
		
		// This lines actually catches most of the specific lines above.
		// However we add this in anyways for future compatibility.
		if ( ! material.getData().equals(MaterialData.class)) return false;
		
		// We may also not repair things that can not take any damage.
		// NOTE: MaxDurability should be renamed to MaxDamage.
		if (material.getMaxDurability() == 0) return false;
		
		// Otherwise repairable
		return true;
	}
	
	// -------------------------------------------- //
	// CLONE ITEMSTACKS/INVENTORY
	// -------------------------------------------- //
	
	public static ItemStack cloneItemStack(ItemStack itemStack)
	{
		if (itemStack == null) return null;
		return new ItemStack(itemStack);
	}
	
	public static ItemStack[] cloneItemStacks(ItemStack[] itemStacks)
	{
		ItemStack[] ret = new ItemStack[itemStacks.length];
		for (int i = 0; i < itemStacks.length; i++)
		{
			ItemStack stack = itemStacks[i];
			if (stack == null) continue;
			ret[i] = cloneItemStack(itemStacks[i]);
		}
		return ret;
	}
	
	public static Inventory cloneInventory(Inventory inventory)
	{
		if (inventory == null) return null;
		
		Inventory ret = null;
		
		int size = inventory.getSize();
		InventoryHolder holder = inventory.getHolder();
		String title = inventory.getTitle();
		
		if (inventory instanceof PlayerInventory)
		{
			PlayerInventory pret = Mixin.createPlayerInventory();
			ret = pret;
			
			PlayerInventory pinventory = (PlayerInventory)inventory;
			
			pret.setHelmet(pinventory.getHelmet() == null ? null : new ItemStack(pinventory.getHelmet()));
			pret.setChestplate(pinventory.getChestplate() == null ? null : new ItemStack(pinventory.getChestplate()));
			pret.setLeggings(pinventory.getLeggings() == null ? null : new ItemStack(pinventory.getLeggings()));
			pret.setBoots(pinventory.getBoots() == null ? null : new ItemStack(pinventory.getBoots()));
		}
		else
		{
			ret = Mixin.createInventory(holder, size, title);
		}
		
		ItemStack[] contents = cloneItemStacks(inventory.getContents());
		ret.setContents(contents);
		
		return ret;
	}
	
	public static PlayerInventory cloneInventory(PlayerInventory inventory)
	{
		return (PlayerInventory)cloneInventory((Inventory)inventory);
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //

	public static boolean equals(ItemStack one, ItemStack two)
	{
		if (isNothing(one)) return isNothing(two);
		if (isNothing(two)) return false;
		return one.equals(two);
	}
	
	public static boolean equals(ItemStack[] one, ItemStack[] two)
	{
		if (one == null) return two == null;
		if (two == null) return false;
		if (one.length != two.length) return false;
		for (int i = 0; i < one.length; i++)
		{
			if (!equals(one[i], two[i])) return false;
		}
		return true;
	}
	
	public static boolean equals(Inventory one, Inventory two)
	{
		if (one == null) return two == null;
		if (two == null) return false;
		if (!equals(one.getContents(), two.getContents())) return false;
		if (one instanceof PlayerInventory)
		{
			PlayerInventory pone = (PlayerInventory)one;
			if (two instanceof PlayerInventory)
			{
				PlayerInventory ptwo = (PlayerInventory)two;
				return equals(pone.getArmorContents(), ptwo.getArmorContents());
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	// -------------------------------------------- //
	// SET CONTENT
	// -------------------------------------------- //
	// This one simply moves the content pointers from on inventory to another.
	// You may want to clone the from inventory first.
	
	public static void setAllContents(Inventory from, Inventory to)
	{
		to.setContents(from.getContents());
		if (from instanceof PlayerInventory)
		{
			PlayerInventory pfrom = (PlayerInventory)from;
			if (to instanceof PlayerInventory)
			{
				PlayerInventory pto = (PlayerInventory)to;
				
				pto.setHelmet(pfrom.getHelmet());
				pto.setChestplate(pfrom.getChestplate());
				pto.setLeggings(pfrom.getLeggings());
				pto.setBoots(pfrom.getBoots());
			}
		}
	}
	
	// -------------------------------------------- //
	// CAN I ADD MANY?
	// -------------------------------------------- //
	
	// Calculate how many times you could add this item to the inventory.
	// NOTE: This method does not alter the inventory.
	public static int roomLeft(Inventory inventory, ItemStack item, int limit)
	{
		inventory = cloneInventory(inventory);
		int ret = 0;
		while(limit <= 0 || ret < limit)
		{
			HashMap<Integer, ItemStack> result = inventory.addItem(item.clone());
			if (result.size() != 0) return ret;
			ret++;
		}
		return ret;
	}
	
	// NOTE: Use the roomLeft method first to ensure this method would succeed
	public static void addItemTimes(Inventory inventory, ItemStack item, int times)
	{
		for (int i = 0 ; i < times ; i++)
		{
			inventory.addItem(item.clone());
		}
	}
	
	// -------------------------------------------- //
	// COUNT
	// -------------------------------------------- //
	
	public static int countSimilar(Inventory inventory, ItemStack itemStack)
	{
		int ret = 0;
		for (ItemStack item : inventory.getContents())
		{
			if (item == null) continue;
			if (!item.isSimilar(itemStack)) continue;
			ret += item.getAmount();
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// GETTERS AND SETTERS
	// -------------------------------------------- //
	
	// META
	
	@SuppressWarnings("unchecked")
	public static <T extends ItemMeta> T getMeta(ItemStack item)
	{
		if (item == null) return null;
		if ( ! item.hasItemMeta()) return null;
		return (T) item.getItemMeta();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ItemMeta> T createMeta(ItemStack item)
	{
		if (item == null) return null;
		return (T) item.getItemMeta();
	}
	
	// DISPLAY NAME
	
	public static String getDisplayName(ItemStack item)
	{
		ItemMeta meta = getMeta(item);
		if (meta == null) return null;
		
		if ( ! meta.hasDisplayName()) return null;
		return meta.getDisplayName();
	}
	
	public static void setDisplayName(ItemStack item, String displayName)
	{
		ItemMeta meta = createMeta(item);
		if (meta == null) return;
		
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
	}
	
	public static boolean isDisplayName(ItemStack item, String displayName)
	{
		String value = getDisplayName(item);
		return MUtil.equals(value, displayName);
	}
	
	// LORE
	
	public static List<String> getLore(ItemStack item)
	{
		ItemMeta meta = getMeta(item);
		if (meta == null) return null;
		
		if ( ! meta.hasLore()) return null;
		return meta.getLore();
	}
	
	public static void setLore(ItemStack item, Collection<String> lore)
	{
		ItemMeta meta = createMeta(item);
		if (meta == null) return;
		
		meta.setLore(lore == null ? null : new MassiveList<>(lore));
		item.setItemMeta(meta);
	}
	
	public static void setLore(ItemStack item, String... lore)
	{
		setLore(item, Arrays.asList(lore));
	}

}
