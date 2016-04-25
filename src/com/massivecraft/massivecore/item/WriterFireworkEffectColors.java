package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import com.google.common.collect.ImmutableList;

public class WriterFireworkEffectColors extends WriterAbstractFireworkEffect<List<Integer>, ImmutableList<Color>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterFireworkEffectColors i = new WriterFireworkEffectColors();
	public static WriterFireworkEffectColors get() { return i; }
	public WriterFireworkEffectColors()
	{
		super("colors");
		this.setConverterTo(ConverterToColors.get());
		this.setConverterFrom(ConverterFromColors.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public List<Integer> getA(DataFireworkEffect ca, Object d)
	{
		return ca.getColors();
	}
	
	@Override
	public void setA(DataFireworkEffect ca, List<Integer> fa, Object d)
	{
		ca.setColors(fa);
	}
	
	@Override
	public ImmutableList<Color> getB(FireworkEffect cb, Object d)
	{
		return (ImmutableList<Color>) cb.getColors();
	}
	
}
