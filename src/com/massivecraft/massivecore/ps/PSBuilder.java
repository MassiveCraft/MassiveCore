package com.massivecraft.massivecore.ps;

import org.bukkit.World;

public class PSBuilder
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String world = null;
	public String world() { return this.world; }
	public PSBuilder world(String worldName) { this.world = worldName; return this; }
	public PSBuilder world(World world) { this.world = PS.calcWorldName(world); return this; }
	
	private Integer blockX = null;
	public Integer blockX() { return this.blockX; }
	public PSBuilder blockX(Integer blockX) { this.blockX = blockX; return this; }
	
	private Integer blockY = null;
	public Integer blockY() { return this.blockY; }
	public PSBuilder blockY(Integer blockY) { this.blockY = blockY; return this; }
	
	private Integer blockZ = null;
	public Integer blockZ() { return this.blockZ; }
	public PSBuilder blockZ(Integer blockZ) { this.blockZ = blockZ; return this; }
	
	private Double locationX = null;
	public Double locationX() { return this.locationX; }
	public PSBuilder locationX(Double locationX) { this.locationX = locationX; return this; }
	
	private Double locationY = null;
	public Double locationY() { return this.locationY; }
	public PSBuilder locationY(Double locationY) { this.locationY = locationY; return this; }
	
	private Double locationZ = null;
	public Double locationZ() { return this.locationZ; }
	public PSBuilder locationZ(Double locationZ) { this.locationZ = locationZ; return this; }
	
	private Integer chunkX = null;
	public Integer chunkX() { return this.chunkX; }
	public PSBuilder chunkX(Integer chunkX) { this.chunkX = chunkX; return this; }
	
	private Integer chunkZ = null;
	public Integer chunkZ() { return this.chunkZ; }
	public PSBuilder chunkZ(Integer chunkZ) { this.chunkZ = chunkZ; return this; }
	
	private Float pitch = null;
	public Float pitch() { return this.pitch; }
	public PSBuilder pitch(Float pitch) { this.pitch = pitch; return this; }
	
	private Float yaw = null;
	public Float yaw() { return this.yaw; }
	public PSBuilder yaw(Float yaw) { this.yaw = yaw; return this; }
	
	private Double velocityX = null;
	public Double velocityX() { return this.velocityX; }
	public PSBuilder velocityX(Double velocityX) { this.velocityX = velocityX; return this; }
	
	private Double velocityY = null;
	public Double velocityY() { return this.velocityY; }
	public PSBuilder velocityY(Double velocityY) { this.velocityY = velocityY; return this; }
	
	private Double velocityZ = null;
	public Double velocityZ() { return this.velocityZ; }
	public PSBuilder velocityZ(Double velocityZ) { this.velocityZ = velocityZ; return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PSBuilder(String world, Integer blockX, Integer blockY, Integer blockZ, Double locationX, Double locationY, Double locationZ, Integer chunkX, Integer chunkZ, Float pitch, Float yaw, Double velocityX, Double velocityY, Double velocityZ)
	{
		this.world = world;
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
	
	public PSBuilder(PS ps)
	{
		this(ps.getWorld(), ps.getBlockX(), ps.getBlockY(), ps.getBlockZ(), ps.getLocationX(), ps.getLocationY(), ps.getLocationZ(), ps.getChunkX(), ps.getChunkZ(), ps.getPitch(), ps.getYaw(), ps.getVelocityX(), ps.getVelocityY(), ps.getVelocityZ());
	}
	
	public PSBuilder()
	{
		
	}
	
	// -------------------------------------------- //
	// BUILD
	// -------------------------------------------- //
	
	public PS build()
	{
		return PS.valueOf(world, blockX, blockY, blockZ, locationX, locationY, locationZ, chunkX, chunkZ, pitch, yaw, velocityX, velocityY, velocityZ);
	}
	
}
