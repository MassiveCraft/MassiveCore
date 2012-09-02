package com.massivecraft.mcore4.store;

public class USelRule
{
	protected final String name;
	public String name() { return this.name; }
	
	protected final String param;
	public String param() { return this.param; }
	
	public USelRule()
	{
		this.name = null;
		this.param = null;
	}
	
	public USelRule(String name, String param)
	{
		this.name = name;
		this.param = param;
	}
}
