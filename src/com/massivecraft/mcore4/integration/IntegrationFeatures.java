package com.massivecraft.mcore4.integration;

public interface IntegrationFeatures
{
	public String getTargetPluginName();
	public void activate();
	public void deactivate();
}
