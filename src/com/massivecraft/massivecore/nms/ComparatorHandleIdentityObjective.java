package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.comparator.ComparatorAbstractTransformer;
import com.massivecraft.massivecore.comparator.ComparatorIdentity;
import org.bukkit.scoreboard.Objective;

public class ComparatorHandleIdentityObjective extends ComparatorAbstractTransformer<Objective, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorHandleIdentityObjective i = new ComparatorHandleIdentityObjective();
	public static ComparatorHandleIdentityObjective get() { return i; }
	public ComparatorHandleIdentityObjective()
	{
		super(ComparatorIdentity.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Object transform(Objective objective)
	{
		NmsBasics nms = NmsBasics.get();
		return nms.getHandle(objective);
	}

}
