package com.massivecraft.mcore5.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_4_5.ChunkPosition;
import net.minecraft.server.v1_4_5.MinecraftServer;
import net.minecraft.server.v1_4_5.Packet60Explosion;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_5.CraftServer;
import org.bukkit.craftbukkit.v1_4_5.CraftWorld;

// http://mc.kev009.com/Protocol
// -----------------------------
// Smoke Directions 
// -----------------------------
// Direction 	ID	Direction
//				0	South - East
//				1	South
//				2	South - West
//				3	East
//				4	(Up or middle ?)
//				5	West
//				6	North - East
//				7	North
//				8	North - West
//-----------------------------

public class SmokeUtil
{
	public static Random random = new Random();
	
	// -------------------------------------------- //
	// Spawn once
	// -------------------------------------------- //
	
	// Single ========
	public static void spawnSingle(Location location, int direction)
	{
		if (location == null) return;
		location.getWorld().playEffect(location, Effect.SMOKE, direction);
	}
	
	public static void spawnSingle(Location location)
	{
		spawnSingle(location, 4);
	}
	
	public static void spawnSingleRandom(Location location)
	{
		spawnSingle(location, random.nextInt(9));
	}
	
	// Simple Cloud ========
	public static void spawnCloudSimple(Location location)
	{
		for (int i = 0; i <= 8; i++)
		{
			spawnSingle(location, i);
		}
	}
	
	public static void spawnCloudSimple(Collection<Location> locations)
	{
		for (Location location : locations)
		{
			spawnCloudSimple(location);
		}
	}
	
	// Random Cloud ========
	public static void spawnCloudRandom(Location location, float thickness)
	{
		int singles = (int) Math.floor(thickness*9);
		for (int i = 0; i < singles; i++)
		{
			spawnSingleRandom(location);
		}
	}
	
	public static void spawnCloudRandom(Collection<Location> locations, float thickness)
	{
		for (Location location : locations)
		{
			spawnCloudRandom(location, thickness);
		}
	}
	
	// Fake Explosion ========
	public static void fakeExplosion(Location location)
	{
		fakeExplosion(location, (Bukkit.getViewDistance()+1)*16*2);
	}
	
	public static void fakeExplosion(Location location, int viewDistance)
	{
		List<ChunkPosition> chunkPositions = new ArrayList<ChunkPosition>();
		Packet60Explosion packet = new Packet60Explosion(location.getX(),location.getY(), location.getZ(), 0.1f, chunkPositions, null);
		CraftServer craftServer = (CraftServer) Bukkit.getServer();
		MinecraftServer minecraftServer = craftServer.getServer();
		minecraftServer.getServerConfigurationManager().sendPacketNearby(location.getX(), location.getY(), location.getZ(), viewDistance, ((CraftWorld)location.getWorld()).getHandle().dimension, packet);
	}
	
	// -------------------------------------------- //
	// Attach continuous effects to or locations
	// -------------------------------------------- //
	
	// TODO
	
}
