package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.ps.PS;

public final class PSGetterPS extends PSGetterAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final PS ps;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private PSGetterPS(PS ps)
	{
		this.ps = ps;
	}
	
	// -------------------------------------------- //
	// VALUE OF
	// -------------------------------------------- //
	
	public static PSGetterPS valueOf(PS ps)
	{
		return new PSGetterPS(ps);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PS getPS()
	{
		return this.ps;
	}

}
