package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;

public class WriterItemStackMetaFireworkEffects extends WriterAbstractItemStackMetaField<FireworkMeta, List<DataFireworkEffect>, List<FireworkEffect>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaFireworkEffects i = new WriterItemStackMetaFireworkEffects();
	public static WriterItemStackMetaFireworkEffects get() { return i; }
	public WriterItemStackMetaFireworkEffects()
	{
		super(FireworkMeta.class);
		this.setMaterial(Material.FIREWORK);
		this.setConverterTo(ConverterToFireworkEffects.get());
		this.setConverterFrom(ConverterFromFireworkEffects.get());
		this.addDependencyClasses(
			WriterFireworkEffect.class
		);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public List<DataFireworkEffect> getA(DataItemStack ca, ItemStack d)
	{
		return ca.getFireworkEffects();
	}
	
	@Override
	public void setA(DataItemStack ca, List<DataFireworkEffect> fa, ItemStack d)
	{
		ca.setFireworkEffects(fa);
	}
	
	@Override
	public List<FireworkEffect> getB(FireworkMeta cb, ItemStack d)
	{
		return cb.getEffects();
	}
	
	@Override
	public void setB(FireworkMeta cb, List<FireworkEffect> fb, ItemStack d)
	{
		cb.addEffects(fb);
	}
	
}
