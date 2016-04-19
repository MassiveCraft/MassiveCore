package com.massivecraft.massivecore;

public interface Active
{
	// Boolean
	public boolean isActive();
	public void setActive(boolean active);
	
	// Plugin
	public MassivePlugin setActivePlugin(MassivePlugin plugin);
	public MassivePlugin getActivePlugin();
	
	// Combined Setter
	// Plugin is set first.
	// Boolean by null state.
	public void setActive(MassivePlugin plugin);
	
}
