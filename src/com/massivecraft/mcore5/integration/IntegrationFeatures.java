package com.massivecraft.mcore5.integration;

public interface IntegrationFeatures
{
	public String getTargetPluginName();
	public void activate();
	public void deactivate();
}
