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
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final int SIZE_PLAYER_STORAGE = 36;
	public static final int SIZE_PLAYER_ARMOR = 4;
	public static final int SIZE_PLAYER_EXTRA = 1;
	public static final int SIZE_PLAYER_ALL = SIZE_PLAYER_STORAGE + SIZE_PLAYER_ARMOR + SIZE_PLAYER_EXTRA;
	
	// 0 --> 36 (35 exclusive)
	public static final int INDEX_PLAYER_STORAGE_FROM = 0;
	public static final int INDEX_PLAYER_STORAGE_TO = INDEX_PLAYER_STORAGE_FROM + SIZE_PLAYER_STORAGE;
	
	// 36 --> 40 (39 exclusive)
	public static final int INDEX_PLAYER_ARMOR_FROM = INDEX_PLAYER_STORAGE_TO;
	public static final int INDEX_PLAYER_ARMOR_TO = INDEX_PLAYER_ARMOR_FROM + SIZE_PLAYER_ARMOR;
	
	// 40 --> 41 (40 exclusive)
	public static final int INDEX_PLAYER_EXTRA_FROM = INDEX_PLAYER_ARMOR_TO;
	public static final int INDEX_PLAYER_EXTRA_TO = INDEX_PLAYER_EXTRA_FROM + SIZE_PLAYER_EXTRA;
	
	// 40
	public static final int INDEX_PLAYER_SHIELD = INDEX_PLAYER_EXTRA_FROM;
	
	// -------------------------------------------- //
	// UTILS
	// -------------------------------------------- //

	public static PlayerInventory asPlayerInventory(Inventory inventory)
	{
		return (inventory instanceof PlayerInventory) ? (PlayerInventory)inventory : null;
	}
	
	// This is a modified copyOfRange implementation.
	// Boundary from is inclusive. Boundary to is exclusive. Just like in copyOfRange.
	// It does however return the original when possible.
	public static <T> T[] range(T[] original, int fromInclusive, int toExclusive)
	{
		if (fromInclusive == 0 && toExclusive == original.length) return original;
		return Arrays.copyOfRange(original, fromInclusive, toExclusive);
	}
	
	@SafeVarargs
	public static <T> T[] concat(T[] first, T[]... rest)
	{
		int totalLength = first.length;
		for (T[] array : rest)
		{
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest)
		{
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
	
	// This method is used to clean out inconsistent air entries.
	public static ItemStack clean(ItemStack item)
	{
		if (item == null) return null;
		if (item.getType() == Material.AIR) return null;
		// NOTE: In 1.9 zero quantity is a thing.
		return item;
	}
	
	public static void clean(ItemStack[] items)
	{
		for (int i = 0; i < items.length; i++)
		{
			items[i] = clean(items[i]);
		}
	}
	
	// -------------------------------------------- //
	// SLOTS
	// -------------------------------------------- //
	
	// HELMET
	
	public static ItemStack getHelmet(Inventory inventory)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return null;
		ItemStack ret = playerInventory.getHelmet();
		ret = clean(ret);
		return ret;
	}
	public static void setHelmet(Inventory inventory, ItemStack helmet)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return;
		playerInventory.setHelmet(helmet);
	}
	public static ItemStack getHelmet(HumanEntity human)
	{
		if (human == null) return null;
		return getHelmet(human.getInventory());
	}
	public static void setHelmet(HumanEntity human, ItemStack helmet)
	{
		if (human == null) return;
		setHelmet(human.getInventory(), helmet);
	}
	
	// CHESTPLATE
	
	public static ItemStack getChestplate(Inventory inventory)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return null;
		ItemStack ret = playerInventory.getChestplate();
		ret = clean(ret);
		return ret;
	}
	public static void setChestplate(Inventory inventory, ItemStack chestplate)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return;
		playerInventory.setChestplate(chestplate);
	}
	public static ItemStack getChestplate(HumanEntity human)
	{
		if (human == null) return null;
		return getChestplate(human.getInventory());
	}
	public static void setChestplate(HumanEntity human, ItemStack chestplate)
	{
		if (human == null) return;
		setChestplate(human.getInventory(), chestplate);
	}
	
	// LEGGINGS
	
	public static ItemStack getLeggings(Inventory inventory)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return null;
		ItemStack ret = playerInventory.getLeggings();
		ret = clean(ret);
		return ret;
	}
	public static void setLeggings(Inventory inventory, ItemStack leggings)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return;
		playerInventory.setLeggings(leggings);
	}
	public static ItemStack getLeggings(HumanEntity human)
	{
		if (human == null) return null;
		return getLeggings(human.getInventory());
	}
	public static void setLeggings(HumanEntity human, ItemStack leggings)
	{
		if (human == null) return;
		setLeggings(human.getInventory(), leggings);
	}
	
	// BOOTS
	
	public static ItemStack getBoots(Inventory inventory)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return null;
		ItemStack ret = playerInventory.getBoots();
		ret = clean(ret);
		return ret;
	}
	public static void setBoots(Inventory inventory, ItemStack boots)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return;
		playerInventory.setBoots(boots);
	}
	public static ItemStack getBoots(HumanEntity human)
	{
		if (human == null) return null;
		return getBoots(human.getInventory());
	}
	public static void setBoots(HumanEntity human, ItemStack boots)
	{
		if (human == null) return;
		setBoots(human.getInventory(), boots);
	}
	
	// WEAPON
	
	// NOTE: We make sure to convert AIR into null due to a Bukkit API inconsistency.
	@SuppressWarnings("deprecation")
	public static ItemStack getWeapon(Inventory inventory)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return null;
		ItemStack ret = playerInventory.getItemInHand();
		ret = clean(ret);
		return ret;

	}
	@SuppressWarnings("deprecation")
	public static void setWeapon(Inventory inventory, ItemStack weapon)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return;
		playerInventory.setItemInHand(weapon);
	}
	@SuppressWarnings("deprecation")
	public static ItemStack getWeapon(HumanEntity human)
	{
		if (human == null) return null;
		ItemStack ret = human.getItemInHand();
		ret = clean(ret);
		return ret;
	}
	@SuppressWarnings("deprecation")
	public static void setWeapon(HumanEntity human, ItemStack weapon)
	{
		if (human == null) return;
		human.setItemInHand(weapon);
	}
	
	// SHIELD
	
	public static ItemStack getShield(Inventory inventory)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return null;
		ItemStack[] contents = playerInventory.getContents();
		
		if (contents.length <= INDEX_PLAYER_SHIELD) return null;
		
		ItemStack ret = contents[INDEX_PLAYER_SHIELD];
		ret = clean(ret);
		return ret;
	}
	public static void setShield(Inventory inventory, ItemStack shield)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return;
		ItemStack[] contents = playerInventory.getContents();
		
		if (contents.length <= INDEX_PLAYER_SHIELD) return;
		
		inventory.setItem(INDEX_PLAYER_SHIELD, shield);
	}
	public static ItemStack getShield(HumanEntity human)
	{
		if (human == null) return null;
		return getShield(human.getInventory());
	}
	public static void setShield(HumanEntity human, ItemStack shield)
	{
		if (human == null) return;
		setShield(human.getInventory(), shield);
	}
	
	// -------------------------------------------- //
	// CONTENTS SECTIONS
	// -------------------------------------------- //
	// When Reading:
	// The content sections NPE evade and aim to behave as the latest Minecraft version.
	// So rather than returning null for getContentsExtra() we create and return a new array.
	//
	// When Writing:
	// ...
	
	// All content varies over versions.
	// Before 1.9 it was getContents() + getArmorContents() + new ItemStack[1].
	// From and including 1.9 it's just getContents().
	public static ItemStack[] getContentsAll(Inventory inventory)
	{
		if (inventory == null) return null;
		ItemStack[] contents = inventory.getContents();
		ItemStack[] ret = contents;
		
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory != null && contents.length == SIZE_PLAYER_STORAGE)
		{
			ret = concat(contents, playerInventory.getArmorContents(), new ItemStack[SIZE_PLAYER_EXTRA]);
		}
		
		clean(ret);
		return ret;
	}
	public static void setContentsAll(Inventory inventory, ItemStack[] all)
	{
		if (inventory == null) return;
		ItemStack[] contents = inventory.getContents();
		
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null)
		{
			inventory.setContents(range(all, 0, contents.length));
			return;
		}
		
		if (all.length < INDEX_PLAYER_STORAGE_TO) return;
		ItemStack[] storage = range(all, INDEX_PLAYER_STORAGE_FROM, INDEX_PLAYER_STORAGE_TO);
		setContentsStorage(playerInventory, storage);
		
		if (all.length < INDEX_PLAYER_ARMOR_TO) return;
		ItemStack[] armor = range(all, INDEX_PLAYER_ARMOR_FROM, INDEX_PLAYER_ARMOR_TO);
		setContentsArmor(playerInventory, armor);
		
		if (all.length < INDEX_PLAYER_EXTRA_TO) return;
		ItemStack[] extra = range(all, INDEX_PLAYER_EXTRA_FROM, INDEX_PLAYER_EXTRA_TO);
		setContentsExtra(playerInventory, extra);
	}
	
	// Storage contents implementation has varied.
	// Before 1.9 it was the same as getContents().
	// From and including 1.9 it became the 36 first of those slots.
	public static ItemStack[] getContentsStorage(Inventory inventory)
	{
		if (inventory == null) return null;
		ItemStack[] contents = inventory.getContents();
		ItemStack[] ret = contents;
		
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory != null)
		{
			ret = range(contents, INDEX_PLAYER_STORAGE_FROM, INDEX_PLAYER_STORAGE_TO);
		}
		
		clean(ret);
		return ret;
	}
	public static void setContentsStorage(Inventory inventory, ItemStack[] storage)
	{
		if (inventory == null) return;
		ItemStack[] contents = inventory.getContents();
		
		// Calculate exclusive maximum
		int max = Math.min(storage.length, contents.length);
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory != null) max = Math.min(max, INDEX_PLAYER_STORAGE_TO);
		
		// Set as much as possible
		for (int i = 0; i < max; i++)
		{
			inventory.setItem(i, storage[i]);
		}
	}
	
	// Armor contents has always been implemented the same way and can be used directly.
	public static ItemStack[] getContentsArmor(Inventory inventory)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return null;
		
		ItemStack[] ret = playerInventory.getArmorContents();
		
		clean(ret);
		return ret;
	}
	public static void setContentsArmor(Inventory inventory, ItemStack[] armor)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return;

		playerInventory.setArmorContents(armor);
	}
	
	// The extra contents was added in 1.9.
	// It is then at the very end of all of the contents.
	// Slot 40 and forward even though it currently is just a single slot.
	public static ItemStack[] getContentsExtra(Inventory inventory)
	{
		PlayerInventory playerInventory = asPlayerInventory(inventory);
		if (playerInventory == null) return null;
		ItemStack[] contents = playerInventory.getContents();
		ItemStack[] ret = new ItemStack[SIZE_PLAYER_EXTRA];
		
		int max = SIZE_PLAYER_EXTRA;
		max = Math.min(max, contents.length - INDEX_PLAYER_EXTRA_FROM);
		
		for (int i = 0; i < max; i++)
		{
			ret[i] = contents[INDEX_PLAYER_EXTRA_FROM + i];
		}
		
		clean(ret);
		return ret;
	}
	public static void setContentsExtra(Inventory intentory, ItemStack[] extra)
	{
		PlayerInventory playerInventory = asPlayerInventory(intentory);
		if (playerInventory == null) return;
		ItemStack[] contents = playerInventory.getContents();
		
		int max = SIZE_PLAYER_EXTRA;
		max = Math.min(max, contents.length - INDEX_PLAYER_EXTRA_FROM);
		max = Math.min(max, extra.length);
		
		for (int i = 0; i < max; i++)
		{
			playerInventory.setItem(INDEX_PLAYER_EXTRA_FROM + i, extra[i]);
		}
	}
	
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
				case DROP_ONE_CURSOR: return InventoryAlter.NONE;
	
				// Take
				case PICKUP_ALL: return InventoryAlter.TAKE;
				case PICKUP_HALF: return InventoryAlter.TAKE;
				case PICKUP_ONE: return InventoryAlter.TAKE;
				case PICKUP_SOME: return InventoryAlter.TAKE;
				case MOVE_TO_OTHER_INVENTORY: return InventoryAlter.TAKE;
				case COLLECT_TO_CURSOR:return InventoryAlter.TAKE;
				case HOTBAR_MOVE_AND_READD: return InventoryAlter.TAKE;
				case DROP_ONE_SLOT: return InventoryAlter.TAKE;
				case DROP_ALL_SLOT: return InventoryAlter.TAKE;
				
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
	
	public static ItemStack clone(ItemStack itemStack)
	{
		if (itemStack == null) return null;
		return new ItemStack(itemStack);
	}
	
	public static ItemStack[] clone(ItemStack[] itemStacks)
	{
		ItemStack[] ret = new ItemStack[itemStacks.length];
		for (int i = 0; i < itemStacks.length; i++)
		{
			ItemStack stack = itemStacks[i];
			if (stack == null) continue;
			ret[i] = clone(itemStacks[i]);
		}
		return ret;
	}
	
	public static Inventory clone(Inventory inventory, boolean playerSupport)
	{
		// Evade
		if (inventory == null) return null;
		
		// Create
		Inventory ret = null;
		if (inventory instanceof PlayerInventory && playerSupport)
		{
			ret = Mixin.createPlayerInventory();
		}
		else
		{
			InventoryHolder holder = inventory.getHolder();
			int size = inventory.getSize();
			if (inventory instanceof PlayerInventory) size = SIZE_PLAYER_STORAGE;
			String title = inventory.getTitle();
			ret = Mixin.createInventory(holder, size, title);
		}
		
		// Fill
		ItemStack[] all = getContentsAll(inventory);
		all = clone(all);
		setContentsAll(ret, all);
		
		// Return
		return ret;
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
		// NOTE: We can not afford to clone player inventories here.
		inventory = clone(inventory, false);
		int ret = 0;
		while (limit <= 0 || ret < limit)
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
