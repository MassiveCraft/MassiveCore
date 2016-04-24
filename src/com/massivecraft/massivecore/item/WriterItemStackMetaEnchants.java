package com.massivecraft.massivecore.item;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

public class WriterItemStackMetaEnchants extends WriterAbstractItemMeta<ItemMeta, Map<Integer, Integer>, Map<Enchantment, Integer>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaEnchants i = new WriterItemStackMetaEnchants();
	public static WriterItemStackMetaEnchants get() { return i; }
	public WriterItemStackMetaEnchants()
	{
		this.setConverterTo(ConverterToEnchants.get());
		this.setConverterFrom(ConverterFromEnchants.get());
	}

	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Map<Integer, Integer> getA(DataItemStack ca)
	{
		return ca.getEnchants();
	}
	
	@Override
	public void setA(DataItemStack ca, Map<Integer, Integer> fa)
	{
		ca.setEnchants(fa);
	}
	
	@Override
	public Map<Enchantment, Integer> getB(ItemMeta cb)
	{
		return cb.getEnchants();
	}
	
	@Override
	public void setB(ItemMeta cb, Map<Enchantment, Integer> fb)
	{
		for (Entry<Enchantment, Integer> entry : fb.entrySet())
		{
			cb.addEnchant(entry.getKey(), entry.getValue(), true);
		}
	}
	
}
