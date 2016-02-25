package com.massivecraft.massivecore;

public interface Active
{
	public boolean isActive();
	public void setActive(boolean active);
	
	public MassivePlugin setActivePlugin(MassivePlugin plugin);
	public MassivePlugin getActivePlugin();
	
}
