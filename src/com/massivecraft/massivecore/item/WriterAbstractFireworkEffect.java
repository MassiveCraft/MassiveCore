package com.massivecraft.massivecore.item;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;


public abstract class WriterAbstractFireworkEffect<FA, FB> extends WriterAbstractReflect<DataFireworkEffect, FireworkEffect, DataFireworkEffect, FireworkEffect, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractFireworkEffect(String fieldName)
	{
		super(FireworkEffect.class, fieldName);
	}
	
	public WriterAbstractFireworkEffect()
	{
		this(null); 
	}
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public DataFireworkEffect createOA()
	{
		return new DataFireworkEffect();
	}
	
	@Override
	public FireworkEffect createOB()
	{
		return FireworkEffect.builder().withColor(Color.GREEN).build();
	}
	
}
