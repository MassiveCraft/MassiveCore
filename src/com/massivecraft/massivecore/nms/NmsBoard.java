package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.collections.MassiveTreeSet;
import com.massivecraft.massivecore.mixin.Mixin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

public class NmsBoard extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsBoard d = new NmsBoard().setAlternatives(
		NmsBoard19R1P.class,
		NmsBoard18R1P.class,
		NmsBoard17R4.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsBoard i = d;
	public static NmsBoard get() { return i; }
	
	// -------------------------------------------- //
	// RESEARCH
	// -------------------------------------------- //
	// Minecraft 1.8 ended with 29 Feb 2016
	// Minecraft 1.7 ended with 25 Nov 2014
	// 
	// This means that 1.8 lacks the Option and OptionStatus system.
	// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/diff/src/main/java/org/bukkit/scoreboard/Team.java?until=f8573a0ca2e384e889832dc30ed41712e046fd30
	//
	// This means that 1.7 lacks the String Team entries:
	// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/diff/src/main/java/org/bukkit/scoreboard/Team.java?until=d24844cdd9ed1568412ebc2ad7e6b6157ac2c26a
	//
	// This means that 1.7 lacks name tag visibility.
	// Note that it should be implemented as part of the Option system.
	// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/diff/src/main/java/org/bukkit/craftbukkit/scoreboard/CraftTeam.java?autoSincePath=false&until=606cf0eea44b88d5630623ff8a5d63571fa42793
	// 
	// This means that 1.7 lacks proper equals and hash code implementation:
	// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/diff/src/main/java/org/bukkit/craftbukkit/scoreboard/CraftTeam.java?autoSincePath=false&until=4b6df5adfeb92cd09428e59a963a6ee56d7d28d6
	
	// -------------------------------------------- //
	// OPTIONS
	// -------------------------------------------- //
	
	public TeamOptionValue getOption(Team team, TeamOptionKey key)
	{
		throw notImplemented();
	}
	
	public void setOption(Team team, TeamOptionKey key, TeamOptionValue value)
	{
		throw notImplemented();
	}
	
	// -------------------------------------------- //
	// MEMBERS
	// -------------------------------------------- //
	
	public void addMember(Team team, String key)
	{
		throw notImplemented();
	}
	
	public boolean removeMember(Team team, String key)
	{
		throw notImplemented();
	}
	
	public boolean isMember(Team team, String key)
	{
		throw notImplemented();
	}
	
	public Set<String> getMembers(Team team)
	{
		throw notImplemented();
	}
	
	// -------------------------------------------- //
	// KEY TEAM
	// -------------------------------------------- //
	
	public Team getKeyTeam(Scoreboard board, String key)
	{
		throw notImplemented();
	}
	
	// -------------------------------------------- //
	// IS EQUALS IMPLEMENTED
	// -------------------------------------------- //
	
	public boolean isEqualsImplemented()
	{
		throw notImplemented();
	}
	
	public Set<Team> createTeamSet()
	{
		if (this.isEqualsImplemented()) return new MassiveSet<>();
		return new MassiveTreeSet<>(ComparatorHandleIdentityTeam.get());
	}
	
	public Set<Objective> createObjectiveSet()
	{
		if (this.isEqualsImplemented()) return new MassiveSet<>();
		return new MassiveTreeSet<>(ComparatorHandleIdentityObjective.get());
	}
	
	// -------------------------------------------- //
	// ENUM CONVERT
	// -------------------------------------------- //
	
	protected static <T extends Enum<T>> T convert(Enum<?> from, T[] to)
	{
		return to[from.ordinal()];
	}
	
}


