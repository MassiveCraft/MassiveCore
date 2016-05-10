package com.massivecraft.massivecore.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class NmsTeamColor extends NmsAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsTeamColor i = new NmsTeamColor();
	public static NmsTeamColor get () { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// org.bukkit.craftbukkit.scoreboard.CraftTeam
	private Class<?> classCraftTeam;
	
	// org.bukkit.craftbukkit.scoreboard.CraftTeam#team
	private Field fieldCraftTeamHandle;
	
	// net.minecraft.server.ScoreboardTeam
	private Class<?> classNmsTeam;
	
	// net.minecraft.server.ScoreboardTeam#k <--- color
	private Field fieldNmsTeamColor;
	
	// net.minecraft.server.EnumChatFormat
	private Class<?> classNmsColor;
	
	// net.minecraft.server.EnumChatFormat#C <-- code
	private Field fieldNmsColorCode;
	
	// net.minecraft.server.EnumChatFormat.a(int i) <-- for code
	private Method methodNmsColorFor;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getRequiredVersion()
	{
		return 9;
	}
	
	@Override
	protected void setup() throws Throwable
	{
		this.classCraftTeam = PackageType.CRAFTBUKKIT_SCOREBOARD.getClass("CraftTeam");
		this.fieldCraftTeamHandle = ReflectionUtil.getField(this.classCraftTeam, "team");
		
		this.classNmsTeam = PackageType.MINECRAFT_SERVER.getClass("ScoreboardTeam");
		this.fieldNmsTeamColor = ReflectionUtil.getField(this.classNmsTeam, "k");
		
		this.classNmsColor = PackageType.MINECRAFT_SERVER.getClass("EnumChatFormat");
		this.fieldNmsColorCode = ReflectionUtil.getField(this.classNmsColor, "C");
		this.methodNmsColorFor = ReflectionUtil.getMethod(this.classNmsColor, "a", int.class);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	public ChatColor get(Team team)
	{
		if ( ! this.isAvailable()) return null;
		
		Object nmsTeam = convertTeam(team);
		Object nmsColor = ReflectionUtil.getField(this.fieldNmsTeamColor, nmsTeam);
		
		return convertColor(nmsColor);
	}
	
	public void set(Team team, ChatColor color)
	{
		if ( ! this.isAvailable()) return;
		
		Object nmsTeam = convertTeam(team);
		Object nmsColor = convertColor(color);
		ReflectionUtil.setField(this.fieldNmsTeamColor, nmsTeam, nmsColor);
		
		// This is a quick and dirty solution.
		// It makes sure the scoreboard is updated.
		team.setDisplayName(team.getDisplayName());
	}
	
	// -------------------------------------------- //
	// CONVERT TEAM
	// -------------------------------------------- //
	
	private Object convertTeam(Team team)
	{
		return ReflectionUtil.getField(this.fieldCraftTeamHandle, team);
	}
	
	// -------------------------------------------- //
	// CONVERT COLOR
	// -------------------------------------------- //
	
	private ChatColor convertColor(Object nms)
	{
		if (nms == null) return null;
		int code = ReflectionUtil.getField(this.fieldNmsColorCode, nms);
		return code(code);
	}
	
	private Object convertColor(ChatColor bukkit)
	{
		if (bukkit == null) return null;
		int code = code(bukkit);
		return ReflectionUtil.invokeMethod(this.methodNmsColorFor, null, code);
	}
	
	// -------------------------------------------- //
	// CODE
	// -------------------------------------------- //
	
	private static ChatColor code(int code)
	{
		ChatColor ret = COLOR_TO_CODE.inverse().get(code);
		if (ret == null) throw new IllegalArgumentException("Unsupported Code " + code);
		return ret;
	}
	
	private static int code(ChatColor color)
	{
		Integer ret = COLOR_TO_CODE.get(color);
		if (ret == null) throw new IllegalArgumentException("Unsupported Color " + color);
		return ret;
	}
	
	private static final BiMap<ChatColor, Integer> COLOR_TO_CODE = ImmutableBiMap.<ChatColor, Integer>builder()
		.put(ChatColor.BLACK, 0)
		.put(ChatColor.DARK_BLUE, 1)
		.put(ChatColor.DARK_GREEN, 2)
		.put(ChatColor.DARK_AQUA, 3)
		.put(ChatColor.DARK_RED, 4)
		.put(ChatColor.DARK_PURPLE, 5)
		.put(ChatColor.GOLD, 6)
		.put(ChatColor.GRAY, 7)
		.put(ChatColor.DARK_GRAY, 8)
		.put(ChatColor.BLUE, 9)
		.put(ChatColor.GREEN, 10)
		.put(ChatColor.AQUA, 11)
		.put(ChatColor.RED, 12)
		.put(ChatColor.LIGHT_PURPLE, 13)
		.put(ChatColor.YELLOW, 14)
		.put(ChatColor.WHITE, 15)
		// The only supported format is RESET.
		// .put(ChatColor.MAGIC, ???)
		// .put(ChatColor.BOLD, ???)
		// .put(ChatColor.STRIKETHROUGH, ???)
		// .put(ChatColor.UNDERLINE, ???)
		// .put(ChatColor.ITALIC, ???)
		.put(ChatColor.RESET, -1)
	.build();
	
}
