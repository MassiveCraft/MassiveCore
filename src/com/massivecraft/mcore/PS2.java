package com.massivecraft.mcore;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.massivecraft.mcore.xlib.gson.annotations.SerializedName;

public final class PS2 implements Serializable
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final transient long serialVersionUID = 1L;
	
	public static final transient String SERIALIZED_NAME_WORLDNAME = "w";
	public static final transient String SERIALIZED_NAME_BLOCKX = "bx";
	public static final transient String SERIALIZED_NAME_BLOCKY = "by";
	public static final transient String SERIALIZED_NAME_BLOCKZ = "bz";
	public static final transient String SERIALIZED_NAME_LOCATIONX = "lx";
	public static final transient String SERIALIZED_NAME_LOCATIONY = "ly";
	public static final transient String SERIALIZED_NAME_LOCATIONZ = "lz";
	public static final transient String SERIALIZED_NAME_CHUNKX = "cx";
	public static final transient String SERIALIZED_NAME_CHUNKZ = "cz";
	public static final transient String SERIALIZED_NAME_PITCH = "p";
	public static final transient String SERIALIZED_NAME_YAW = "y";
	public static final transient String SERIALIZED_NAME_VELOCITYX = "vx";
	public static final transient String SERIALIZED_NAME_VELOCITYY = "vy";
	public static final transient String SERIALIZED_NAME_VELOCITYZ = "vz";
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	@SerializedName(SERIALIZED_NAME_WORLDNAME)
	private final String worldName;
	public String getWorldName() { return this.worldName; }
	
	@SerializedName(SERIALIZED_NAME_BLOCKX)
	private final Integer blockX;
	public Integer getBlockX() { return this.blockX; }
	
	@SerializedName(SERIALIZED_NAME_BLOCKY)
	private final Integer blockY;
	public Integer getBlockY() { return this.blockY; }
	
	@SerializedName(SERIALIZED_NAME_BLOCKZ)
	private final Integer blockZ;
	public Integer getBlockZ() { return this.blockZ; }
	
	@SerializedName(SERIALIZED_NAME_LOCATIONX)
	private final Double locationX;
	public Double getLocationX() { return this.locationX; }
	
	@SerializedName(SERIALIZED_NAME_LOCATIONY)
	private final Double locationY;
	public Double getLocationY() { return this.locationY; }
	
	@SerializedName(SERIALIZED_NAME_LOCATIONZ)
	private final Double locationZ;
	public Double getLocationZ() { return this.locationZ; }
	
	@SerializedName(SERIALIZED_NAME_CHUNKX)
	private final Integer chunkX;
	public Integer getChunkX() { return this.chunkX; }
	
	@SerializedName(SERIALIZED_NAME_CHUNKZ)
	private final Integer chunkZ;
	public Integer getChunkZ() { return this.chunkZ; }
	
	@SerializedName(SERIALIZED_NAME_PITCH)
	private final Float pitch;
	public Float getPitch() { return this.pitch; }
	
	@SerializedName(SERIALIZED_NAME_YAW)
	private final Float yaw;
	public Float getYaw() { return this.yaw; }
	
	@SerializedName(SERIALIZED_NAME_VELOCITYX)
	private final Double velocityX;
	public Double getVelocityX() { return this.velocityX; }
	
	@SerializedName(SERIALIZED_NAME_VELOCITYY)
	private final Double velocityY;
	public Double getVelocityY() { return this.velocityY; }
	
	@SerializedName(SERIALIZED_NAME_VELOCITYZ)
	private final Double velocityZ;
	public Double getVelocityZ() { return this.velocityZ; }
	
	// -------------------------------------------- //
	// FIELDS: RAW FAKE
	// -------------------------------------------- //
	
	public World getWorld()
	{
		return calcWorld(this.worldName);
	}
	
	public static String calcWorldName(World world)
	{
		if (world == null) return null;
		return world.getName();
	}
	
	public static World calcWorld(String worldName)
	{
		if (worldName == null) return null;
		return Bukkit.getWorld(worldName);
	}
	
	// -------------------------------------------- //
	// PRIVATE CONSTRUCTOR
	// -------------------------------------------- //
	
	private PS2(String worldName, Integer blockX, Integer blockY, Integer blockZ, Double locationX, Double locationY, Double locationZ, Integer chunkX, Integer chunkZ, Float pitch, Float yaw, Double velocityX, Double velocityY, Double velocityZ)
	{
		this.worldName = worldName;
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
	// FIELDS: WITH
	// -------------------------------------------- //
	
	public PS2 withWorldName(String worldName) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withWorld(World world) { return new PS2(calcWorldName(world), blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withBlockX(Integer blockX) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withBlockY(Integer blockY) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withBlockZ(Integer blockZ) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withLocationX(Double locationX) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withLocationY(Double locationY) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withLocationZ(Double locationZ) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withChunkX(Integer chunkX) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withChunkZ(Integer chunkZ) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withPitch(Float pitch) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withYaw(Float yaw) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withVelocityX(Double velocityX) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withVelocityY(Double velocityY) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	public PS2 withVelocityZ(Double velocityZ) { return new PS2(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ); }
	
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
		result = prime * result + ((ps.worldName == null) ? 0 : ps.worldName.hashCode());
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
		if (ps.worldName == null)
		{
			if (derp.worldName != null) return false;
		}
		else if (!ps.worldName.equals(derp.worldName)) return false;
		if (ps.yaw == null)
		{
			if (derp.yaw != null) return false;
		}
		else if (!ps.yaw.equals(derp.yaw)) return false;
		return true;
	}
	
	
}
