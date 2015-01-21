package com.massivecraft.massivecore;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

public class AspectColl extends Coll<Aspect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AspectColl i = new AspectColl();
	public static AspectColl get() { return i; }
	private AspectColl()
	{
		super("massivecore_aspect", Aspect.class, MStore.getDb("default"), MassiveCore.get());
	}

	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public List<Aspect> getAllRegistered()
	{
		List<Aspect> ret = new ArrayList<Aspect>();
		for (Aspect aspect : this.getAll())
		{
			if (aspect.isRegistered() == false) continue;
			ret.add(aspect);
		}
		return ret;
	}
	
	public List<Aspect> getAllRegisteredForMultiverse(Multiverse multiverse, boolean normal)
	{
		List<Aspect> ret = new ArrayList<Aspect>();
		for (Aspect aspect : this.getAll())
		{
			if (aspect.isRegistered() == false) continue;
			if ((aspect.getMultiverse() != multiverse) == normal) continue;
			ret.add(aspect);
		}
		return ret;
	}
	
}
