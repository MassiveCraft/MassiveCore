package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import com.google.common.collect.ImmutableList;

public class WriterFireworkEffectFadeColors extends WriterAbstractFireworkEffect<List<Integer>, ImmutableList<Color>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterFireworkEffectFadeColors i = new WriterFireworkEffectFadeColors();
	public static WriterFireworkEffectFadeColors get() { return i; }
	public WriterFireworkEffectFadeColors()
	{
		super("fadeColors");
		this.setConverterTo(ConverterToColors.get());
		this.setConverterFrom(ConverterFromColors.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public List<Integer> getA(DataFireworkEffect ca, Object d)
	{
		return ca.getFadeColors();
	}
	
	@Override
	public void setA(DataFireworkEffect ca, List<Integer> fa, Object d)
	{
		ca.setFadeColors(fa);
	}
	
	@Override
	public ImmutableList<Color> getB(FireworkEffect cb, Object d)
	{
		return (ImmutableList<Color>) cb.getFadeColors();
	}
	
}
