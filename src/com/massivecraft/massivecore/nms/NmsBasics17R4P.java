package com.massivecraft.massivecore.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;

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
	
	// org.bukkit.craftbukkit.scoreboard.CraftTeam
	private Class<?> classCraftTeam;
	// org.bukkit.craftbukkit.scoreboard.CraftTeam#team
	private Field fieldCraftTeamHandle;
	
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
		
		this.classCraftTeam = PackageType.CRAFTBUKKIT_SCOREBOARD.getClass("CraftTeam");
		this.fieldCraftTeamHandle = ReflectionUtil.getField(this.classCraftTeam, "team");
		
		// GET BUKKIT
		this.classNmsEntity = PackageType.MINECRAFT_SERVER.getClass("Entity");
		this.methodNmsEntityGetBukkitEntity = ReflectionUtil.getMethod(this.classNmsEntity, "getBukkitEntity");
		
		// CONNECTION & PACKET
		
		this.classNmsPlayer = PackageType.MINECRAFT_SERVER.getClass("EntityPlayer");
		this.fieldNmsPlayerPlayerConnection = ReflectionUtil.getField(this.classNmsPlayer, "playerConnection");
		this.classNmsPacket = PackageType.MINECRAFT_SERVER.getClass("Packet");
		this.classNmsPlayerConnection = PackageType.MINECRAFT_SERVER.getClass("PlayerConnection");
		this.methodPlayerConnectionsendPacket = ReflectionUtil.getMethod(this.classNmsPlayerConnection, "sendPacket", this.classNmsPacket);
	}
	
	// -------------------------------------------- //
	// GET HANDLE
	// -------------------------------------------- //
	
	@Override
	public <T> T getHandle(Entity entity)
	{
		return ReflectionUtil.invokeMethod(this.methodCraftEntityGetHandle, entity);
	}
	
	@Override
	public <T> T getHandle(World world)
	{
		return ReflectionUtil.getField(this.fieldCraftWorldWorld, world);
	}
	
	@Override
	public <T> T getHandle(Team team)
	{
		return ReflectionUtil.getField(this.fieldCraftTeamHandle, team);
	}
	
	// -------------------------------------------- //
	// GET BUKKIT
	// -------------------------------------------- //
	
	@Override
	public <T extends Entity> T getBukkit(Object handle)
	{
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
	
}
