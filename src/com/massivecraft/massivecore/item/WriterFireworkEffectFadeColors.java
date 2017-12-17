package com.massivecraft.massivecore.item;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import java.util.List;

public class WriterFireworkEffectFadeColors extends WriterAbstractFireworkEffect<List<Integer>, List<Color>>
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
	public List<Color> getB(FireworkEffect cb, Object d)
	{
		return cb.getFadeColors();
	}
	
}
