package com.massivecraft.mcore.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import net.minecraft.server.v1_7_R1.BiomeBase;
import net.minecraft.server.v1_7_R1.Chunk;
import net.minecraft.server.v1_7_R1.WorldServer;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;

public class BiomeUtil
{
	public static Map<Integer, String> getBiomeIdNames()
	{
		Map<Integer, String> ret = new LinkedHashMap<Integer, String>();
		for (BiomeBase bb : BiomeBase.n())
		{
			if (bb == null) continue;
			ret.put(bb.id, bb.af);
		}
		return ret;
	}	
	
	public static Entry<Integer, String> getBiomeIdAndNameAt(World world, int x, int z)
	{
		CraftWorld craftWorld = (CraftWorld)world;
		WorldServer worldServer = craftWorld.getHandle();
		
		BiomeBase biomeBase = worldServer.getBiome(x, z);
		
		Integer id = biomeBase.id;
		String name = biomeBase.af;
		
		return new SimpleEntry<Integer, String>(id, name);
	}
	
	public static void setBiomeIdAt(World world, int x, int z, int id)
	{
		CraftWorld craftWorld = (CraftWorld)world;
		WorldServer worldServer = craftWorld.getHandle();
		
		BiomeBase bb = BiomeBase.getBiome(id);
		if (craftWorld.loadChunk(x >> 4, z >> 4, false)) {
			Chunk chunk = worldServer.getChunkAtWorldCoords(x, z);

			if (chunk != null) {
				byte[] biomevals = chunk.m();
				biomevals[((z & 0xF) << 4) | (x & 0xF)] = (byte)bb.id;
			}
		}
	}
	
	public static int getBiomeIdAt(World world, int x, int z)
	{
		return getBiomeIdAndNameAt(world, x, z).getKey();
	}
	
	public static String getBiomeNameAt(World world, int x, int z)
	{
		return getBiomeIdAndNameAt(world, x, z).getValue();
	}
	
}
