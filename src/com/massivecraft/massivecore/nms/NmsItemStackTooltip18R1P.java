package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class NmsItemStackTooltip18R1P extends NmsItemStackTooltip
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsItemStackTooltip18R1P i = new NmsItemStackTooltip18R1P();
	public static NmsItemStackTooltip18R1P get () { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// org.bukkit.craftbukkit.inventory.CraftItemStack
	private Class<?> classCraftItemStack;
	
	// net.minecraft.server.ItemStack
	private Class<?> classNmsItemStack;
	
	// net.minecraft.serverNBTTagCompound
	private Class<?> classNmsNbtTagCompound;
	
	// org.bukkit.craftbukkit.inventory.CraftItemStack#asNmsCopy(net.minecraft.server.ItemStack)
	private Method methodCraftItemStackAsNmsCopy;
	
	// net.minecraft.server.ItemStack#save(net.minecraft.serverNBTTagCompound)
	private Method methodNmsItemStackSave;
	
	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	
	@Override
	public Class<ArmorStand> provoke() throws Throwable
	{
		// Demand 1.8
		// The rich chat system with clickables and tooltips were added in Minecraft 1.8.
		// At Minecraft 1.7 the reflection in setup will succeed.
		// The returned String is however an inferior and incompatible version.
		return ArmorStand.class;
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		this.classCraftItemStack = PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack");
		this.classNmsItemStack = PackageType.MINECRAFT_SERVER.getClass("ItemStack");
		this.classNmsNbtTagCompound = PackageType.MINECRAFT_SERVER.getClass("NBTTagCompound");
		
		this.methodCraftItemStackAsNmsCopy = ReflectionUtil.getMethod(this.classCraftItemStack, "asNMSCopy", ItemStack.class);
		this.methodNmsItemStackSave = ReflectionUtil.getMethod(this.classNmsItemStack, "save", classNmsNbtTagCompound);
	}
	
	// -------------------------------------------- //
	// TOOLTIP
	// -------------------------------------------- //
	
	@Override
	public String getNbtStringTooltip(ItemStack item)
	{
		Object nmsItemStack = ReflectionUtil.invokeMethod(this.methodCraftItemStackAsNmsCopy, null, item);
		Object nbtTagCompound = ReflectionUtil.newInstance(this.classNmsNbtTagCompound);
		nbtTagCompound = ReflectionUtil.invokeMethod(this.methodNmsItemStackSave, nmsItemStack, nbtTagCompound);
		return nbtTagCompound.toString();
	}
	
}
