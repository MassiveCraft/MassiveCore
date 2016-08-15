package com.massivecraft.massivecore.nms;

import java.lang.reflect.Constructor;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class NmsItemStack extends NmsAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsItemStack i = new NmsItemStack();
	public static NmsItemStack get () { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// net.minecraft.server.ItemStack
	public Class<?> classNmsItemStack;
	
	// org.bukkit.craftbukkit.inventory.CraftItemStack
	public Class<?> classCraftItemStack;
	
	// org.bukkit.craftbukkit.inventory.CraftItemStack(net.minecraft.server.ItemStack)
	public Constructor<?> constructorCraftItemStack;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	// NOTE: This has been properly researched.
	// NOTE: The constructor have been the same for quite some time! 
	@Override
	public int getRequiredVersion()
	{
		return 6;
	}
	
	@Override
	protected void setup() throws Throwable
	{
		this.classNmsItemStack = PackageType.MINECRAFT_SERVER.getClass("ItemStack");
		this.classCraftItemStack = PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack");
		this.constructorCraftItemStack = ReflectionUtil.getConstructor(this.classCraftItemStack, this.classNmsItemStack);
	}
	
	// -------------------------------------------- //
	// METHOD
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public ItemStack createItemStack()
	{
		// Create
		ItemStack ret = null;
		
		// Fill
		if (this.isAvailable())
		{
			ret = ReflectionUtil.invokeConstructor(this.constructorCraftItemStack, (Object)null);
		}
		else
		{
			ret = new ItemStack(0);
		}
		
		// Return
		return ret;
	}
	
}
