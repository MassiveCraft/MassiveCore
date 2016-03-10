package com.massivecraft.massivecore.nms;

import java.lang.reflect.Method;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;

public class NmsItem extends NmsAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsItem i = new NmsItem();
	public static NmsItem get () { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Handling tooltips
	private static Method toNms;
	private static Method toJson;
	private static Class<?> cbItem;
	private static Class<?> nmsItem;
	private static Class<?> nbtTag;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getRequiredVersion()
	{
		return 8;
	}
	
	@Override
	protected void setup() throws Throwable
	{
		cbItem = PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack");
		nmsItem = PackageType.MINECRAFT_SERVER.getClass("ItemStack");
		nbtTag = PackageType.MINECRAFT_SERVER.getClass("NBTTagCompound");
		
		toNms = ReflectionUtils.getMethod(cbItem, "asNMSCopy", ItemStack.class);
		toJson = ReflectionUtils.getMethod(nmsItem, "save", nbtTag);
		
		// Set accessible
		toNms.setAccessible(true);
		toJson.setAccessible(true);
	}
	
	
	public static String itemToString(ItemStack item)
	{
		if (item == null) throw new NullPointerException("item");
		
		if ( ! get().isAvailable()) return null;
		try
		{
			Object nmsItem = toNms.invoke(null, item);
			if (nmsItem == null) throw new RuntimeException(item.toString());
			String str = toJson.invoke(nmsItem, nbtTag.newInstance()).toString();
			return str;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
}
