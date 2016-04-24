package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;

public class WriterItemStackMetaFireworkEffects extends WriterAbstractItemStackMetaField<FireworkMeta, List<DataFireworkEffect>, List<FireworkEffect>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaFireworkEffects i = new WriterItemStackMetaFireworkEffects();
	public static WriterItemStackMetaFireworkEffects get() { return i; }
	{
		this.setMaterial(Material.FIREWORK);
		this.setConverterTo(ConverterToFireworkEffects.get());
		this.setConverterFrom(ConverterFromFireworkEffects.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public List<DataFireworkEffect> getA(DataItemStack ca)
	{
		return ca.getFireworkEffects();
	}
	
	@Override
	public void setA(DataItemStack ca, List<DataFireworkEffect> fa)
	{
		ca.setFireworkEffects(fa);
	}
	
	@Override
	public List<FireworkEffect> getB(FireworkMeta cb)
	{
		return cb.getEffects();
	}
	
	@Override
	public void setB(FireworkMeta cb, List<FireworkEffect> fb)
	{
		cb.addEffects(fb);
	}
	
}
