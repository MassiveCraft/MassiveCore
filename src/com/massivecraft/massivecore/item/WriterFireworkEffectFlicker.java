package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;

public class WriterFireworkEffectFlicker extends WriterAbstractFireworkEffect<Boolean, Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterFireworkEffectFlicker i = new WriterFireworkEffectFlicker();
	public static WriterFireworkEffectFlicker get() { return i; }
	public WriterFireworkEffectFlicker()
	{
		super("flicker");
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public Boolean getA(DataFireworkEffect ca, Object d)
	{
		return ca.hasFlicker();
	}
	
	@Override
	public void setA(DataFireworkEffect ca, Boolean fa, Object d)
	{
		ca.setFlicker(fa);
	}
	
	@Override
	public Boolean getB(FireworkEffect cb, Object d)
	{
		return cb.hasFlicker();
	}
	
}
