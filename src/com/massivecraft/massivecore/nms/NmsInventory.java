package com.massivecraft.massivecore.nms;

import java.lang.reflect.Constructor;

import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils;

public class NmsInventory extends NmsAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsInventory i = new NmsInventory();
	public static NmsInventory get () { return i; }

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private static Class<?> playerInventoryClass;
	private static Class<?> entityHumanClass;
	private static Constructor<?> playerInventoryConstructor;
	private static Class<?> craftInventoryPlayerClass;
	private static Constructor<?> craftInventoryPlayerConstructor;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	protected void setup() throws Throwable
	{
		playerInventoryClass = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("PlayerInventory");
		entityHumanClass = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EntityHuman");
		playerInventoryConstructor = ReflectionUtils.getConstructor(playerInventoryClass, entityHumanClass);
		craftInventoryPlayerClass = ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftInventoryPlayer");
		craftInventoryPlayerConstructor = ReflectionUtils.getConstructor(craftInventoryPlayerClass, playerInventoryClass);
	}
	
	// -------------------------------------------- //
	// CREATE PLAYERINVENTORY
	// -------------------------------------------- //
	
	public static PlayerInventory createPlayerInventory()
	{
		if ( ! get().isAvailable()) return null;
		
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
	
}
