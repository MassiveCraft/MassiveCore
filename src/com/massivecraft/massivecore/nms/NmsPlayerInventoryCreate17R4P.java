package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils;
import com.massivecraft.massivecore.util.ReflectionUtil;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Constructor;

public class NmsPlayerInventoryCreate17R4P extends NmsPlayerInventoryCreate
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsPlayerInventoryCreate17R4P i = new NmsPlayerInventoryCreate17R4P();
	public static NmsPlayerInventoryCreate17R4P get () { return i; }

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Class<?> classNmsPlayerInventory;
	private Class<?> classNmsEntityHuman;
	private Constructor<?> constructorNmsPlayerInventory;
	
	private Class<?> classCraftInventoryPlayer;
	private Constructor<?> constructorCraftInventoryPlayer;
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		this.classNmsPlayerInventory = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("PlayerInventory");
		this.classNmsEntityHuman = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EntityHuman");
		this.constructorNmsPlayerInventory = ReflectionUtils.getConstructor(this.classNmsPlayerInventory, this.classNmsEntityHuman);
		
		this.classCraftInventoryPlayer = ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftInventoryPlayer");
		this.constructorCraftInventoryPlayer = ReflectionUtils.getConstructor(this.classCraftInventoryPlayer, this.classNmsPlayerInventory);
	}
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public PlayerInventory create()
	{
		Object nmsInventory = ReflectionUtil.invokeConstructor(this.constructorNmsPlayerInventory, (Object)null);
		return ReflectionUtil.invokeConstructor(this.constructorCraftInventoryPlayer, nmsInventory);
	}
	
}
