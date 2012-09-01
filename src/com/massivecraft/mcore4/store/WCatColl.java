package com.massivecraft.mcore4.store;

import com.massivecraft.mcore4.MCore;

public class WCatColl extends Coll<WCat, String>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	public static WCatColl i = new WCatColl();
	
	private WCatColl()
	{
		super(MCore.p, "ai", "mcore_wcat", WCat.class, String.class, true);
	}
	
	@Override
	public void copy(Object ofrom, Object oto)
	{
		WCat from = (WCat)ofrom;
		WCat to = (WCat)oto;
		to.rules = from.rules;
	}
	
	@Override
	public void init()
	{
		super.init();
		
		// Ensure the default WorldCategorizer is present.
		WCat d = this.get(WCat._DEFAULT);
		d.rules(WCat.DEFAULT_DEFAULT_RULES);
	}
	
	@Override
	public boolean isDefault(WCat entity)
	{
		return entity.rules().equals(WCat.DEFAULT_RULES);
	}
	
}
