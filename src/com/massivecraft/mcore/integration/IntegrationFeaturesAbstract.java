package com.massivecraft.mcore.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class IntegrationFeaturesAbstract implements IntegrationFeatures
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private List<String> targetPluginNames;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public IntegrationFeaturesAbstract(String... targetPluginNames)
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