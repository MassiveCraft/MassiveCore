package com.massivecraft.mcore;

import java.io.Serializable;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonObject;
import com.massivecraft.mcore.xlib.gson.annotations.SerializedName;

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

public final class PS2 implements Cloneable, Serializable
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
	public static final transient String NAME_FULL_YAW = "yay";
	public static final transient String NAME_FULL_VELOCITYX = "velocityX";
	public static final transient String NAME_FULL_VELOCITYY = "velocityY";
	public static final transient String NAME_FULL_VELOCITYZ = "velocityZ";
	
	// -------------------------------------------- //
	// STANDARD INSTANCES
	// -------------------------------------------- //
	
	public static final transient PS2 NULL = new PS2(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	
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
	
	public PS2 withWorld(String world) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withWorld(World world) { return new PS2(calcWorldName(world), blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withBlockX(Integer blockX) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withBlockY(Integer blockY) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withBlockZ(Integer blockZ) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withLocationX(Double locationX) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withLocationY(Double locationY) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withLocationZ(Double locationZ) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withChunkX(Integer chunkX) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withChunkZ(Integer chunkZ) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withPitch(Float pitch) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withYaw(Float yaw) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withVelocityX(Double velocityX) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withVelocityY(Double velocityY) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withVelocityZ(Double velocityZ) { return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	
	public PS2 with(PS2 ps)
	{
		PS2Builder builder = this.builder();
		
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
	// PRIVATE CONSTRUCTOR
	// -------------------------------------------- //
	
	private PS2(String worldName, Integer blockX, Integer blockY, Integer blockZ, Double locationX, Double locationY, Double locationZ, Integer chunkX, Integer chunkZ, Float pitch, Float yaw, Double velocityX, Double velocityY, Double velocityZ)
	{
		this.world = worldName;
		this.blockX = blockX;
		this.blockY = blockY;
		this.blockZ = blockZ;
		this.locationX = locationX;
		this.locationY = locationY;
		this.locationZ = locationZ;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.pitch = pitch;
		this.yaw = yaw;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
	}
	
	// -------------------------------------------- //
	// BUILDER
	// -------------------------------------------- //
	
	public PS2Builder builder()
	{
		return new PS2Builder(this);
	}
	
	// -------------------------------------------- //
	// FACTORY: VALUE OF
	// -------------------------------------------- //
	
	public static PS2 valueOf(String world, Integer blockX, Integer blockY, Integer blockZ, Double locationX, Double locationY, Double locationZ, Integer chunkX, Integer chunkZ, Float pitch, Float yaw, Double velocityX, Double velocityY, Double velocityZ)
	{
		return new PS2(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ);
	}
	
	public static PS2 valueOf(Location location)
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
	
	public static PS2 valueOf(Vector velocity)
	{
		if (velocity == null) return null;
		Double velocityX = velocity.getX();
		Double velocityY = velocity.getY();
		Double velocityZ = velocity.getZ();
		return valueOf(null, null, null, null, null, null, null, null, null, null, null, velocityX, velocityY, velocityZ);
	}
	
	public static PS2 valueOf(Entity entity)
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
		Double velocityY = velocity.getY();
		Double velocityZ = velocity.getZ();
		
		return valueOf(world, null, null, null, locationX, locationY, locationZ, null, null, pitch, yaw, velocityX, velocityY, velocityZ);
	}
	
	public static PS2 valueOf(Block block)
	{
		if (block == null) return null;
		String world = calcWorldName(block.getWorld());
		Integer blockX = block.getX();
		Integer blockY = block.getY();
		Integer blockZ = block.getZ();
		return valueOf(world, blockX, blockY, blockZ, null, null, null, null, null, null, null, null, null, null);
	}
	
	public static PS2 valueOf(Chunk chunk)
	{
		if (chunk == null) return null;
		String world = calcWorldName(chunk.getWorld());
		Integer chunkX = chunk.getX();
		Integer chunkZ = chunk.getZ();
		return valueOf(world, null, null, null, null, null, null, chunkX, chunkZ, null, null, null, null, null);
	}
	
	public static PS2 valueOf(final JsonElement jsonElement)
	{
		if (jsonElement == null) return null;
		if (jsonElement.isJsonNull()) return null;
		
		final JsonObject jsonObject = jsonElement.getAsJsonObject();
		final PS2Builder builder = new PS2Builder();
		for (Entry<String, JsonElement> entry : jsonObject.entrySet())
		{
			final String key = entry.getKey();
			final JsonElement value = entry.getValue();
			
			switch(key)
			{
				case NAME_SERIALIZED_WORLD:
					builder.world(value.getAsString());
				break;
				case NAME_SERIALIZED_BLOCKX:
					builder.blockX(value.getAsInt());
				break;
				case NAME_SERIALIZED_BLOCKY:
					builder.blockY(value.getAsInt());
				break;
				case NAME_SERIALIZED_BLOCKZ:
					builder.blockZ(value.getAsInt());
				break;
				case NAME_SERIALIZED_LOCATIONX:
					builder.locationX(value.getAsDouble());
				break;
				case NAME_SERIALIZED_LOCATIONY:
					builder.locationY(value.getAsDouble());
				break;
				case NAME_SERIALIZED_LOCATIONZ:
					builder.locationZ(value.getAsDouble());
				break;
				case NAME_SERIALIZED_CHUNKX:
					builder.chunkX(value.getAsInt());
				break;
				case NAME_SERIALIZED_CHUNKZ:
					builder.chunkZ(value.getAsInt());
				break;
				case NAME_SERIALIZED_PITCH:
					builder.pitch(value.getAsFloat());
				break;
				case NAME_SERIALIZED_YAW:
					builder.yaw(value.getAsFloat());
				break;
				case NAME_SERIALIZED_VELOCITYX:
					builder.velocityX(value.getAsDouble());
				break;
				case NAME_SERIALIZED_VELOCITYY:
					builder.velocityY(value.getAsDouble());
				break;
				case NAME_SERIALIZED_VELOCITYZ:
					builder.velocityZ(value.getAsDouble());
				break;
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
		if (location != null) return (int) Math.floor(location);
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
		if (location != null) return location.intValue() >> 4;
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
	
	public PS2 getVelocity() { return this.getVelocity(false); }
	public PS2 getVelocity(boolean calc)
	{
		return new PS2Builder()
		.velocityX(this.getVelocityX(calc))
		.velocityY(this.getVelocityY(calc))
		.velocityZ(this.getVelocityZ(calc))
		.build();
	}
	
	public PS2 getBlockCoords() { return this.getBlockCoords(false); }
	public PS2 getBlockCoords(boolean calc)
	{
		return new PS2Builder()
		.blockX(this.getBlockX(calc))
		.blockY(this.getBlockY(calc))
		.blockZ(this.getBlockZ(calc))
		.build();
	}
	
	public PS2 getLocationCoords() { return this.getLocationCoords(false); }
	public PS2 getLocationCoords(boolean calc)
	{
		return new PS2Builder()
		.locationX(this.getLocationX(calc))
		.locationY(this.getLocationY(calc))
		.locationZ(this.getLocationZ(calc))
		.build();
	}
	
	public PS2 getChunkCoords() { return this.getChunkCoords(false); }
	public PS2 getChunkCoords(boolean calc)
	{
		return new PS2Builder()
		.chunkX(this.getChunkX(calc))
		.chunkZ(this.getChunkZ(calc))
		.build();
	}
	
	public PS2 getHead() { return this.getHead(false); }
	public PS2 getHead(boolean calc)
	{
		return new PS2Builder()
		.pitch(this.getPitch(calc))
		.yaw(this.getYaw(calc))
		.build();
	}
	
	public PS2 getBlock() { return this.getBlock(false); }
	public PS2 getBlock(boolean calc)
	{
		return new PS2Builder()
		.world(this.getWorld(calc))
		.blockX(this.getBlockX(calc))
		.blockY(this.getBlockY(calc))
		.blockZ(this.getBlockZ(calc))
		.build();
	}
	
	public PS2 getLocation() { return this.getLocation(false); }
	public PS2 getLocation(boolean calc)
	{
		return new PS2Builder()
		.world(this.getWorld(calc))
		.locationX(this.getLocationX(calc))
		.locationY(this.getLocationY(calc))
		.locationZ(this.getLocationZ(calc))
		.pitch(this.getPitch(calc))
		.yaw(this.getYaw(calc))
		.build();
	}
	
	public PS2 getChunk() { return this.getChunk(false); }
	public PS2 getChunk(boolean calc)
	{
		return new PS2Builder()
		.world(this.getWorld(calc))
		.chunkX(this.getChunkX(calc))
		.chunkZ(this.getChunkZ(calc))
		.build();
	}
	
	public PS2 getEntity() { return this.getEntity(false); }
	public PS2 getEntity(boolean calc)
	{
		return new PS2Builder()
		.world(this.getWorld(calc))
		.locationX(this.getLocationX(calc))
		.locationY(this.getLocationY(calc))
		.locationZ(this.getLocationZ(calc))
		.pitch(this.getPitch(calc))
		.yaw(this.getYaw(calc))
		.velocityX(this.getVelocityX(calc))
		.velocityY(this.getVelocityY(calc))
		.velocityZ(this.getVelocityZ(calc))
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
		if (world == null) throw new NullPointerException("world wasn't set");
		World ret = Bukkit.getWorld(world);
		if (ret == null) throw new IllegalStateException("the world "+world+" does not exist on "+MCore.getServerId());
		return ret;
	}
	
	public static Block asBukkitBlock(PS2 ps) throws IllegalStateException
	{
		World world = ps.asBukkitWorld();
		
		Integer blockX = ps.getBlockX();
		if (blockX == null) throw new IllegalStateException("blockX wasn't set");
		
		Integer blockY = ps.getBlockY();
		if (blockY == null) throw new IllegalStateException("blockY wasn't set");
		
		Integer blockZ = ps.getBlockZ();
		if (blockZ == null) throw new IllegalStateException("blockZ wasn't set");
		
		return world.getBlockAt(blockX, blockY, blockZ);
	}
	
	public static Location asBukkitLocation(PS2 ps) throws IllegalStateException
	{
		World world = ps.asBukkitWorld();
		
		Double locationX = ps.getLocationX();
		if (locationX == null) throw new IllegalStateException("locationX wasn't set");
		
		Double locationY = ps.getLocationY();
		if (locationY == null) throw new IllegalStateException("locationY wasn't set");
		
		Double locationZ = ps.getLocationZ();
		if (locationZ == null) throw new IllegalStateException("locationZ wasn't set");
		
		Float pitch = ps.getPitch();
		if (pitch == null) pitch = DEFAULT_BUKKIT_PITCH;
		
		Float yaw = ps.getYaw();
		if (yaw == null) yaw = DEFAULT_BUKKIT_YAW;
		
		return new Location(world, locationX, locationY, locationZ, yaw, pitch);
	}
	
	public static Chunk asBukkitChunk(PS2 ps) throws IllegalStateException
	{
		World world = ps.asBukkitWorld();
		
		Integer chunkX = ps.getChunkX();
		if (chunkX == null) throw new IllegalStateException("chunkX wasn't set");
		
		Integer chunkZ = ps.getChunkZ();
		if (chunkZ == null) throw new IllegalStateException("chunkZ wasn't set");
		
		return world.getChunkAt(chunkX, chunkZ);
	}
	
	public static Vector asBukkitVelocity(PS2 ps) throws IllegalStateException
	{
		Double velocityX = ps.getVelocityX();
		if (velocityX == null) throw new IllegalStateException("velocityX wasn't set");
		
		Double velocityY = ps.getVelocityY();
		if (velocityY == null) throw new IllegalStateException("velocityY wasn't set");
		
		Double velocityZ = ps.getVelocityZ();
		if (velocityZ == null) throw new IllegalStateException("velocityZ wasn't set");
		
		return new Vector(velocityX, velocityY, velocityZ);
	}
	
	// -------------------------------------------- //
	// ASSORTED
	// -------------------------------------------- //
	
	// TODO: Malplaced?
	public static String calcWorldName(World world)
	{
		if (world == null) return null;
		return world.getName();
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
		return PS2.calcHashCode(this);
	}
	
	public static int calcHashCode(PS2 ps)
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
	public boolean equals(Object obj)
	{
		return PS2.equals(this, obj);
	}

	public static boolean equals(PS2 ps, Object obj)
	{
		if (ps == obj) return true;
		if (ps == null) return false;
		if (obj == null) return false;
		if (!(obj instanceof PS2)) return false;
		PS2 derp = (PS2) obj;
		
		if (ps.blockX == null)
		{
			if (derp.blockX != null) return false;
		}
		else if (!ps.blockX.equals(derp.blockX)) return false;
		if (ps.blockY == null)
		{
			if (derp.blockY != null) return false;
		}
		else if (!ps.blockY.equals(derp.blockY)) return false;
		if (ps.blockZ == null)
		{
			if (derp.blockZ != null) return false;
		}
		else if (!ps.blockZ.equals(derp.blockZ)) return false;
		if (ps.chunkX == null)
		{
			if (derp.chunkX != null) return false;
		}
		else if (!ps.chunkX.equals(derp.chunkX)) return false;
		if (ps.chunkZ == null)
		{
			if (derp.chunkZ != null) return false;
		}
		else if (!ps.chunkZ.equals(derp.chunkZ)) return false;
		if (ps.locationX == null)
		{
			if (derp.locationX != null) return false;
		}
		else if (!ps.locationX.equals(derp.locationX)) return false;
		if (ps.locationY == null)
		{
			if (derp.locationY != null) return false;
		}
		else if (!ps.locationY.equals(derp.locationY)) return false;
		if (ps.locationZ == null)
		{
			if (derp.locationZ != null) return false;
		}
		else if (!ps.locationZ.equals(derp.locationZ)) return false;
		if (ps.pitch == null)
		{
			if (derp.pitch != null) return false;
		}
		else if (!ps.pitch.equals(derp.pitch)) return false;
		if (ps.velocityX == null)
		{
			if (derp.velocityX != null) return false;
		}
		else if (!ps.velocityX.equals(derp.velocityX)) return false;
		if (ps.velocityY == null)
		{
			if (derp.velocityY != null) return false;
		}
		else if (!ps.velocityY.equals(derp.velocityY)) return false;
		if (ps.velocityZ == null)
		{
			if (derp.velocityZ != null) return false;
		}
		else if (!ps.velocityZ.equals(derp.velocityZ)) return false;
		if (ps.world == null)
		{
			if (derp.world != null) return false;
		}
		else if (!ps.world.equals(derp.world)) return false;
		if (ps.yaw == null)
		{
			if (derp.yaw != null) return false;
		}
		else if (!ps.yaw.equals(derp.yaw)) return false;
		return true;
	}
	
	// -------------------------------------------- //
	// CLONE
	// -------------------------------------------- //
	
	@Override
	protected PS2 clone()
	{
		return this;
	}
	
}
