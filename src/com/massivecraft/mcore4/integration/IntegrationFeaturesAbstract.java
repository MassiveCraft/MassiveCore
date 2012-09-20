package com.massivecraft.mcore4.integration;

public abstract class IntegrationFeaturesAbstract implements IntegrationFeatures
{
	@Override
	public String getTargetPluginName()
	{
		// "SpoutFeatures" --> "Spout"
		String ret = this.getClass().getSimpleName();
		ret = ret.substring(0, ret.length()-8);
		return ret;
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
