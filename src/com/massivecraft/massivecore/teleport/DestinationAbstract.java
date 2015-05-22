package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSFormatHumanSpace;
import com.massivecraft.massivecore.util.Txt;

public abstract class DestinationAbstract implements Destination
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected String desc = null;
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public PS getPsInner()
	{
		return null;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PS getPs(Object watcherObject) throws MassiveException
	{
		PS ret = this.getPsInner();
		if (ret == null)
		{
			throw new MassiveException().addMessage(this.getMessagePsNull(watcherObject));
		}
		return ret;
	}
	
	@Override
	public boolean hasPs()
	{
		try
		{
			return this.getPs(null) != null;
		}
		catch (MassiveException e)
		{
			return false;
		}
	}
	
	@Override
	public String getMessagePsNull(Object watcherObject)
	{
		String desc = this.getDesc(watcherObject);
		return Txt.parse("<b>Location for <h>%s<b> could not be found.", desc);
	}
	
	@Override
	public String getDesc(Object watcherObject)
	{
		if (this.desc != null) return this.desc;
		try
		{
			PS ps = this.getPs(watcherObject);
			return PSFormatHumanSpace.get().format(ps);
		}
		catch (MassiveException e)
		{
			return "null";
		}
	}
	
	@Override
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
}
