package com.massivecraft.massivecore.item;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class WriterItemStackMetaStoredEnchants extends WriterAbstractItemMeta<EnchantmentStorageMeta, Map<Integer, Integer>, Map<Enchantment, Integer>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaStoredEnchants i = new WriterItemStackMetaStoredEnchants();
	public static WriterItemStackMetaStoredEnchants get() { return i; }
	public WriterItemStackMetaStoredEnchants()
	{
		this.setMaterial(Material.ENCHANTED_BOOK);
		this.setConverterTo(ConverterToEnchants.get());
		this.setConverterFrom(ConverterFromEnchants.get());
	}

	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Map<Integer, Integer> getA(DataItemStack ca)
	{
		return ca.getStoredEnchants();
	}
	
	@Override
	public void setA(DataItemStack ca, Map<Integer, Integer> fa)
	{
		ca.setStoredEnchants(fa);
	}
	
	@Override
	public Map<Enchantment, Integer> getB(EnchantmentStorageMeta cb)
	{
		return cb.getStoredEnchants();
	}
	
	@Override
	public void setB(EnchantmentStorageMeta cb, Map<Enchantment, Integer> fb)
	{
		for (Entry<Enchantment, Integer> entry : fb.entrySet())
		{
			cb.addStoredEnchant(entry.getKey(), entry.getValue(), true);
		}
	}
	
}
