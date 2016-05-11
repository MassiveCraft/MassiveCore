package com.massivecraft.massivecore.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class NmsBoard extends NmsAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsBoard i = new NmsBoard();
	public static NmsBoard get () { return i; }
	
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
	
	// net.minecraft.server.PacketPlayOutScoreboardTeam
	private Class<?> classPacketTeam;
	
	// net.minecraft.server.PacketPlayOutScoreboardTeam(ScoreboardTeam, int)
	private Constructor<?> constructorPacketTeamUpdate;
	
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
		
		this.classPacketTeam = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutScoreboardTeam");
		this.constructorPacketTeamUpdate = ReflectionUtil.getConstructor(this.classPacketTeam, this.classNmsTeam, int.class);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	public ChatColor getColor(Team team)
	{
		if ( ! this.isAvailable()) return null;
		
		Object nmsTeam = getTeamHandle(team);
		Object nmsColor = ReflectionUtil.getField(this.fieldNmsTeamColor, nmsTeam);
		
		return convertColor(nmsColor);
	}
	
	public void setColor(Team team, ChatColor color)
	{
		if ( ! this.isAvailable()) return;
		
		Object nmsTeam = getTeamHandle(team);
		Object nmsColor = convertColor(color);
		ReflectionUtil.setField(this.fieldNmsTeamColor, nmsTeam, nmsColor);
		
		// This is a quick and dirty solution.
		// It makes sure the scoreboard is updated.
		team.setDisplayName(team.getDisplayName());
	}
	
	// -------------------------------------------- //
	// TEAM
	// -------------------------------------------- //
	
	public <T> T getTeamHandle(Team team)
	{
		return ReflectionUtil.getField(this.fieldCraftTeamHandle, team);
	}
	
	// -------------------------------------------- //
	// PACKET
	// -------------------------------------------- //
	
	// This is a magic NMS value for the packet constructor.
	// 2 simply means update exiting team rather than creating a new one.
	private static final int PACKET_UPDATE_MODE = 2;
	
	public <T> T createTeamUpdatePacket(Team team)
	{
		Object handle = getTeamHandle(team);
		return ReflectionUtil.invokeConstructor(this.constructorPacketTeamUpdate, handle, PACKET_UPDATE_MODE);
	}
	
	public void sendTeamUpdatePacket(Team team, Player player)
	{
		Object packet = this.createTeamUpdatePacket(team);
		NmsPacket.sendPacket(player, packet);
	}
	
	// -------------------------------------------- //
	// COLOR > CONVERT
	// -------------------------------------------- //
	
	public ChatColor convertColor(Object nms)
	{
		if (nms == null) return null;
		int code = ReflectionUtil.getField(this.fieldNmsColorCode, nms);
		return code(code);
	}
	
	public <T> T convertColor(ChatColor bukkit)
	{
		if (bukkit == null) return null;
		int code = code(bukkit);
		return ReflectionUtil.invokeMethod(this.methodNmsColorFor, null, code);
	}
	
	// -------------------------------------------- //
	// COLOR > CODE
	// -------------------------------------------- //
	
	public static ChatColor code(int code)
	{
		ChatColor ret = COLOR_TO_CODE.inverse().get(code);
		if (ret == null) throw new IllegalArgumentException("Unsupported Code " + code);
		return ret;
	}
	
	public static int code(ChatColor color)
	{
		Integer ret = COLOR_TO_CODE.get(color);
		if (ret == null) throw new IllegalArgumentException("Unsupported Color " + color);
		return ret;
	}
	
	public static final BiMap<ChatColor, Integer> COLOR_TO_CODE = ImmutableBiMap.<ChatColor, Integer>builder()
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
