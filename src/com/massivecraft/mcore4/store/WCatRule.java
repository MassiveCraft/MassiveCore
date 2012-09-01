package com.massivecraft.mcore4.store;

public class WCatRule
{
	protected final String name;
	public String name() { return this.name; }
	
	protected final String param;
	public String param() { return this.param; }
	
	public WCatRule()
	{
		this.name = null;
		this.param = null;
	}
	
	public WCatRule(String name, String param)
	{
		this.name = name;
		this.param = param;
	}
}
