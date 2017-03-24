package com.massivecraft.massivecore;

public interface Active
{
	// Boolean
	boolean isActive();
	void setActive(boolean active);
	
	// Plugin
	MassivePlugin setActivePlugin(MassivePlugin plugin);
	MassivePlugin getActivePlugin();
	
	// Combined Setter
	// Plugin is set first.
	// Boolean by null state.
	void setActive(MassivePlugin plugin);
	
}
