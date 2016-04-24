package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

public class WriterFireworkEffectType extends WriterAbstractFireworkEffect<String, Type>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterFireworkEffectType i = new WriterFireworkEffectType();
	public static WriterFireworkEffectType get() { return i; }
	public WriterFireworkEffectType()
	{
		super("type");
		this.setConverterTo(ConverterToFireworkEffectType.get());
		this.setConverterFrom(ConverterFromFireworkEffectType.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public String getA(DataFireworkEffect ca)
	{
		return ca.getType();
	}
	
	@Override
	public void setA(DataFireworkEffect ca, String fa)
	{
		ca.setType(fa);
	}
	
	@Override
	public Type getB(FireworkEffect cb)
	{
		return cb.getType();
	}
	
}
