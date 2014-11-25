package com.massivecraft.massivecore.mixin;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils;

public class InventoryMixinDefault extends InventoryMixinAbstract
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static InventoryMixinDefault i = new InventoryMixinDefault();
	public static InventoryMixinDefault get() { return i; }

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static Class<?> playerInventoryClass;
	public static Class<?> entityHumanClass;
	public static Constructor<?> playerInventoryConstructor;
	public static Class<?> craftInventoryPlayerClass;
	public static Constructor<?> craftInventoryPlayerConstructor;
	
	static
	{
		try
		{
			playerInventoryClass = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("PlayerInventory");
			entityHumanClass = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EntityHuman");
			playerInventoryConstructor = ReflectionUtils.getConstructor(playerInventoryClass, entityHumanClass);
			craftInventoryPlayerClass = ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftInventoryPlayer");
			craftInventoryPlayerConstructor = ReflectionUtils.getConstructor(craftInventoryPlayerClass, playerInventoryClass);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PlayerInventory createPlayerInventory()
	{
		try
		{
			Object playerInventory = playerInventoryConstructor.newInstance(new Object[]{null});
			Object craftInventoryPlayer = craftInventoryPlayerConstructor.newInstance(playerInventory);
			return (PlayerInventory)craftInventoryPlayer;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		return Bukkit.createInventory(holder, size, title);
	}
	
}
