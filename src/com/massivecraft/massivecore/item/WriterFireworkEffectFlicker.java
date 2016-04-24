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
	public Boolean getA(DataFireworkEffect ca)
	{
		return ca.hasFlicker();
	}
	
	@Override
	public void setA(DataFireworkEffect ca, Boolean fa)
	{
		ca.setFlicker(fa);
	}
	
	@Override
	public Boolean getB(FireworkEffect cb)
	{
		return cb.hasFlicker();
	}
	
}
