package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NmsBasics17R4P extends NmsBasics
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsBasics17R4P i = new NmsBasics17R4P();
	public static NmsBasics17R4P get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// GET HANDLE
	
	// org.bukkit.craftbukkit.entity.CraftEntity
	private Class<?> classCraftEntity;
	// org.bukkit.craftbukkit.entity.Entity#getHandle()
	private Method methodCraftEntityGetHandle;
	
	// org.bukkit.craftbukkit.CraftWorld
	public Class<?> classCraftWorld;
	// org.bukkit.craftbukkit.CraftWorld#world
	public Field fieldCraftWorldWorld;
	
	// org.bukkit.craftbukkit.scoreboard.CraftScoreboard
	private Class<?> classCraftScoreboard;
	// org.bukkit.craftbukkit.scoreboard.CraftScoreboard#board
	private Field fieldCraftScoreboardHandle;
	
	// org.bukkit.craftbukkit.scoreboard.CraftTeam
	private Class<?> classCraftTeam;
	// org.bukkit.craftbukkit.scoreboard.CraftTeam#team
	private Field fieldCraftTeamHandle;
	
	// org.bukkit.craftbukkit.scoreboard.CraftObjective
	private Class<?> classCraftObjective;
	// org.bukkit.craftbukkit.scoreboard.CraftObjective#objective
	private Field fieldCraftObjectiveHandle;
	
	// SIGN for 1.12.0 and below
	// org.bukkit.craftbukkit.block.CraftSign
	private Class<?> classCraftSign;
	// org.bukkit.craftbukkit.block.CraftSign#sign
	private Field fieldCraftSignHandle;
	
	// SIGN for 1.12.1 and above
	// org.bukkit.craftbukkit.block.CraftBlockEntityState
	private Class<?> classCraftBlockEntityState;
	// org.bukkit.craftbukkit.block.CraftBlockEntityState#tileEntity
	private Field fieldCraftBlockEntityStateHandle;
	
	// GET BUKKIT
	// net.minecraft.server.Entity
	private Class<?> classNmsEntity;
	// net.minecraft.server.Entity#getBukkitEntity()
	private Method methodNmsEntityGetBukkitEntity;
	
	// CONNECTION & PACKET
	
	// net.minecraft.server.EntityPlayer
	private Class<?> classNmsPlayer;
	// net.minecraft.server.EntityPlayer#playerConnection
	private Field fieldNmsPlayerPlayerConnection;
	// net.minecraft.server.Packet
	private Class<?> classNmsPacket;
	// net.minecraft.server.PlayerConnection
	private Class<?> classNmsPlayerConnection;
	// net.minecraft.server.PlayerConnection#sendPacket(Packet)
	private Method methodPlayerConnectionsendPacket;
	
	// PING
	// net.minecraft.server.EntityPlayer#ping
	private Field fieldNmsPlayerPing;
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		// GET HANDLE
		this.classCraftEntity = PackageType.CRAFTBUKKIT_ENTITY.getClass("CraftEntity");
		this.methodCraftEntityGetHandle = ReflectionUtil.getMethod(this.classCraftEntity, "getHandle");
		
		this.classCraftWorld = PackageType.CRAFTBUKKIT.getClass("CraftWorld");
		this.fieldCraftWorldWorld = ReflectionUtil.getField(this.classCraftWorld, "world");
		
		this.classCraftScoreboard = PackageType.CRAFTBUKKIT_SCOREBOARD.getClass("CraftScoreboard");
		this.fieldCraftScoreboardHandle = ReflectionUtil.getField(this.classCraftScoreboard, "board");
		
		this.classCraftTeam = PackageType.CRAFTBUKKIT_SCOREBOARD.getClass("CraftTeam");
		this.fieldCraftTeamHandle = ReflectionUtil.getField(this.classCraftTeam, "team");
		
		this.classCraftObjective = PackageType.CRAFTBUKKIT_SCOREBOARD.getClass("CraftObjective");
		this.fieldCraftObjectiveHandle = ReflectionUtil.getField(this.classCraftObjective, "objective");
		
		try
		{
			// SIGN for 1.12.0 and below
			this.classCraftSign = PackageType.CRAFTBUKKIT_BLOCK.getClass("CraftSign");
			this.fieldCraftSignHandle = ReflectionUtil.getField(this.classCraftSign, "sign");
		}
		catch (Throwable t)
		{
			// SIGN for 1.12.1 and above
			this.classCraftBlockEntityState = PackageType.CRAFTBUKKIT_BLOCK.getClass("CraftBlockEntityState");
			this.fieldCraftBlockEntityStateHandle = ReflectionUtil.getField(this.classCraftBlockEntityState, "tileEntity");
		}
		
		// GET BUKKIT
		this.classNmsEntity = PackageType.MINECRAFT_SERVER.getClass("Entity");
		this.methodNmsEntityGetBukkitEntity = ReflectionUtil.getMethod(this.classNmsEntity, "getBukkitEntity");
		
		// CONNECTION & PACKET
		
		this.classNmsPlayer = PackageType.MINECRAFT_SERVER.getClass("EntityPlayer");
		this.fieldNmsPlayerPlayerConnection = ReflectionUtil.getField(this.classNmsPlayer, "playerConnection");
		this.classNmsPacket = PackageType.MINECRAFT_SERVER.getClass("Packet");
		this.classNmsPlayerConnection = PackageType.MINECRAFT_SERVER.getClass("PlayerConnection");
		this.methodPlayerConnectionsendPacket = ReflectionUtil.getMethod(this.classNmsPlayerConnection, "sendPacket", this.classNmsPacket);
		
		// PING
		this.fieldNmsPlayerPing = ReflectionUtil.getField(this.classNmsPlayer, "ping");
	}
	
	// -------------------------------------------- //
	// GET HANDLE
	// -------------------------------------------- //
	
	@Override
	public <T> T getHandle(Entity entity)
	{
		if (entity == null) return null;
		return ReflectionUtil.invokeMethod(this.methodCraftEntityGetHandle, entity);
	}
	
	@Override
	public <T> T getHandle(World world)
	{
		if (world == null) return null;
		return ReflectionUtil.getField(this.fieldCraftWorldWorld, world);
	}
	
	@Override
	public <T> T getHandle(Scoreboard scoreboard)
	{
		if (scoreboard == null) return null;
		return ReflectionUtil.getField(this.fieldCraftScoreboardHandle, scoreboard);
	}
	
	@Override
	public <T> T getHandle(Team team)
	{
		if (team == null) return null;
		return ReflectionUtil.getField(this.fieldCraftTeamHandle, team);
	}
	
	@Override
	public <T> T getHandle(Objective objective)
	{
		if (objective == null) return null;
		return ReflectionUtil.getField(this.fieldCraftObjectiveHandle, objective);
	}
	
	@Override
	public <T> T getHandle(Sign sign)
	{
		if (sign == null) return null;
		
		Field field;
		
		if (this.fieldCraftSignHandle != null)
		{
			// SIGN for 1.12.0 and below
			field = this.fieldCraftSignHandle;
		}
		else
		{
			// SIGN for 1.12.1 and above
			field = this.fieldCraftBlockEntityStateHandle;
		}
		
		return ReflectionUtil.getField(field, sign);
	}
	
	// -------------------------------------------- //
	// GET BUKKIT
	// -------------------------------------------- //
	
	@Override
	public <T extends Entity> T getBukkit(Object handle)
	{
		if (handle == null) return null;
		return ReflectionUtil.invokeMethod(this.methodNmsEntityGetBukkitEntity, handle);
	}
	
	// -------------------------------------------- //
	// CONNECTION & PACKET
	// -------------------------------------------- //
	
	@Override
	public <T> T getConnection(Player player)
	{
		Object handle = this.getHandle(player);
		return ReflectionUtil.getField(this.fieldNmsPlayerPlayerConnection, handle);
	}
	
	@Override
	public void sendPacket(Object connection, Object packet)
	{
		ReflectionUtil.invokeMethod(this.methodPlayerConnectionsendPacket, connection, packet);
	}
	
	// -------------------------------------------- //
	// PING
	// -------------------------------------------- //
	
	@Override
	public int getPing(Player player)
	{
		Object handle = this.getHandle(player);
		return ReflectionUtil.getField(this.fieldNmsPlayerPing, handle);
	}
	
}
