package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.comparator.ComparatorAbstractTransformer;
import com.massivecraft.massivecore.comparator.ComparatorIdentity;
import org.bukkit.scoreboard.Team;

public class ComparatorHandleIdentityTeam extends ComparatorAbstractTransformer<Team, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorHandleIdentityTeam i = new ComparatorHandleIdentityTeam();
	public static ComparatorHandleIdentityTeam get() { return i; }
	public ComparatorHandleIdentityTeam()
	{
		super(ComparatorIdentity.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Object transform(Team team)
	{
		NmsBasics nms = NmsBasics.get();
		return nms.getHandle(team);
	}

}
