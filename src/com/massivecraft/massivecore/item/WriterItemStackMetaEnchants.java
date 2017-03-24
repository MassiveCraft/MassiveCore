package com.massivecraft.massivecore.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Map.Entry;

public class WriterItemStackMetaEnchants extends WriterAbstractItemStackMetaField<ItemMeta, Map<Integer, Integer>, Map<Enchantment, Integer>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaEnchants i = new WriterItemStackMetaEnchants();
	public static WriterItemStackMetaEnchants get() { return i; }
	public WriterItemStackMetaEnchants()
	{
		super(ItemMeta.class);
		this.setConverterTo(ConverterToEnchants.get());
		this.setConverterFrom(ConverterFromEnchants.get());
	}

	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Map<Integer, Integer> getA(DataItemStack ca, ItemStack d)
	{
		return ca.getEnchants();
	}
	
	@Override
	public void setA(DataItemStack ca, Map<Integer, Integer> fa, ItemStack d)
	{
		ca.setEnchants(fa);
	}
	
	@Override
	public Map<Enchantment, Integer> getB(ItemMeta cb, ItemStack d)
	{
		return cb.getEnchants();
	}
	
	@Override
	public void setB(ItemMeta cb, Map<Enchantment, Integer> fb, ItemStack d)
	{
		for (Entry<Enchantment, Integer> entry : fb.entrySet())
		{
			cb.addEnchant(entry.getKey(), entry.getValue(), true);
		}
	}
	
}
