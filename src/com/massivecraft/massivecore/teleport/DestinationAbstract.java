package com.massivecraft.massivecore.teleport;

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
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean hasPs()
	{
		return this.getPs() != null;
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
		return PSFormatHumanSpace.get().format(this.getPs());
	}
	
	@Override
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
}
