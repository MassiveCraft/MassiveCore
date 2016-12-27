package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.mixin.Mixin;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NmsBasics extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsBasics d = new NmsBasics().setAlternatives(
		NmsBasics17R4P.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsBasics i = d;
	public static NmsBasics get() { return i; }
	
	// -------------------------------------------- //
	// GET HANDLE
	// -------------------------------------------- //
	
	public <T> T getHandle(Entity entity)
	{
		throw this.notImplemented();
	}
	
	public <T> T getHandle(World world)
	{
		throw this.notImplemented();
	}
	
	public <T> T getHandle(Scoreboard scoreboard)
	{
		throw this.notImplemented();
	}
	
	public <T> T getHandle(Team team)
	{
		throw this.notImplemented();
	}
	
	public <T> T getHandle(Objective objective)
	{
		throw this.notImplemented();
	}
	
	public <T> T getHandle(Sign sign)
	{
		throw this.notImplemented();
	}
	
	// -------------------------------------------- //
	// GET BUKKIT
	// -------------------------------------------- //
	
	public <T extends Entity> T getBukkit(Object handle)
	{
		throw this.notImplemented();
	}
	
	// -------------------------------------------- //
	// CONNECTION & PACKET
	// -------------------------------------------- //
	
	public <T> T getConnection(Player player)
	{
		throw this.notImplemented();
	}
	
	public void sendPacket(Player player, Object packet)
	{
		Object connection = this.getConnection(player);
		this.sendPacket(connection, packet);
	}
	
	public void sendPacket(Object connection, Object packet)
	{
		throw this.notImplemented();
	}
	
	// -------------------------------------------- //
	// PING
	// -------------------------------------------- //
	
	public int getPing(Player player)
	{
		throw this.notImplemented();
	}
	
}
