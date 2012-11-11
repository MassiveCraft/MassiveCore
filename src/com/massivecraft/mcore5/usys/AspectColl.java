package com.massivecraft.mcore5.usys;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.store.Coll;

public class AspectColl extends Coll<Aspect, String>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static AspectColl i = new AspectColl();
	
	private AspectColl()
	{
		super(MCore.p, "ai", "usys_aspect", Aspect.class, String.class, false);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public List<Aspect> getAllRegistered()
	{
		List<Aspect> ret = new ArrayList<Aspect>();
		for (Aspect aspect : this.getAll())
		{
			if(aspect.isRegistered() == false) continue;
			ret.add(aspect);
		}
		return ret;
	}
	
	public List<Aspect> getAllRegisteredForMultiverse(Multiverse multiverse, boolean normal)
	{
		List<Aspect> ret = new ArrayList<Aspect>();
		for (Aspect aspect : this.getAll())
		{
			if(aspect.isRegistered() == false) continue;
			if((aspect.multiverse() != multiverse) == normal) continue;
			ret.add(aspect);
		}
		return ret;
	}	
}