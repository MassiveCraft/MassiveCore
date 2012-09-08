package com.massivecraft.mcore4.store;

import com.massivecraft.mcore4.MCore;

public class USelColl extends Coll<USel, String>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	public static USelColl i = new USelColl();
	
	private USelColl()
	{
		super(MCore.p, "ai", "mcore_usel", USel.class, String.class, true);
	}
	
	@Override
	public void copy(Object ofrom, Object oto)
	{
		USel from = (USel)ofrom;
		USel to = (USel)oto;
		to.rules = from.rules;
	}
	
	@Override
	public void init()
	{
		super.init();
		
		// Ensure the default WorldCategorizer is present.
		if ( ! this.ids().contains(USel._DEFAULT))
		{
			USel d = this.get(USel._DEFAULT);
			d.rules(USel.DEFAULT_DEFAULT_RULES);
		}
	}
	
	@Override
	public boolean isDefault(USel entity)
	{
		return entity.rules().equals(USel.DEFAULT_RULES);
	}
	
}
