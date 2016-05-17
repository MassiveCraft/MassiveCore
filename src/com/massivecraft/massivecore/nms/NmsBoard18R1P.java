package com.massivecraft.massivecore.nms;

import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

@SuppressWarnings("deprecation")
public class NmsBoard18R1P extends NmsBoard17R4
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsBoard18R1P i = new NmsBoard18R1P();
	public static NmsBoard18R1P get() { return i; }	
	
	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	
	@Override
	public Object provoke() throws Throwable
	{
		super.provoke();
		return NameTagVisibility.ALWAYS;
	}
	
	// -------------------------------------------- //
	// OPTIONS
	// -------------------------------------------- //
	// In 1.8 there were only name tag visibility.
	
	@Override
	public TeamOptionValue getOption(Team team, TeamOptionKey key)
	{
		if (key != TeamOptionKey.NAME_TAG_VISIBILITY) return null;
		NameTagVisibility bukkitValue = team.getNameTagVisibility();
		return convert(bukkitValue, TeamOptionValue.values());
	}
	
	@Override
	public void setOption(Team team, TeamOptionKey key, TeamOptionValue value)
	{
		if (key != TeamOptionKey.NAME_TAG_VISIBILITY) return;
		NameTagVisibility bukkitValue = convert(value, NameTagVisibility.values());
		team.setNameTagVisibility(bukkitValue);
	}
	
	// -------------------------------------------- //
	// IS EQUALS IMPLEMENTED
	// -------------------------------------------- //
	// In 1.8 the equals was implemented and we no longer need a custom comparator.
	
	@Override
	public boolean isEqualsImplemented()
	{
		return true;
	}
	
}


