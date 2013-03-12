package com.massivecraft.mcore;

import java.io.Serializable;

import com.massivecraft.mcore.xlib.gson.annotations.SerializedName;

public final class PS2 implements Serializable
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final transient long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	@SerializedName("w")
	private final String worldName;
	public String getWorldName() { return this.worldName; }
	
	@SerializedName("bx")
	private final Integer blockX;
	public Integer getBlockX() { return this.blockX; }
	
	@SerializedName("by")
	private final Integer blockY;
	public Integer getBlockY() { return this.blockY; }
	
	@SerializedName("bz")
	private final Integer blockZ;
	public Integer getBlockZ() { return this.blockZ; }
	
	@SerializedName("lx")
	private final Double locationX;
	public Double getLocationX() { return this.locationX; }
	
	@SerializedName("ly")
	private final Double locationY;
	public Double getLocationY() { return this.locationY; }
	
	@SerializedName("lz")
	private final Double locationZ;
	public Double getLocationZ() { return this.locationZ; }
	
	@SerializedName("cx")
	private final Integer chunkX;
	public Integer getChunkX() { return this.chunkX; }
	
	@SerializedName("cz")
	private final Integer chunkZ;
	public Integer getChunkZ() { return this.chunkZ; }
	
	@SerializedName("p")
	private final Float pitch;
	public Float getPitch() { return this.pitch; }
	
	@SerializedName("y")
	private final Float yaw;
	public Float getYaw() { return this.yaw; }
	
	@SerializedName("vx")
	private final Double velocityX;
	public Double getVelocityX() { return this.velocityX; }
	
	@SerializedName("vy")
	private final Double velocityY;
	public Double getVelocityY() { return this.velocityY; }
	
	@SerializedName("vz")
	private final Double velocityZ;
	public Double getVelocityZ() { return this.velocityZ; }
	
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
