package com.massivecraft.massivecore.sender;

import org.bukkit.permissions.ServerOperator;

public class BasicServerOperator implements ServerOperator
{
	private String name;
	private boolean op;
	private boolean changeable;
	
	public BasicServerOperator(String name, boolean op, boolean opChangeable)
	{
		this.name = name;
		this.op = op;
		this.changeable = opChangeable;
	}
	
	@Override
	public boolean isOp()
	{
		return this.op;
	}

	@Override
	public void setOp(boolean value)
	{
		if (!this.changeable)
		{
			throw new UnsupportedOperationException("Cannot change operator status for "+this.name);
		}
		this.op = value;
	}
}
