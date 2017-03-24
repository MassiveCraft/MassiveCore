package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;
import java.util.Map.Entry;

public class WriterItemStackMetaStoredEnchants extends WriterAbstractItemStackMetaField<EnchantmentStorageMeta, Map<Integer, Integer>, Map<Enchantment, Integer>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaStoredEnchants i = new WriterItemStackMetaStoredEnchants();
	public static WriterItemStackMetaStoredEnchants get() { return i; }
	public WriterItemStackMetaStoredEnchants()
	{
		super(EnchantmentStorageMeta.class);
		this.setMaterial(Material.ENCHANTED_BOOK);
		this.setConverterTo(ConverterToEnchants.get());
		this.setConverterFrom(ConverterFromEnchants.get());
	}

	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Map<Integer, Integer> getA(DataItemStack ca, ItemStack d)
	{
		return ca.getStoredEnchants();
	}
	
	@Override
	public void setA(DataItemStack ca, Map<Integer, Integer> fa, ItemStack d)
	{
		ca.setStoredEnchants(fa);
	}
	
	@Override
	public Map<Enchantment, Integer> getB(EnchantmentStorageMeta cb, ItemStack d)
	{
		return cb.getStoredEnchants();
	}
	
	@Override
	public void setB(EnchantmentStorageMeta cb, Map<Enchantment, Integer> fb, ItemStack d)
	{
		for (Entry<Enchantment, Integer> entry : fb.entrySet())
		{
			cb.addStoredEnchant(entry.getKey(), entry.getValue(), true);
		}
	}
	
}
