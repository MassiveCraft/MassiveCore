package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.util.ReflectionUtil;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;

public class NmsItemStackMetaUnbreakableSpigot extends NmsItemStackMetaUnbreakable
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsItemStackMetaUnbreakableSpigot i = new NmsItemStackMetaUnbreakableSpigot();
	public static NmsItemStackMetaUnbreakableSpigot get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	//org.bukkit.inventory.meta.ItemMeta#isUnbreakable()
	private Method bukkitItemMetaIsUnbreakable;
	//org.bukkit.inventory.meta.ItemMeta#setUnbreakable(Boolean)
	private Method bukkitItemMetaSetUnbreakable;
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		bukkitItemMetaIsUnbreakable = ReflectionUtil.getMethod(ItemMeta.class,"isUnbreakable");
		bukkitItemMetaSetUnbreakable = ReflectionUtil.getMethod(ItemMeta.class,"setUnbreakable", boolean.class);
	}
	
	// -------------------------------------------- //
	// ACCESS > UNBREAKABLE
	// -------------------------------------------- //
	
	public boolean isUnbreakable(ItemMeta meta) {
		return ReflectionUtil.invokeMethod(bukkitItemMetaIsUnbreakable, meta);
	}
	
	public void setUnbreakable(ItemMeta meta, boolean unbreakable)
	{
		ReflectionUtil.invokeMethod(bukkitItemMetaSetUnbreakable, meta, unbreakable);
	}
	
}
