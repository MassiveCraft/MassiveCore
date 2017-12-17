package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.xlib.guava.collect.ImmutableSet;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

public class NmsBoard17R4 extends NmsBoard
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsBoard17R4 i = new NmsBoard17R4();
	public static NmsBoard17R4 get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// net.minecraft.server.Scoreboard
	protected Class<?> classNmsScoreboard;
	// net.minecraft.server.ScoreboardTeam
	protected Class<?> classNmsScoreboardTeam;
	
	// net.minecraft.server.Scoreboard#getTeam(String)
	protected Method methodNmsScoreboardGetTeam;
	// net.minecraft.server.Scoreboard#addPlayerToTeam(String, String)
	protected Method methodNmsScoreboardAddPlayerToTeam;
	// net.minecraft.server.Scoreboard#removePlayerFromTeam(String, net.minecraft.server.ScoreboardTeam)
	protected Method methodNmsScoreboardRemovePlayerFromTeam;
	// net.minecraft.server.Scoreboard#getPlayerTeam(String)
	protected Method methodNmsScoreboardGetPlayerTeam;

	// net.minecraft.server.ScoreboardTeam#getPlayerNameSet()
	protected Method methodNmsScoreboardTeamGetPlayerNameSet;
	
	// org.bukkit.craftbukkit.scoreboard.CraftScoreboard
	protected Class<?> classCraftScoreboard;	
	// org.bukkit.craftbukkit.scoreboard.CraftTeam
	protected Class<?> classCraftTeam;
	// org.bukkit.craftbukkit.scoreboard.CraftTeam(org.bukkit.craftbukkit.scoreboard.CraftScoreboard, net.minecraft.server.ScoreboardTeam)
	protected Constructor<?> constructorCraftTeam;
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		NmsBasics.get().require();
		
		this.classNmsScoreboard = PackageType.MINECRAFT_SERVER.getClass("Scoreboard");
		this.classNmsScoreboardTeam = PackageType.MINECRAFT_SERVER.getClass("ScoreboardTeam");
		
		this.methodNmsScoreboardGetTeam = ReflectionUtil.getMethod(this.classNmsScoreboard, "getTeam", String.class);
		this.methodNmsScoreboardAddPlayerToTeam = ReflectionUtil.getMethod(this.classNmsScoreboard, "addPlayerToTeam", String.class, String.class);
		this.methodNmsScoreboardRemovePlayerFromTeam = ReflectionUtil.getMethod(this.classNmsScoreboard, "removePlayerFromTeam", String.class, this.classNmsScoreboardTeam);
		this.methodNmsScoreboardGetPlayerTeam = ReflectionUtil.getMethod(this.classNmsScoreboard, "getPlayerTeam", String.class);
					
		this.methodNmsScoreboardTeamGetPlayerNameSet = ReflectionUtil.getMethod(this.classNmsScoreboardTeam, "getPlayerNameSet");
		
		this.classCraftScoreboard = PackageType.CRAFTBUKKIT_SCOREBOARD.getClass("CraftScoreboard");
		this.classCraftTeam = PackageType.CRAFTBUKKIT_SCOREBOARD.getClass("CraftTeam");
		this.constructorCraftTeam = ReflectionUtil.getConstructor(this.classCraftTeam, this.classCraftScoreboard, this.classNmsScoreboardTeam);
	}
	
	// -------------------------------------------- //
	// OPTIONS
	// -------------------------------------------- //
	// In 1.7 there were no options.
	
	@Override
	public TeamOptionValue getOption(Team team, TeamOptionKey key)
	{
		return null;
	}
	
	@Override
	public void setOption(Team team, TeamOptionKey key, TeamOptionValue value)
	{
		
	}
	
	// -------------------------------------------- //
	// MEMBERS
	// -------------------------------------------- //
	
	@Override
	public void addMember(Team team, String key)
	{
		Object handle = this.getBoardHandleValidated(team);
		
		ReflectionUtil.invokeMethod(this.methodNmsScoreboardAddPlayerToTeam, handle, key, team.getName());
	}
	
	@Override
	public boolean removeMember(Team team, String key)
	{
		Object handle = this.getBoardHandleValidated(team);
		
		if ( ! this.getMembersRaw(team).contains(key)) return false;
		
		Object teamHandle = NmsBasics.get().getHandle(team);
		
		ReflectionUtil.invokeMethod(this.methodNmsScoreboardRemovePlayerFromTeam, handle, key, teamHandle);
		
		return true;
	}
	
	@Override
	public boolean isMember(Team team, String key)
	{
		this.getBoardHandleValidated(team);
		
		Set<String> members = this.getMembersRaw(team);
		
		return members.contains(key);
	}
	
	public Set<String> getMembers(Team team)
	{
		this.getBoardHandleValidated(team);
		
		Set<String> members = this.getMembersRaw(team);
		
		ImmutableSet.Builder<String> ret = ImmutableSet.builder();
		for (String member : members)
		{
			ret.add(member);
		}
		
		return ret.build();
	}
	
	protected Set<String> getMembersRaw(Team team)
	{
		Object handle = NmsBasics.get().getHandle(team);
		return ReflectionUtil.invokeMethod(this.methodNmsScoreboardTeamGetPlayerNameSet, handle);
	}
	
	protected <T> T getBoardHandleValidated(Team team)
	{
		Scoreboard board = team.getScoreboard();
		T handle = NmsBasics.get().getHandle(board);
		if (ReflectionUtil.invokeMethod(this.methodNmsScoreboardGetTeam, handle, team.getName()) == null) throw new IllegalStateException("Unregistered scoreboard component");
		return handle;
	}
	

	// -------------------------------------------- //
	// KEY TEAM
	// -------------------------------------------- //
	
	@Override
	public Team getKeyTeam(Scoreboard board, String key)
	{
		if (board == null) throw new NullPointerException("board");
		if (key == null) throw new NullPointerException("key");
		
		Object boardHandle = NmsBasics.get().getHandle(board);
		Object teamHandle = ReflectionUtil.invokeMethod(this.methodNmsScoreboardGetPlayerTeam, boardHandle, key);
		
		if (teamHandle == null) return null;
		return ReflectionUtil.invokeConstructor(this.constructorCraftTeam, board, teamHandle);
	}
	
	// -------------------------------------------- //
	// IS EQUALS IMPLEMENTED
	// -------------------------------------------- //
	// In 1.7 the equals was not yet implemented.
	
	@Override
	public boolean isEqualsImplemented()
	{
		return false;
	}
	
}


