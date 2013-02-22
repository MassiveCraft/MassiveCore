package com.massivecraft.mcore.integration;

import java.util.List;

public interface IntegrationFeatures
{
	public List<String> getTargetPluginNames();
	public void activate();
	public void deactivate();
}
