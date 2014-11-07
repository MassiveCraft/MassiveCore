package com.massivecraft.massivecore.ps;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.annotations.SerializedName;

/**
 * # Introduction
 * PS stands for PhysicalState.
 * This class stores data related to just that.
 * When coding plugins you may find yourself wanting to store a player location.
 * Another time you may want to store the player location but without the worldName info.
 * Another time you may want to store pitch and yaw only.
 * This class is supposed to be usable in all those cases.
 * Hopefully this class will save you from implementing special classes for all those combinations.
 * 
 * # Field Groups
 * velocity: velocityX, velocityY, velocityZ
 * blockCoords: blockX, blockY, blockZ
 * locationCoords: locationX, locationY, locationZ
 * chunkCoords: chunkX, chunkZ
 * head: pitch, yaw
 * block: world, blockX, blockY, blockZ
 * location: world, locationX, locationY, locationZ, pitch, yaw
 * chunk: world, chunkX, chunkZ
 * entity: world, locationX, locationY, locationZ, pitch, yaw, velocityX, velocityY, velocityZ
 */

public final class PS implements Cloneable, Serializable, Comparable<PS>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final transient long serialVersionUID = 1L;
	
	public static final transient float DEFAULT_BUKKIT_PITCH = 0F;
	public static final transient float DEFAULT_BUKKIT_YAW = 0F;
	
	public static final transient String NAME_SERIALIZED_WORLD = "w";
	public static final transient String NAME_SERIALIZED_BLOCKX = "bx";
	public static final transient String NAME_SERIALIZED_BLOCKY = "by";
	public static final transient String NAME_SERIALIZED_BLOCKZ = "bz";
	public static final transient String NAME_SERIALIZED_LOCATIONX = "lx";
	public static final transient String NAME_SERIALIZED_LOCATIONY = "ly";
	public static final transient String NAME_SERIALIZED_LOCATIONZ = "lz";
	public static final transient String NAME_SERIALIZED_CHUNKX = "cx";
	public static final transient String NAME_SERIALIZED_CHUNKZ = "cz";
	public static final transient String NAME_SERIALIZED_PITCH = "p";
	public static final transient String NAME_SERIALIZED_YAW = "y";
	public static final transient String NAME_SERIALIZED_VELOCITYX = "vx";
	public static final transient String NAME_SERIALIZED_VELOCITYY = "vy";
	public static final transient String NAME_SERIALIZED_VELOCITYZ = "vz";
	
	public static final transient String NAME_FULL_WORLD = "world";
	public static final transient String NAME_FULL_BLOCKX = "blockX";
	public static final transient String NAME_FULL_BLOCKY = "blockY";
	public static final transient String NAME_FULL_BLOCKZ = "blockZ";
	public static final transient String NAME_FULL_LOCATIONX = "locationX";
	public static final transient String NAME_FULL_LOCATIONY = "locationY";
	public static final transient String NAME_FULL_LOCATIONZ = "locationZ";
	public static final transient String NAME_FULL_CHUNKX = "chunkX";
	public static final transient String NAME_FULL_CHUNKZ = "chunkZ";
	public static final transient String NAME_FULL_PITCH = "pitch";
	public static final transient String NAME_FULL_YAW = "yaw";
	public static final transient String NAME_FULL_VELOCITYX = "velocityX";
	public static final transient String NAME_FULL_VELOCITYY = "velocityY";
	public static final transient String NAME_FULL_VELOCITYZ = "velocityZ";
	
	public static final transient String NAME_VERBOOSE_WORLD = "World";
	public static final transient String NAME_VERBOOSE_BLOCKX = "Block X";
	public static final transient String NAME_VERBOOSE_BLOCKY = "Block Y";
	public static final transient String NAME_VERBOOSE_BLOCKZ = "Block Z";
	public static final transient String NAME_VERBOOSE_LOCATIONX = "Location X";
	public static final transient String NAME_VERBOOSE_LOCATIONY = "Location Y";
	public static final transient String NAME_VERBOOSE_LOCATIONZ = "Location Z";
	public static final transient String NAME_VERBOOSE_CHUNKX = "Chunk X";
	public static final transient String NAME_VERBOOSE_CHUNKZ = "Chunk Z";
	public static final transient String NAME_VERBOOSE_PITCH = "Pitch";
	public static final transient String NAME_VERBOOSE_YAW = "Yaw";
	public static final transient String NAME_VERBOOSE_VELOCITYX = "Velocity X";
	public static final transient String NAME_VERBOOSE_VELOCITYY = "Velocity Y";
	public static final transient String NAME_VERBOOSE_VELOCITYZ = "Velocity Z";
	
	public static final transient String SPACE_WASNT_SET = " wasn't set";
	
	// -------------------------------------------- //
	// STANDARD INSTANCES
	// -------------------------------------------- //
	
	public static final transient PS NULL = new PS(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	@SerializedName(NAME_SERIALIZED_WORLD)
	private final String world;
	public String getWorld() { return this.world; }
	
	@SerializedName(NAME_SERIALIZED_BLOCKX)
	private final Integer blockX;
	public Integer getBlockX() { return this.blockX; }
	
	@SerializedName(NAME_SERIALIZED_BLOCKY)
	private final Integer blockY;
	public Integer getBlockY() { return this.blockY; }
	
	@SerializedName(NAME_SERIALIZED_BLOCKZ)
	private final Integer blockZ;
	public Integer getBlockZ() { return this.blockZ; }
	
	@SerializedName(NAME_SERIALIZED_LOCATIONX)
	private final Double locationX;
	public Double getLocationX() { return this.locationX; }
	
	@SerializedName(NAME_SERIALIZED_LOCATIONY)
	private final Double locationY;
	public Double getLocationY() { return this.locationY; }
	
	@SerializedName(NAME_SERIALIZED_LOCATIONZ)
	private final Double locationZ;
	public Double getLocationZ() { return this.locationZ; }
	
	@SerializedName(NAME_SERIALIZED_CHUNKX)
	private final Integer chunkX;
	public Integer getChunkX() { return this.chunkX; }
	
	@SerializedName(NAME_SERIALIZED_CHUNKZ)
	private final Integer chunkZ;
	public Integer getChunkZ() { return this.chunkZ; }
	
	@SerializedName(NAME_SERIALIZED_PITCH)
	private final Float pitch;
	public Float getPitch() { return this.pitch; }
	
	@SerializedName(NAME_SERIALIZED_YAW)
	private final Float yaw;
	public Float getYaw() { return this.yaw; }
	
	@SerializedName(NAME_SERIALIZED_VELOCITYX)
	private final Double velocityX;
	public Double getVelocityX() { return this.velocityX; }
	
	@SerializedName(NAME_SERIALIZED_VELOCITYY)
	private final Double velocityY;
	public Double getVelocityY() { return this.velocityY; }
	
	@SerializedName(NAME_SERIALIZED_VELOCITYZ)
	private final Double velocityZ;
	public Double getVelocityZ() { return this.velocityZ; }
	
	// -------------------------------------------- //
	// FIELDS: WITH
	// -------------------------------------------- //
	
	public PS withWorld(String world) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withBlockX(Integer blockX) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withBlockY(Integer blockY) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withBlockZ(Integer blockZ) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withLocationX(Double locationX) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withLocationY(Double locationY) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withLocationZ(Double locationZ) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withChunkX(Integer chunkX) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withChunkZ(Integer chunkZ) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withPitch(Float pitch) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withYaw(Float yaw) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withVelocityX(Double velocityX) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withVelocityY(Double velocityY) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS withVelocityZ(Double velocityZ) { return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	
	public PS with(PS ps)
	{
		PSBuilder builder = this.builder();
		
		if (ps.getWorld() != null) builder.world(ps.getWorld());
		if (ps.getBlockX() != null) builder.blockX(ps.getBlockX());
		if (ps.getBlockY() != null) builder.blockY(ps.getBlockY());
		if (ps.getBlockZ() != null) builder.blockZ(ps.getBlockZ());
		if (ps.getLocationX() != null) builder.locationX(ps.getLocationX());
		if (ps.getLocationY() != null) builder.locationY(ps.getLocationY());
		if (ps.getLocationZ() != null) builder.locationZ(ps.getLocationZ());
		if (ps.getChunkX() != null) builder.chunkX(ps.getChunkX());
		if (ps.getChunkZ() != null) builder.chunkZ(ps.getChunkZ());
		if (ps.getPitch() != null) builder.pitch(ps.getPitch());
		if (ps.getYaw() != null) builder.yaw(ps.getYaw());
		if (ps.getVelocityX() != null) builder.velocityX(ps.getVelocityX());
		if (ps.getVelocityY() != null) builder.velocityY(ps.getVelocityY());
		if (ps.getVelocityZ() != null) builder.velocityZ(ps.getVelocityZ());
		
		return builder.build();
	}
	
	// -------------------------------------------- //
	// FIELDS: PLUS
	// -------------------------------------------- //
	
	public PS plusChunkCoords(int chunkX, int chunkZ)
	{
		PSBuilder builder = this.builder();
		
		if (builder.chunkX() != null)
		{
			builder.chunkX(builder.chunkX() + chunkX);
		}
		
		if (builder.chunkZ() != null)
		{
			builder.chunkZ(builder.chunkZ() + chunkZ);
		}
		
		return builder.build();
	}
	
	// -------------------------------------------- //
	// FIELDS: IS
	// -------------------------------------------- //
	
	public boolean isWorldLoadedOnThisServer()
	{
		if (this.world == null) return true;
		return MUtil.getLoadedWorldNames().contains(this.world);
	}
	
	// -------------------------------------------- //
	// PRIVATE CONSTRUCTOR
	// -------------------------------------------- //
	
	private PS(String worldName, Integer blockX, Integer blockY, Integer blockZ, Double locationX, Double locationY, Double locationZ, Integer chunkX, Integer chunkZ, Float pitch, Float yaw, Double velocityX, Double velocityY, Double velocityZ)
	{
		this.world = worldName;
		this.blockX = blockX;
		this.blockY = blockY;
		this.blockZ = blockZ;
		this.locationX = throwIfStrange(locationX, NAME_VERBOOSE_LOCATIONX);
		this.locationY = throwIfStrange(locationY, NAME_VERBOOSE_LOCATIONY);
		this.locationZ = throwIfStrange(locationZ, NAME_VERBOOSE_LOCATIONZ);
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.pitch = throwIfStrange(pitch, NAME_VERBOOSE_PITCH);
		this.yaw = throwIfStrange(yaw, NAME_VERBOOSE_YAW);
		this.velocityX = throwIfStrange(velocityX, NAME_VERBOOSE_VELOCITYX);
		this.velocityY = throwIfStrange(velocityY, NAME_VERBOOSE_VELOCITYY);
		this.velocityZ = throwIfStrange(velocityZ, NAME_VERBOOSE_VELOCITYZ);
	}
	
	public static Double throwIfStrange(Double d, String name)
	{
		if (d == null) return null;
		if (d.isInfinite()) throw new IllegalArgumentException(name + " should not be Infinite!");
		if (d.isNaN()) throw new IllegalArgumentException(name + " should not be NaN!");
		return d;
	}
	
	public static Float throwIfStrange(Float f, String name)
	{
		if (f == null) return null;
		if (f.isInfinite()) throw new IllegalArgumentException(name + " should not be Infinite!");
		if (f.isNaN()) throw new IllegalArgumentException(name + " should not be NaN!");
		return f;
	}
	
	// -------------------------------------------- //
	// BUILDER
	// -------------------------------------------- //
	
	public PSBuilder builder()
	{
		return new PSBuilder(this);
	}
	
	// -------------------------------------------- //
	// FACTORY: VALUE OF
	// -------------------------------------------- //
	
	public static PS valueOf(String world, Integer blockX, Integer blockY, Integer blockZ, Double locationX, Double locationY, Double locationZ, Integer chunkX, Integer chunkZ, Float pitch, Float yaw, Double velocityX, Double velocityY, Double velocityZ)
	{
		return new PS(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ);
	}
	
	public static PS valueOf(Location location)
	{
		if (location == null) return null;
		String world = calcWorldName(location.getWorld());
		Double locationX = location.getX();
		Double locationY = location.getY();
		Double locationZ = location.getZ();
		Float pitch = location.getPitch();
		Float yaw = location.getYaw();
		return valueOf(world, null, null, null, locationX, locationY, locationZ, null, null, pitch, yaw, null, null, null);
	}
	
	public static PS valueOf(Vector velocity)
	{
		if (velocity == null) return null;
		Double velocityX = velocity.getX();
		Double velocityY = velocity.getY();
		Double velocityZ = velocity.getZ();
		return valueOf(null, null, null, null, null, null, null, null, null, null, null, velocityX, velocityY, velocityZ);
	}
	
	public static PS valueOf(Entity entity)
	{
		if (entity == null) return null;
		
		Location location = entity.getLocation();
		String world = calcWorldName(location.getWorld());
		Double locationX = location.getX();
		Double locationY = location.getY();
		Double locationZ = location.getZ();
		Float pitch = location.getPitch();
		Float yaw = location.getYaw();
		
		Vector velocity = entity.getVelocity();
		Double velocityX = velocity.getX();
		Double velocityY = trimEntityVelocityY(velocity.getY());
		Double velocityZ = velocity.getZ();
		
		return valueOf(world, null, null, null, locationX, locationY, locationZ, null, null, pitch, yaw, velocityX, velocityY, velocityZ);
	}
	
	public static PS valueOf(Block block)
	{
		if (block == null) return null;
		String world = calcWorldName(block.getWorld());
		Integer blockX = block.getX();
		Integer blockY = block.getY();
		Integer blockZ = block.getZ();
		return valueOf(world, blockX, blockY, blockZ, null, null, null, null, null, null, null, null, null, null);
	}
	
	public static PS valueOf(String world, int chunkX, int chunkZ)
	{
		return valueOf(world, null, null, null, null, null, null, chunkX, chunkZ, null, null, null, null, null);
	}
	public static PS valueOf(Chunk chunk)
	{
		if (chunk == null) return null;
		String world = calcWorldName(chunk.getWorld());
		Integer chunkX = chunk.getX();
		Integer chunkZ = chunk.getZ();
		return valueOf(world, chunkX, chunkZ);
	}
	public static PS valueOf(int chunkX, int chunkZ)
	{
		return valueOf(null, null, null, null, null, null, null, chunkX, chunkZ, null, null, null, null, null);
	}
	
	
	public static PS valueOf(final JsonElement jsonElement)
	{
		if (jsonElement == null) return null;
		if (jsonElement.isJsonNull()) return null;
		
		final JsonObject jsonObject = jsonElement.getAsJsonObject();
		final PSBuilder builder = new PSBuilder();
		
		if (jsonObject.has("world") && jsonObject.has("yaw"))
		{
			// Old Faction LazyLocation
			for (Entry<String, JsonElement> entry : jsonObject.entrySet())
			{
				final String key = entry.getKey();
				final JsonElement value = entry.getValue();
				
				if (key.equals("world"))
				{
					builder.world(value.getAsString());
				}
				else if (key.equals("x"))
				{
					builder.locationX(value.getAsDouble());
				}
				else if (key.equals("y"))
				{
					builder.locationY(value.getAsDouble());
				}
				else if (key.equals("z"))
				{
					builder.locationZ(value.getAsDouble());
				}
				else if (key.equals("pitch"))
				{
					builder.pitch(value.getAsFloat());
				}
				else if (key.equals("yaw"))
				{
					builder.yaw(value.getAsFloat());
				}
			}
		}
		else
		{
			// The Standard Format
			for (Entry<String, JsonElement> entry : jsonObject.entrySet())
			{
				final String key = entry.getKey();
				final JsonElement value = entry.getValue();
				
				if (key.equals(NAME_SERIALIZED_WORLD))
				{
					builder.world(value.getAsString());
				}
				else if (key.equals(NAME_SERIALIZED_BLOCKX))
				{
					builder.blockX(value.getAsInt());
				}
				else if (key.equals(NAME_SERIALIZED_BLOCKY))
				{
					builder.blockY(value.getAsInt());
				}
				else if (key.equals(NAME_SERIALIZED_BLOCKZ))
				{
					builder.blockZ(value.getAsInt());
				}
				else if (key.equals(NAME_SERIALIZED_LOCATIONX))
				{
					builder.locationX(value.getAsDouble());
				}
				else if (key.equals(NAME_SERIALIZED_LOCATIONY))
				{
					builder.locationY(value.getAsDouble());
				}
				else if (key.equals(NAME_SERIALIZED_LOCATIONZ))
				{
					builder.locationZ(value.getAsDouble());
				}
				else if (key.equals(NAME_SERIALIZED_CHUNKX))
				{
					builder.chunkX(value.getAsInt());
				}
				else if (key.equals(NAME_SERIALIZED_CHUNKZ))
				{
					builder.chunkZ(value.getAsInt());	
				}
				else if (key.equals(NAME_SERIALIZED_PITCH))
				{
					builder.pitch(value.getAsFloat());
				}
				else if (key.equals(NAME_SERIALIZED_YAW))
				{
					builder.yaw(value.getAsFloat());
				}
				else if (key.equals(NAME_SERIALIZED_VELOCITYX))
				{
					builder.velocityX(value.getAsDouble());
				}
				else if (key.equals(NAME_SERIALIZED_VELOCITYY))
				{
					builder.velocityY(value.getAsDouble());
				}
				else if (key.equals(NAME_SERIALIZED_VELOCITYZ))
				{
					builder.velocityZ(value.getAsDouble());
				}
			}
		}
		
		return builder.build();
	}
	
	// -------------------------------------------- //
	// GET SINGLE FIELD (CALC FLAG INCLUDED)
	// -------------------------------------------- //
	
	public String getWorld(boolean calc) { return world; }
	public Integer getBlockX(boolean calc) { return getBlockCoord(calc, locationX, blockX, chunkX); }
	public Integer getBlockY(boolean calc) { return getBlockCoord(calc, locationY, blockY, null); }
	public Integer getBlockZ(boolean calc) { return getBlockCoord(calc, locationZ, blockZ, chunkZ); }
	public Double getLocationX(boolean calc) { return getLocationCoord(calc, locationX, blockX, chunkX); }
	public Double getLocationY(boolean calc) { return getLocationCoord(calc, locationY, blockY, null); }
	public Double getLocationZ(boolean calc) { return getLocationCoord(calc, locationZ, blockZ, chunkZ); }
	public Integer getChunkX(boolean calc) { return getChunkCoord(calc, locationX, blockX, chunkX); }
	public Integer getChunkZ(boolean calc) { return getChunkCoord(calc, locationZ, blockZ, chunkZ); }
	public Float getPitch(boolean calc) { return getPitch(calc, pitch); }
	public Float getYaw(boolean calc) { return getYaw(calc, yaw); }
	public Double getVelocityX(boolean calc) { return getVelocityCoord(calc, locationX, blockX, chunkX, velocityX); }
	public Double getVelocityY(boolean calc) { return getVelocityCoord(calc, locationY, blockY, null, velocityY); }
	public Double getVelocityZ(boolean calc) { return getVelocityCoord(calc, locationZ, blockZ, chunkZ, velocityZ); }
	
	public static Integer getBlockCoord(boolean calc, Double location, Integer block, Integer chunk) { if (calc) return calcBlockCoord(location, block, chunk); return block; }
	public static Double getLocationCoord(boolean calc, Double location, Integer block, Integer chunk) { if (calc) return calcLocationCoord(location, block, chunk); return location; }
	public static Integer getChunkCoord(boolean calc, Double location, Integer block, Integer chunk) { if (calc) return calcChunkCoord(location, block, chunk); return chunk; }
	public static Float getPitch(boolean calc, Float pitch) { if (calc) return calcPitch(pitch); return pitch; }
	public static Float getYaw(boolean calc, Float yaw) { if (calc) return calcYaw(yaw); return yaw; }
	public static Double getVelocityCoord(boolean calc, Double location, Integer block, Integer chunk, Double velocity) { if (calc) return calcVelocityCoord(location, block, chunk, velocity); return velocity; }
	
	public static Integer calcBlockCoord(Double location, Integer block, Integer chunk)
	{
		if (block != null) return block;
		if (location != null) return (int) Location.locToBlock(location);
		if (chunk != null) return chunk * 16;
		return null;
	}
	public static Double calcLocationCoord(Double location, Integer block, Integer chunk)
	{
		if (location != null) return location;
		if (block != null) return (double) block;
		if (chunk != null) return chunk * 16D;
		return null;
	}
	public static Integer calcChunkCoord(Double location, Integer block, Integer chunk)
	{
		if (chunk != null) return chunk;
		if (location != null) return Location.locToBlock(location) >> 4;
		if (block != null) return block >> 4;
		return null;
	}
	public static Float calcPitch(Float pitch)
	{
		if (pitch != null) return pitch;
		return DEFAULT_BUKKIT_PITCH;
	}
	public static Float calcYaw(Float yaw)
	{
		if (yaw != null) return yaw;
		return DEFAULT_BUKKIT_YAW;
	}
	public static Double calcVelocityCoord(Double location, Integer block, Integer chunk, Double velocity)
	{
		if (velocity != null) return velocity;
		if (location != null) return location;
		if (block != null) return (double) block;
		if (chunk != null) return chunk * 16D;
		return null;
	}
	
	// -------------------------------------------- //
	// GET FIELD GROUPS
	// -------------------------------------------- //
	
	public PS getVelocity() { return this.getVelocity(false); }
	public PS getVelocity(boolean calc)
	{
		return new PSBuilder()
		.velocityX(this.getVelocityX(calc))
		.velocityY(this.getVelocityY(calc))
		.velocityZ(this.getVelocityZ(calc))
		.build();
	}
	
	public PS getBlockCoords() { return this.getBlockCoords(false); }
	public PS getBlockCoords(boolean calc)
	{
		return new PSBuilder()
		.blockX(this.getBlockX(calc))
		.blockY(this.getBlockY(calc))
		.blockZ(this.getBlockZ(calc))
		.build();
	}
	
	public PS getLocationCoords() { return this.getLocationCoords(false); }
	public PS getLocationCoords(boolean calc)
	{
		return new PSBuilder()
		.locationX(this.getLocationX(calc))
		.locationY(this.getLocationY(calc))
		.locationZ(this.getLocationZ(calc))
		.build();
	}
	
	public PS getChunkCoords() { return this.getChunkCoords(false); }
	public PS getChunkCoords(boolean calc)
	{
		return new PSBuilder()
		.chunkX(this.getChunkX(calc))
		.chunkZ(this.getChunkZ(calc))
		.build();
	}
	
	public PS getHead() { return this.getHead(false); }
	public PS getHead(boolean calc)
	{
		return new PSBuilder()
		.pitch(this.getPitch(calc))
		.yaw(this.getYaw(calc))
		.build();
	}
	
	public PS getBlock() { return this.getBlock(false); }
	public PS getBlock(boolean calc)
	{
		return new PSBuilder()
		.world(this.getWorld(calc))
		.blockX(this.getBlockX(calc))
		.blockY(this.getBlockY(calc))
		.blockZ(this.getBlockZ(calc))
		.build();
	}
	
	public PS getLocation() { return this.getLocation(false); }
	public PS getLocation(boolean calc)
	{
		return new PSBuilder()
		.world(this.getWorld(calc))
		.locationX(this.getLocationX(calc))
		.locationY(this.getLocationY(calc))
		.locationZ(this.getLocationZ(calc))
		.pitch(this.getPitch(calc))
		.yaw(this.getYaw(calc))
		.build();
	}
	
	public PS getChunk() { return this.getChunk(false); }
	public PS getChunk(boolean calc)
	{
		return new PSBuilder()
		.world(this.getWorld(calc))
		.chunkX(this.getChunkX(calc))
		.chunkZ(this.getChunkZ(calc))
		.build();
	}
	
	public PS getEntity() { return this.getEntity(false); }
	public PS getEntity(boolean calc)
	{
		return new PSBuilder()
		.world(this.getWorld(calc))
		.locationX(this.getLocationX(calc))
		.locationY(this.getLocationY(calc))
		.locationZ(this.getLocationZ(calc))
		.pitch(this.getPitch(calc))
		.yaw(this.getYaw(calc))
		.velocityX(this.getVelocityX(false))
		.velocityY(this.getVelocityY(false))
		.velocityZ(this.getVelocityZ(false))
		.build();
	}
	
	// -------------------------------------------- //
	// AS BUKKIT EQUIVALENT
	// -------------------------------------------- //
	
	public World asBukkitWorld() throws IllegalStateException { return this.asBukkitWorld(false); }
	public World asBukkitWorld(boolean calc) throws IllegalStateException { return asBukkitWorld(this.getWorld(calc)); }
	
	public Block asBukkitBlock() throws IllegalStateException { return this.asBukkitBlock(false); }
	public Block asBukkitBlock(boolean calc) throws IllegalStateException { return asBukkitBlock(this.getBlock(calc)); }
	
	public Location asBukkitLocation() throws IllegalStateException { return this.asBukkitLocation(false); }
	public Location asBukkitLocation(boolean calc) throws IllegalStateException { return asBukkitLocation(this.getLocation(calc)); }
	
	public Chunk asBukkitChunk() throws IllegalStateException { return this.asBukkitChunk(false); }
	public Chunk asBukkitChunk(boolean calc) throws IllegalStateException { return asBukkitChunk(this.getChunk(calc)); }
	
	public Vector asBukkitVelocity() throws IllegalStateException { return this.asBukkitVelocity(false); }
	public Vector asBukkitVelocity(boolean calc) throws IllegalStateException { return asBukkitVelocity(this.getVelocity(calc)); }
	
	public static World asBukkitWorld(String world) throws IllegalStateException, NullPointerException
	{
		if (world == null) throw new NullPointerException(NAME_FULL_WORLD + SPACE_WASNT_SET);
		World ret = Bukkit.getWorld(world);
		if (ret == null) throw new IllegalStateException("the world "+world+" does not exist on "+MassiveCore.getServerId());
		return ret;
	}
	
	public static Block asBukkitBlock(PS ps) throws IllegalStateException
	{
		World world = ps.asBukkitWorld();
		
		Integer blockX = ps.getBlockX();
		if (blockX == null) throw new IllegalStateException(NAME_FULL_BLOCKX + SPACE_WASNT_SET);
		
		Integer blockY = ps.getBlockY();
		if (blockY == null) throw new IllegalStateException(NAME_FULL_BLOCKY + SPACE_WASNT_SET);
		
		Integer blockZ = ps.getBlockZ();
		if (blockZ == null) throw new IllegalStateException(NAME_FULL_BLOCKZ + SPACE_WASNT_SET);
		
		return world.getBlockAt(blockX, blockY, blockZ);
	}
	
	public static Location asBukkitLocation(PS ps) throws IllegalStateException
	{
		World world = ps.asBukkitWorld();
		
		Double locationX = ps.getLocationX();
		if (locationX == null) throw new IllegalStateException(NAME_FULL_LOCATIONX + SPACE_WASNT_SET);
		
		Double locationY = ps.getLocationY();
		if (locationY == null) throw new IllegalStateException(NAME_FULL_LOCATIONY + SPACE_WASNT_SET);
		
		Double locationZ = ps.getLocationZ();
		if (locationZ == null) throw new IllegalStateException(NAME_FULL_LOCATIONZ + SPACE_WASNT_SET);
		
		Float pitch = ps.getPitch();
		if (pitch == null) pitch = DEFAULT_BUKKIT_PITCH;
		
		Float yaw = ps.getYaw();
		if (yaw == null) yaw = DEFAULT_BUKKIT_YAW;
		
		return new Location(world, locationX, locationY, locationZ, yaw, pitch);
	}
	
	public static Chunk asBukkitChunk(PS ps) throws IllegalStateException
	{
		World world = ps.asBukkitWorld();
		
		Integer chunkX = ps.getChunkX();
		if (chunkX == null) throw new IllegalStateException(NAME_FULL_CHUNKX + SPACE_WASNT_SET);
		
		Integer chunkZ = ps.getChunkZ();
		if (chunkZ == null) throw new IllegalStateException(NAME_FULL_CHUNKZ + SPACE_WASNT_SET);
		
		return world.getChunkAt(chunkX, chunkZ);
	}
	
	public static Vector asBukkitVelocity(PS ps) throws IllegalStateException
	{
		Double velocityX = ps.getVelocityX();
		if (velocityX == null) throw new IllegalStateException(NAME_FULL_VELOCITYX + SPACE_WASNT_SET);
		
		Double velocityY = ps.getVelocityY();
		if (velocityY == null) throw new IllegalStateException(NAME_FULL_VELOCITYY + SPACE_WASNT_SET);
		
		Double velocityZ = ps.getVelocityZ();
		if (velocityZ == null) throw new IllegalStateException(NAME_FULL_VELOCITYZ + SPACE_WASNT_SET);
		
		return new Vector(velocityX, velocityY, velocityZ);
	}
	
	// -------------------------------------------- //
	// ASSORTED
	// -------------------------------------------- //
	
	public static String calcWorldName(World world)
	{
		if (world == null) return null;
		return world.getName();
	}
	
	// Because of something in the physics engine players actually
	// have a small negative velocityY even when standing still.
	// We remove this redundant small negative value.
	public static Double trimEntityVelocityY(Double velocityY)
	{
		if (velocityY == null) return null;
		if (velocityY >= 0) return velocityY;
		if (velocityY < -0.1D) return velocityY;
		return 0D;
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return this.toString(PSFormatFormal.get());
	}
	
	public String toString(PSFormat format)
	{
		return format.format(this);
	}
	
	public static String toString(PS ps, PSFormat format)
	{
		return format.format(ps);
	}
	
	// -------------------------------------------- //
	// PARTIAL COMPARES
	// -------------------------------------------- //
	
	public static Double locationDistanceSquared(PS one, PS two)
	{
		if (one == null) return null;
		if (two == null) return null;
		
		String w1 = one.getWorld();
		String w2 = two.getWorld();
		
		if (!MUtil.equals(w1, w2)) return null;
		
		Double x1 = one.getLocationX(true);
		if (x1 == null) return null;
		
		Double y1 = one.getLocationY(true);
		if (y1 == null) return null;
		
		Double z1 = one.getLocationZ(true);
		if (z1 == null) return null;
		
		Double x2 = two.getLocationX(true);
		if (x2 == null) return null;
		
		Double y2 = two.getLocationY(true);
		if (y2 == null) return null;
		
		Double z2 = two.getLocationZ(true);
		if (z2 == null) return null;
		
		return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2);
	}
	
	public static Double locationDistance(PS one, PS two)
	{
		Double ret = locationDistanceSquared(one, two);
		if (ret == null) return null;
		return Math.sqrt(ret);
	}
	
	public static boolean inSameWorld(PS one, PS two)
	{
		if (one == null) return false;
		if (two == null) return false;
		
		String w1 = one.getWorld();
		String w2 = two.getWorld();
		
		if (w1 == null) return false;
		if (w2 == null) return false;
		
		return w1.equalsIgnoreCase(w2);
	}
	
	public static boolean inSameUniverse(PS one, PS two, Multiverse multiverse)
	{
		if (one == null) return false;
		if (two == null) return false;
		
		String w1 = one.getWorld();
		String w2 = two.getWorld();
		
		if (w1 == null) return false;
		if (w2 == null) return false;
		
		String m1 = multiverse.getUniverseForWorldName(w1);
		String m2 = multiverse.getUniverseForWorldName(w2);
		
		return m1.equalsIgnoreCase(m2);
	}

	public static boolean inSameUniverse(PS one, PS two, Aspect aspect)
	{
		return inSameUniverse(one, two, aspect.getMultiverse());
	}
	
	// -------------------------------------------- //
	// GET SETS
	// -------------------------------------------- //
	
	public static Set<PS> getDistinctChunks(Collection<PS> pss)
	{
		Set<PS> ret = new LinkedHashSet<PS>();
		for (PS ps : pss)
		{
			ret.add(ps.getChunk(true));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// HASHCODE (CACHED)
	// -------------------------------------------- //
	
	private transient volatile boolean hashed = false;
	private transient volatile int hashcode = 0;
	
	@Override
	public int hashCode()
	{
		if (!this.hashed)
		{
			this.hashcode = this.calcHashCode();
			this.hashed = true;
		}
		return this.hashcode;
	}
	
	public int calcHashCode()
	{
		return PS.calcHashCode(this);
	}
	
	public static int calcHashCode(PS ps)
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ps.blockX == null) ? 0 : ps.blockX.hashCode());
		result = prime * result + ((ps.blockY == null) ? 0 : ps.blockY.hashCode());
		result = prime * result + ((ps.blockZ == null) ? 0 : ps.blockZ.hashCode());
		result = prime * result + ((ps.chunkX == null) ? 0 : ps.chunkX.hashCode());
		result = prime * result + ((ps.chunkZ == null) ? 0 : ps.chunkZ.hashCode());
		result = prime * result + ((ps.locationX == null) ? 0 : ps.locationX.hashCode());
		result = prime * result + ((ps.locationY == null) ? 0 : ps.locationY.hashCode());
		result = prime * result + ((ps.locationZ == null) ? 0 : ps.locationZ.hashCode());
		result = prime * result + ((ps.pitch == null) ? 0 : ps.pitch.hashCode());
		result = prime * result + ((ps.velocityX == null) ? 0 : ps.velocityX.hashCode());
		result = prime * result + ((ps.velocityY == null) ? 0 : ps.velocityY.hashCode());
		result = prime * result + ((ps.velocityZ == null) ? 0 : ps.velocityZ.hashCode());
		result = prime * result + ((ps.world == null) ? 0 : ps.world.hashCode());
		result = prime * result + ((ps.yaw == null) ? 0 : ps.yaw.hashCode());
		return result;
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object derpObject)
	{
		return equals(this, derpObject);
	}

	public static boolean equals(PS herp, Object derpObject)
	{
		return compareTo(herp, derpObject) == 0;
	}
	
	// -------------------------------------------- //
	// COMPARE
	// -------------------------------------------- //
	
	@Override
	public int compareTo(PS derp)
	{
		return compareTo(this, derp);
	}
	
	public static int compareTo(PS herp, Object derpObject)
	{
		if (herp == null && derpObject == null) return 0;
		if (herp == null) return -1;
		if (derpObject == null) return +1;
		
		if (!(derpObject instanceof PS)) return -1;
		PS derp = (PS) derpObject;
		
		int ret;
		
		ret = MUtil.compare(herp.world, derp.world);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.chunkX, derp.chunkX);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.chunkZ, derp.chunkZ);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.blockX, derp.blockX);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.blockY, derp.blockY);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.blockZ, derp.blockZ);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.locationX, derp.locationX);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.locationX, derp.locationX);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.locationZ, derp.locationZ);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.pitch, derp.pitch);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.yaw, derp.yaw);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.velocityX, derp.velocityX);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.velocityY, derp.velocityY);
		if (ret != 0) return ret;
		
		ret = MUtil.compare(herp.velocityZ, derp.velocityZ);
		if (ret != 0) return ret;
		
		return 0;
	}
	
	// -------------------------------------------- //
	// CLONE
	// -------------------------------------------- //
	
	@Override
	public PS clone()
	{
		return this;
	}
	
}
