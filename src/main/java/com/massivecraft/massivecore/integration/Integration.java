package com.massivecraft.massivecore.integration;

import java.util.List;

public interface Integration
{
	public List<String> getTargetPluginNames();
	public void activate();
	public void deactivate();
}
