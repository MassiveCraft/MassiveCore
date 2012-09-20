package com.massivecraft.mcore4;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.mcore4.store.accessor.Accessor;
import com.massivecraft.mcore4.xlib.gson.annotations.SerializedName;

/**
 * PS stands for PhysicalState.
 * This class stores data related to just that.
 * When coding plugins you may find yourself wanting to store a player location.
 * Another time you may want to store the player location but without the worldName info.
 * Another time you may want to store pitch and yaw only.
 * This class is supposed to be usable in all those cases.
 * Hopefully this class will save you from implementing special classes for all those combinations.
 */
public class PS implements Cloneable
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Field: worldName
	@SerializedName("w")
	protected String worldName;
	public String worldName() { return this.worldName; }
	public void worldName(String val) { this.worldName = val; }
	
	// FakeField: world
	public World world()
	{
		if (this.worldName == null) return null;
		return Bukkit.getWorld(this.worldName);
	}
	public PS world(World val) { this.worldName = val.getName(); return this; }
	
	// ---------------------
	
	// Field: blockX
	@SerializedName("bx")
	protected Integer blockX;
	public PS blockX(Integer val) { this.blockX = val; return this; }
	public Integer blockX() { return this.blockX; }
	public Integer blockXCalc()
	{
		return blockCalc(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: blockY
	@SerializedName("by")
	protected Integer blockY;
	public PS blockY(Integer val) { this.blockY = val; return this; }
	public Integer blockY() { return this.blockY; }
	public Integer blockYCalc()
	{
		return blockCalc(this.locationY, this.blockY, null);
	}
	
	// Field: blockZ
	@SerializedName("bz")
	protected Integer blockZ;
	public PS blockZ(Integer val) { this.blockZ = val; return this; }
	public Integer blockZ() { return this.blockZ; }
	public Integer blockZCalc()
	{
		return blockCalc(this.locationZ, this.blockZ, this.chunkZ);
	}
	
	protected static synchronized Integer blockCalc(Double location, Integer block, Integer chunk)
	{
		if (block != null) return block;
		if (location != null) return (int) Math.floor(location);
		if (chunk != null) return chunk * 16;
		return null;
	}
	
	// ---------------------
	
	// Field: locationX
	@SerializedName("lx")
	protected Double locationX;
	public PS locationX(Double val) { this.locationX = val; return this; }
	public Double locationX() { return this.locationX; }
	public Double locationXCalc()
	{
		return locationCalc(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: locationY
	@SerializedName("ly")
	protected Double locationY;
	public PS locationY(Double val) { this.locationY = val; return this; }
	public Double locationY() { return this.locationY; }
	public Double locationYCalc()
	{
		return locationCalc(this.locationY, this.blockY, null);
	}
	
	// Field: locationZ
	@SerializedName("lz")
	protected Double locationZ;
	public PS locationZ(Double val) { this.locationZ = val; return this; }
	public Double locationZ() { return this.locationZ; }
	public Double locationZCalc()
	{
		return locationCalc(this.locationZ, this.blockZ, this.chunkZ);
	}
	
	protected static synchronized Double locationCalc(Double location, Integer block, Integer chunk)
	{
		if (location != null) return location;
		if (block != null) return (double) block;
		if (chunk != null) return chunk * 16D;
		return null;
	}
	
	// ---------------------
	
	// Field: chunkX
	@SerializedName("cx")
	protected Integer chunkX;
	public PS chunkX(Integer val) { this.chunkX = val; return this; }
	public Integer chunkX() { return this.chunkX; }
	public Integer chunkXCalc()
	{
		return chunkCalc(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: chunkZ
	@SerializedName("xz")
	protected Integer chunkZ;
	public PS chunkZ(Integer val) { this.chunkZ = val; return this; }
	public Integer chunkZ() { return this.chunkZ; }
	public Integer chunkZCalc()
	{
		return chunkCalc(this.locationZ, this.blockZ, this.chunkZ);
	}
	
	protected static synchronized Integer chunkCalc(Double location, Integer block, Integer chunk)
	{
		if (chunk != null) return chunk;
		if (location != null) return location.intValue() >> 4;
		if (block != null) return block >> 4;
		return null;
	}
	
	// ---------------------
	
	// Field: pitch
	@SerializedName("p")
	protected Float pitch;
	public PS pitch(Float val) 
	{
		if (val == null)
		{
			this.pitch = null;
		}
		else
		{
			this.pitch = (val + 360F) % 360F;
		}
		return this;
	}
	public Float pitch() { return this.pitch; }
	
	// Field: yaw
	@SerializedName("y")
	protected Float yaw;
	public PS yaw(Float val) { this.yaw = val; return this; }
	public Float yaw() { return this.yaw; }
	
	// ---------------------
	
	// Field: velocityX
	@SerializedName("vx")
	protected Double velocityX;
	public PS velocityX(Double val) { this.velocityX = val; return this; }
	public Double velocityX() { return this.velocityX; }
	public Double velocityXCalc()
	{
		return velocityCalc(this.locationX, this.blockX, this.chunkX, this.velocityX);
	}
	
	// Field: velocityY
	@SerializedName("vy")
	protected Double velocityY;
	public PS velocityY(Double val) { this.velocityY = val; return this; }
	public Double velocityY() { return this.velocityY; }
	public Double velocityYCalc()
	{
		return velocityCalc(this.locationY, this.blockY, 0, this.velocityY);
	}
	
	// Field: velocityZ
	@SerializedName("vz")
	protected Double velocityZ;
	public PS velocityZ(Double val) { this.velocityZ = val; return this; }
	public Double velocityZ() { return this.velocityZ; }
	public Double velocityZCalc()
	{
		return velocityCalc(this.locationZ, this.blockZ, this.chunkZ, this.velocityZ);
	}
	
	protected static synchronized Double velocityCalc(Double location, Integer block, Integer chunk, Double velocity)
	{
		if (velocity != null) return velocity;
		if (location != null) return location;
		if (block != null) return (double) block;
		if (chunk != null) return chunk * 16D;
		return null;
	}
	
	//----------------------------------------------//
	// CONVERTERS
	//----------------------------------------------//
	
	public synchronized Location location()
	{
		return this.locationInner(this.locationX(), this.locationY(), this.locationZ());
	}
	public synchronized Location locationCalc()
	{
		return this.locationInner(this.locationXCalc(), this.locationYCalc(), this.locationZCalc());
	}
	protected synchronized Location locationInner(Double x, Double y, Double z)
	{
		World world = this.world();
		
		if (x == null) return null;
		if (y == null) return null;
		if (z == null) return null;
		
		Float pitch = this.pitch();
		if (pitch == null) pitch = 0F;
		
		Float yaw = this.yaw();
		if (yaw == null) yaw = 0F;
		
		return new Location(world, x, y, z, pitch, yaw);
	}
	
	public synchronized Block block()
	{
		return this.blockInner(this.blockX(), this.blockY(), this.blockZ());
	}
	public synchronized Block blockCalc()
	{
		return this.blockInner(this.blockXCalc(), this.blockYCalc(), this.blockZCalc());
	}
	public synchronized Block blockInner(Integer x, Integer y, Integer z)
	{
		World world = this.world();
		if (world == null) return null;
		
		if (x == null) return null;
		if (y == null) return null;
		if (z == null) return null;
		
		return world.getBlockAt(x, y, z);
	}
	
	public synchronized Chunk chunk()
	{
		return this.chunkInner(this.chunkX(), this.chunkZ());
	}
	public synchronized Chunk chunkCalc()
	{
		return this.chunkInner(this.chunkXCalc(), this.chunkZCalc());
	}
	public synchronized Chunk chunkInner(Integer x, Integer z)
	{
		World world = this.world();
		if (world == null) return null;
		
		if (x == null) return null;
		if (z == null) return null;
		
		return world.getChunkAt(x, z);
	}
	
	public synchronized Vector velocity()
	{
		return this.velocityInner(this.velocityX(), this.velocityY(), this.velocityZ());
	}
	public synchronized Vector velocityCalc()
	{
		return this.velocityInner(this.velocityXCalc(), this.velocityYCalc(), this.velocityZCalc());
	}
	public synchronized Vector velocityInner(Double x, Double y, Double z)
	{
		if (x == null) return null;
		if (y == null) return null;
		if (z == null) return null;
		return new Vector(x, y, z);
	}
	
	//----------------------------------------------//
	// READERS
	//----------------------------------------------//
	
	public synchronized PS readDefault()
	{
		this.worldName = null;
		
		this.blockX = null;
		this.blockY = null;
		this.blockZ = null;
		
		this.locationX = null;
		this.locationY = null;
		this.locationZ = null;
		
		this.chunkX = null;
		this.chunkZ = null;
		
		this.pitch = null;
		this.yaw = null;
		
		this.velocityX = null;
		this.velocityY = null;
		this.velocityZ = null;
		
		return this;
	}
	
	public synchronized PS readTransparent(PS ps)
	{
		Accessor.get(PS.class).copy(ps, this, true);
		return this;
	}
	
	public synchronized PS read(PS ps)
	{
		Accessor.get(PS.class).copy(ps, this);
		return this;
	}
	
	// ---------------------
	
	public synchronized PS read(Location location)
	{
		return this.readDefault().readTransparent(location);
	}
	
	public synchronized PS readTransparent(Location location)
	{
		this.worldName = location.getWorld().getName();
		this.locationX = location.getX();
		this.locationY = location.getY();
		this.locationZ = location.getZ();
		this.pitch(location.getPitch());
		this.yaw = location.getYaw();
		
		return this;
	}
	
	// ---------------------
	
	public synchronized PS read(Vector vector)
	{
		return this.readDefault().readTransparent(vector);
	}
	
	public synchronized PS readTransparent(Vector vector)
	{
		this.velocityX = vector.getX();
		this.velocityY = vector.getY();
		this.velocityZ = vector.getZ();
		
		return this;
	}
	
	// ---------------------
	
	public synchronized PS read(Player player)
	{
		return this.readDefault().readTransparent(player);
	}
	
	public synchronized PS readTransparent(Player player)
	{
		this.readTransparent(player.getLocation());
		this.readTransparent(player.getVelocity());
		return this;
	}
	
	// ---------------------
	
	public synchronized PS read(Block block)
	{
		return this.readDefault().readTransparent(block);
	}
	
	public synchronized PS readTransparent(Block block)
	{
		this.worldName = block.getWorld().getName();
		this.blockX = block.getX();
		this.blockY = block.getY();
		this.blockZ = block.getZ();
		return this;
	}
	
	// ---------------------
	
	public synchronized PS read(Chunk chunk)
	{
		return this.readDefault().readTransparent(chunk);
	}
	
	public synchronized PS readTransparent(Chunk chunk)
	{
		this.worldName = chunk.getWorld().getName();
		this.chunkX = chunk.getX();
		this.chunkZ = chunk.getZ();
		return this;
	}
	
	//----------------------------------------------//
	// WRITERS
	//----------------------------------------------//
	
	public synchronized void write(Player player)
	{
		teleporter.teleport(player, this);
	}
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	public PS()
	{
		
	}
	
	public PS(PS ps)
	{
		this.read(ps);
	}
	
	public PS(Location location)
	{
		this.read(location);
	}
	
	public PS(Vector vector)
	{
		this.read(vector);
	}
	
	public PS(Player player)
	{
		this.read(player);
	}
	
	public PS(Block block)
	{
		this.read(block);
	}
	
	public PS(Chunk chunk)
	{
		this.read(chunk);
	}
	
	//----------------------------------------------//
	// TO STRING
	//----------------------------------------------//
	
    @Override
    public synchronized String toString()
    {
    	return this.getClass().getSimpleName()+MCore.gson.toJson(this);
    } 
	
	//----------------------------------------------//
	// CLONE
	//----------------------------------------------//
	
	@Override
	public PS clone()
	{
		return new PS(this);
	}
	
	// -------------------------------------------- //
	// TELEPORTER
	// -------------------------------------------- //
	
	public static transient PSTeleporter teleporter = PSTeleporterDefault.get();
	
	//----------------------------------------------//
	// COMPARISON
	//----------------------------------------------//
	// These were autogenerated using eclipse.
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockX == null) ? 0 : blockX.hashCode());
		result = prime * result + ((blockY == null) ? 0 : blockY.hashCode());
		result = prime * result + ((blockZ == null) ? 0 : blockZ.hashCode());
		result = prime * result + ((chunkX == null) ? 0 : chunkX.hashCode());
		result = prime * result + ((chunkZ == null) ? 0 : chunkZ.hashCode());
		result = prime * result	+ ((locationX == null) ? 0 : locationX.hashCode());
		result = prime * result	+ ((locationY == null) ? 0 : locationY.hashCode());
		result = prime * result	+ ((locationZ == null) ? 0 : locationZ.hashCode());
		result = prime * result + ((velocityX == null) ? 0 : velocityX.hashCode());
		result = prime * result + ((velocityY == null) ? 0 : velocityY.hashCode());
		result = prime * result + ((velocityZ == null) ? 0 : velocityZ.hashCode());
		result = prime * result + ((pitch == null) ? 0 : pitch.hashCode());
		result = prime * result	+ ((worldName == null) ? 0 : worldName.hashCode());
		result = prime * result + ((yaw == null) ? 0 : yaw.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PS other = (PS) obj;
		if (blockX == null)
		{
			if (other.blockX != null) return false;
		}
		else if (!blockX.equals(other.blockX)) return false;
		if (blockY == null)
		{
			if (other.blockY != null) return false;
		}
		else if (!blockY.equals(other.blockY)) return false;
		if (blockZ == null)
		{
			if (other.blockZ != null) return false;
		}
		else if (!blockZ.equals(other.blockZ)) return false;
		if (chunkX == null)
		{
			if (other.chunkX != null) return false;
		}
		else if (!chunkX.equals(other.chunkX)) return false;
		if (chunkZ == null)
		{
			if (other.chunkZ != null) return false;
		}
		else if (!chunkZ.equals(other.chunkZ)) return false;
		if (locationX == null)
		{
			if (other.locationX != null) return false;
		}
		else if (!locationX.equals(other.locationX)) return false;
		if (locationY == null)
		{
			if (other.locationY != null) return false;
		}
		else if (!locationY.equals(other.locationY)) return false;
		if (locationZ == null)
		{
			if (other.locationZ != null) return false;
		}
		else if (!locationZ.equals(other.locationZ)) return false;
		if (velocityX == null)
		{
			if (other.velocityX != null) return false;
		}
		else if (!velocityX.equals(other.velocityX)) return false;
		if (velocityY == null)
		{
			if (other.velocityY != null) return false;
		}
		else if (!velocityY.equals(other.velocityY)) return false;
		if (velocityZ == null)
		{
			if (other.velocityZ != null) return false;
		}
		else if (!velocityZ.equals(other.velocityZ)) return false;
		if (pitch == null)
		{
			if (other.pitch != null) return false;
		}
		else if (!pitch.equals(other.pitch)) return false;
		if (worldName == null)
		{
			if (other.worldName != null) return false;
		}
		else if (!worldName.equals(other.worldName)) return false;
		if (yaw == null)
		{
			if (other.yaw != null) return false;
		}
		else if (!yaw.equals(other.yaw)) return false;
		return true;
	}

}
