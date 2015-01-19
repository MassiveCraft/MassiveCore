package com.massivecraft.massivecore.teleport;

public abstract class PSGetterAbstract implements PSGetter
{
	private static final long serialVersionUID = 1L;

	public boolean hasPS()
	{
		return this.getPS() != null;
	}
}
