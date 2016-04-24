package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;

public class WriterFireworkEffectTrail extends WriterAbstractFireworkEffect<Boolean, Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterFireworkEffectTrail i = new WriterFireworkEffectTrail();
	public static WriterFireworkEffectTrail get() { return i; }
	public WriterFireworkEffectTrail()
	{
		super("trail");
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public Boolean getA(DataFireworkEffect ca)
	{
		return ca.hasTrail();
	}
	
	@Override
	public void setA(DataFireworkEffect ca, Boolean fa)
	{
		ca.setTrail(fa);
	}
	
	@Override
	public Boolean getB(FireworkEffect cb)
	{
		return cb.hasTrail();
	}
	
}
