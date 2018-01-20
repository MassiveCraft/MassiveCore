package com.massivecraft.massivecore.store;

public abstract class DriverAbstract implements Driver
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String name;
	@Override
	public String getDriverName()
	{
		return this.name;
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DriverAbstract(String name)
	{
		this.name = name;
	}

}
