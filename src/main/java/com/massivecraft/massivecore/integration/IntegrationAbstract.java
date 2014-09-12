package com.massivecraft.massivecore.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class IntegrationAbstract implements Integration
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private List<String> targetPluginNames;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public IntegrationAbstract(String... targetPluginNames)
	{
		this.targetPluginNames = new ArrayList<String>(Arrays.asList(targetPluginNames));
	}

	// -------------------------------------------- //
	// OVERRIDES
	// -------------------------------------------- //
	
	@Override
	public List<String> getTargetPluginNames()
	{
		return this.targetPluginNames;
	}
	
	@Override
	public void activate()
	{
		
	}

	@Override
	public void deactivate()
	{
		
	}
	
}
