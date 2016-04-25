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
	public Boolean getA(DataFireworkEffect ca, Object d)
	{
		return ca.hasTrail();
	}
	
	@Override
	public void setA(DataFireworkEffect ca, Boolean fa, Object d)
	{
		ca.setTrail(fa);
	}
	
	@Override
	public Boolean getB(FireworkEffect cb, Object d)
	{
		return cb.hasTrail();
	}
	
}
