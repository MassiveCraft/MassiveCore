package com.massivecraft.mcore;

import org.bukkit.World;

public class PS2Builder
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String worldName = null;
	public String worldName() { return this.worldName; }
	public World world() { return PS2.calcWorld(this.worldName); }
	public PS2Builder worldName(String worldName) { this.worldName = worldName; return this; }
	public PS2Builder world(World world) { this.worldName = PS2.calcWorldName(world); return this; }
	
	private Integer blockX = null;
	public Integer blockX() { return this.blockX; }
	public PS2Builder blockX(Integer blockX) { this.blockX = blockX; return this; }
	
	private Integer blockY = null;
	public Integer blockY() { return this.blockY; }
	public PS2Builder blockY(Integer blockY) { this.blockY = blockY; return this; }
	
	private Integer blockZ = null;
	public Integer blockZ() { return this.blockZ; }
	public PS2Builder blockZ(Integer blockZ) { this.blockZ = blockZ; return this; }
	
	private Double locationX = null;
	public Double locationX() { return this.locationX; }
	public PS2Builder locationX(Double locationX) { this.locationX = locationX; return this; }
	
	private Double locationY = null;
	public Double locationY() { return this.locationY; }
	public PS2Builder locationY(Double locationY) { this.locationY = locationY; return this; }
	
	private Double locationZ = null;
	public Double locationZ() { return this.locationZ; }
	public PS2Builder locationZ(Double locationZ) { this.locationZ = locationZ; return this; }
	
	private Integer chunkX = null;
	public Integer chunkX() { return this.chunkX; }
	public PS2Builder chunkX(Integer chunkX) { this.chunkX = chunkX; return this; }
	
	private Integer chunkZ = null;
	public Integer chunkZ() { return this.chunkZ; }
	public PS2Builder chunkZ(Integer chunkZ) { this.chunkZ = chunkZ; return this; }
	
	private Float pitch = null;
	public Float pitch() { return this.pitch; }
	public PS2Builder pitch(Float pitch) { this.pitch = pitch; return this; }
	
	private Float yaw = null;
	public Float yaw() { return this.yaw; }
	public PS2Builder yaw(Float yaw) { this.yaw = yaw; return this; }
	
	private Double velocityX = null;
	public Double velocityX() { return this.velocityX; }
	public PS2Builder velocityX(Double velocityX) { this.velocityX = velocityX; return this; }
	
	private Double velocityY = null;
	public Double velocityY() { return this.velocityY; }
	public PS2Builder velocityY(Double velocityY) { this.velocityY = velocityY; return this; }
	
	private Double velocityZ = null;
	public Double velocityZ() { return this.velocityZ; }
	public PS2Builder velocityZ(Double velocityZ) { this.velocityZ = velocityZ; return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PS2Builder(String worldName, Integer blockX, Integer blockY, Integer blockZ, Double locationX, Double locationY, Double locationZ, Integer chunkX, Integer chunkZ, Float pitch, Float yaw, Double velocityX, Double velocityY, Double velocityZ)
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
	
	public PS2Builder(PS2 ps)
	{
		this(ps.getWorldName(), ps.getBlockX(), ps.getBlockY(), ps.getBlockZ(), ps.getLocationX(), ps.getLocationY(), ps.getLocationZ(), ps.getChunkX(), ps.getChunkZ(), ps.getPitch(), ps.getYaw(), ps.getVelocityX(), ps.getVelocityY(), ps.getVelocityZ());
	}
	
	public PS2Builder()
	{
		
	}
	
	// -------------------------------------------- //
	// BUILD
	// -------------------------------------------- //
	
	public PS2 build()
	{
		return PS2.valueOf(worldName, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ);
	}
	
}
