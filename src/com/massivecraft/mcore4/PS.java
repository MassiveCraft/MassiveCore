package com.massivecraft.mcore4;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.massivecraft.mcore4.store.accessor.Accessor;
import com.massivecraft.mcore4.xlib.gson.annotations.SerializedName;

/**
 * PS stands for PhysicalState.
 * This class stores data related to just that.
 * When coding plugins you may find yourself wanting to store a player location.
 * Another time you may want to store the player location but without the worldName info.
 * Another time you may want to store pitch and yaw only.
 * This class is supposed to be usable in all those cases.
 * Hopefully this class will save you from implementing special classes for all those compinations.
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
	public World world() { return Bukkit.getWorld(this.worldName); }
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
	public PS pitch(Float val) { this.pitch = val; return this; }
	public Float pitch() { return this.pitch; }
	
	// Field: yaw
	@SerializedName("y")
	protected Float yaw;
	public PS yaw(Float val) { this.yaw = val; return this; }
	public Float yaw() { return this.yaw; }
	
	// ---------------------
	
	// Field: motionX
	@SerializedName("mx")
	protected Double motionX;
	public PS motionX(Double val) { this.motionX = val; return this; }
	public Double motionX() { return this.motionX; }
	
	// Field: motionY
	@SerializedName("my")
	protected Double motionY;
	public PS motionY(Double val) { this.motionY = val; return this; }
	public Double motionY() { return this.motionY; }
	
	// Field: motionZ
	@SerializedName("mz")
	protected Double motionZ;
	public PS motionZ(Double val) { this.motionZ = val; return this; }
	public Double motionZ() { return this.motionZ; }
	
	//----------------------------------------------//
	// CONVERTERS
	//----------------------------------------------//
	
	public synchronized Location asLocation()
	{
		World world = this.world();
		
		Double x = this.locationXCalc();
		if (x == null) return null;
		
		Double y = this.locationYCalc();
		if (y == null) return null;
		
		Double z = this.locationZCalc();
		if (z == null) return null;
		
		Float pitch = this.pitch();
		if (pitch == null) pitch = 0F;
		
		Float yaw = this.yaw();
		if (yaw == null) yaw = 0F;
		
		return new Location(world, x, y, z, pitch, yaw);
	}
	
	public synchronized Block asBlock()
	{
		World world = this.world();
		if (world == null) return null;
		
		Integer x = this.blockXCalc();
		if (x == null) return null;
		
		Integer y = this.blockYCalc();
		if (y == null) return null;
		
		Integer z = this.blockZCalc();
		if (z == null) return null;
		
		return world.getBlockAt(x, y, z);
	}
	
	public synchronized Chunk asChunk()
	{
		World world = this.world();
		if (world == null) return null;
		
		Integer x = this.chunkXCalc();
		if (x == null) return null;
		
		Integer z = this.chunkZCalc();
		if (z == null) return null;
		
		return world.getChunkAt(x, z);
	}
	
	//----------------------------------------------//
	// LOADERS
	//----------------------------------------------//
	
	public synchronized PS loadDefault()
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
		
		this.motionX = null;
		this.motionY = null;
		this.motionZ = null;
		
		return this;
	}
	
	public synchronized PS loadTransparent(PS ps)
	{
		Accessor.get(PS.class).copy(ps, this, true);
		return this;
	}
	
	public synchronized PS load(PS ps)
	{
		Accessor.get(PS.class).copy(ps, this);
		return this;
	}

	// ---------------------
	
	public synchronized PS load(Location location)
	{
		return this.loadDefault().loadTransparent(location);
	}
	
	public synchronized PS loadTransparent(Location location)
	{
		this.worldName = location.getWorld().getName();
		this.locationX = location.getX();
		this.locationY = location.getY();
		this.locationZ = location.getZ();
		this.pitch = location.getPitch();
		this.yaw = location.getYaw();
		
		return this;
	}
	
	// ---------------------
	
	public synchronized PS load(Block block)
	{
		return this.loadDefault().loadTransparent(block);
	}
	
	public synchronized PS loadTransparent(Block block)
	{
		this.worldName = block.getWorld().getName();
		this.blockX = block.getX();
		this.blockY = block.getY();
		this.blockZ = block.getZ();
		return this;
	}
	
	// ---------------------
	
	public synchronized PS load(Chunk chunk)
	{
		return this.loadDefault().loadTransparent(chunk);
	}
	
	public synchronized PS loadTransparent(Chunk chunk)
	{
		this.worldName = chunk.getWorld().getName();
		this.chunkX = chunk.getX();
		this.chunkZ = chunk.getZ();
		return this;
	}
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	public PS()
	{
		
	}
	
	public PS(PS ps)
	{
		this.load(ps);
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
		result = prime * result + ((motionX == null) ? 0 : motionX.hashCode());
		result = prime * result + ((motionY == null) ? 0 : motionY.hashCode());
		result = prime * result + ((motionZ == null) ? 0 : motionZ.hashCode());
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
		if (motionX == null)
		{
			if (other.motionX != null) return false;
		}
		else if (!motionX.equals(other.motionX)) return false;
		if (motionY == null)
		{
			if (other.motionY != null) return false;
		}
		else if (!motionY.equals(other.motionY)) return false;
		if (motionZ == null)
		{
			if (other.motionZ != null) return false;
		}
		else if (!motionZ.equals(other.motionZ)) return false;
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
