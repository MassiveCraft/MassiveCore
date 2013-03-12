package com.massivecraft.mcore;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.mixin.TeleportMixinDefault;
import com.massivecraft.mcore.mixin.TeleporterException;
import com.massivecraft.mcore.usys.Aspect;
import com.massivecraft.mcore.usys.Multiverse;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.Txt;
import com.massivecraft.mcore.xlib.gson.annotations.SerializedName;

/**
 * PS stands for PhysicalState.
 * This class stores data related to just that.
 * When coding plugins you may find yourself wanting to store a player location.
 * Another time you may want to store the player location but without the worldName info.
 * Another time you may want to store pitch and yaw only.
 * This class is supposed to be usable in all those cases.
 * Hopefully this class will save you from implementing special classes for all those combinations.
 */

public class PS implements Cloneable, Serializable
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final transient long serialVersionUID = 1L;
	
	public static final transient String UNKNOWN_WORLD_NAME = "?";
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Field: worldName
	@SerializedName("w")
	protected String worldName = null;
	public String getWorldName() { return this.worldName; }
	public void setWorldName(String worldName) { this.worldName = worldName; }
	
	// FakeField: world
	public World getWorld()
	{
		if (this.worldName == null) return null;
		return Bukkit.getWorld(this.worldName);
	}
	public void setWorld(World val)
	{
		this.worldName = val.getName();
	}
	
	// ---------------------
	
	// Field: blockX
	@SerializedName("bx")
	protected Integer blockX = null;
	public Integer getBlockX() { return this.blockX; }
	public void setBlockX(Integer blockX) { this.blockX = blockX; }
	
	public Integer calcBlockX()
	{
		return calcBlock(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: blockY
	@SerializedName("by")
	protected Integer blockY = null;
	public Integer getBlockY() { return this.blockY; }
	public void setBlockY(Integer blockY) { this.blockY = blockY; }
	
	public Integer calcBlockY()
	{
		return calcBlock(this.locationY, this.blockY, null);
	}
	
	// Field: blockZ
	@SerializedName("bz")
	protected Integer blockZ = null;
	public Integer getBlockZ() { return this.blockZ; }
	public void setBlockZ(Integer blockZ) { this.blockZ = blockZ; }
	
	public Integer calcBlockZ()
	{
		return calcBlock(this.locationZ, this.blockZ, this.chunkZ);
	}
	
	protected static synchronized Integer calcBlock(Double location, Integer block, Integer chunk)
	{
		if (block != null) return block;
		if (location != null) return (int) Math.floor(location);
		if (chunk != null) return chunk * 16;
		return null;
	}
	
	// ---------------------
	
	// Field: locationX
	@SerializedName("lx")
	protected Double locationX = null;
	public Double getLocationX() { return this.locationX; }
	public void setLocationX(Double locationX) { this.locationX = locationX; }
	
	public Double calcLocationX()
	{
		return calcLocation(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: locationY
	@SerializedName("ly")
	protected Double locationY = null;
	public Double getLocationY() { return this.locationY; }
	public void setLocationY(Double locationY) { this.locationY = locationY; }
	
	public Double calcLocationY()
	{
		return calcLocation(this.locationY, this.blockY, null);
	}
	
	// Field: locationZ
	@SerializedName("lz")
	protected Double locationZ = null;
	public Double getLocationZ() { return this.locationZ; }
	public void setLocationZ(Double locationZ) { this.locationZ = locationZ; }
	
	public Double calcLocationZ()
	{
		return calcLocation(this.locationZ, this.blockZ, this.chunkZ);
	}
	
	protected static synchronized Double calcLocation(Double location, Integer block, Integer chunk)
	{
		if (location != null) return location;
		if (block != null) return (double) block;
		if (chunk != null) return chunk * 16D;
		return null;
	}
	
	// ---------------------
	
	// Field: chunkX
	@SerializedName("cx")
	protected Integer chunkX = null;
	public Integer getChunkX() { return this.chunkX; }
	public void setChunkX(Integer chunkX) { this.chunkX = chunkX; }
	
	public Integer calcChunkX()
	{
		return calcChunk(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: chunkZ
	@SerializedName("cz")
	protected Integer chunkZ = null;
	public Integer getChunkZ() { return this.chunkZ; }
	public void setChunkZ(Integer chunkZ) { this.chunkZ = chunkZ; }
	
	public Integer calcChunkZ()
	{
		return calcChunk(this.locationZ, this.blockZ, this.chunkZ);
	}
	
	protected static synchronized Integer calcChunk(Double location, Integer block, Integer chunk)
	{
		if (chunk != null) return chunk;
		if (location != null) return location.intValue() >> 4;
		if (block != null) return block >> 4;
		return null;
	}
	
	// ---------------------
	
	// Field: pitch
	@SerializedName("p")
	protected Float pitch = null;
	public Float getPitch() { return this.pitch; }
	public void setPitch(Float pitch) { this.pitch = pitch; }
	
	// Field: yaw
	@SerializedName("y")
	protected Float yaw = null;
	public Float getYaw() { return this.yaw; }
	public void setYaw(Float yaw) { this.yaw = yaw; }
	
	// ---------------------
	
	// Field: velocityX
	@SerializedName("vx")
	protected Double velocityX = null;
	public Double getVelocityX() { return this.velocityX; }
	public void setVelocityX(Double velocityX) { this.velocityX = velocityX; }
	
	public Double calcVelocityX()
	{
		return calcVelocity(this.locationX, this.blockX, this.chunkX, this.velocityX);
	}
	
	// Field: velocityY
	@SerializedName("vy")
	protected Double velocityY = null;
	public Double getVelocityY() { return this.velocityY; }
	public void setVelocityY(Double velocityY) { this.velocityY = velocityY; }
	
	public Double calcVelocityY()
	{
		return calcVelocity(this.locationY, this.blockY, 0, this.velocityY);
	}
	
	// Field: velocityZ
	@SerializedName("vz")
	protected Double velocityZ = null;
	public Double getVelocityZ() { return this.velocityZ; }
	public void setVelocityZ(Double velocityZ) { this.velocityZ = velocityZ; }
	
	public Double calcVelocityZ()
	{
		return calcVelocity(this.locationZ, this.blockZ, this.chunkZ, this.velocityZ);
	}
	
	protected static synchronized Double calcVelocity(Double location, Integer block, Integer chunk, Double velocity)
	{
		if (velocity != null) return velocity;
		if (location != null) return location;
		if (block != null) return (double) block;
		if (chunk != null) return chunk * 16D;
		return null;
	}
	
	//----------------------------------------------//
	// GET / CALC
	//----------------------------------------------//
	
	public synchronized Location getLocation()
	{
		return this.innerLocation(this.getLocationX(), this.getLocationY(), this.getLocationZ());
	}
	public synchronized Location calcLocation()
	{
		return this.innerLocation(this.calcLocationX(), this.calcLocationY(), this.calcLocationZ());
	}
	protected synchronized Location innerLocation(Double x, Double y, Double z)
	{
		World world = this.getWorld();
		if (world == null) return null;
		
		if (x == null) return null;
		if (y == null) return null;
		if (z == null) return null;
		
		Float pitch = this.getPitch();
		if (pitch == null) pitch = 0F;
		
		Float yaw = this.getYaw();
		if (yaw == null) yaw = 0F;
		
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	public synchronized Block getBlock()
	{
		return this.innerBlock(this.getBlockX(), this.getBlockY(), this.getBlockZ());
	}
	public synchronized Block calcBlock()
	{
		return this.innerBlock(this.calcBlockX(), this.calcBlockY(), this.calcBlockZ());
	}
	protected synchronized Block innerBlock(Integer x, Integer y, Integer z)
	{
		World world = this.getWorld();
		if (world == null) return null;
		
		if (x == null) return null;
		if (y == null) return null;
		if (z == null) return null;
		
		return world.getBlockAt(x, y, z);
	}
	
	public synchronized Chunk getChunk()
	{
		return this.innerChunk(this.getChunkX(), this.getChunkZ());
	}
	public synchronized Chunk calcChunk()
	{
		return this.innerChunk(this.calcChunkX(), this.calcChunkZ());
	}
	protected synchronized Chunk innerChunk(Integer x, Integer z)
	{
		World world = this.getWorld();
		if (world == null) return null;
		
		if (x == null) return null;
		if (z == null) return null;
		
		return world.getChunkAt(x, z);
	}
	
	public synchronized Vector getVelocity()
	{
		return this.innerVelocity(this.getVelocityX(), this.getVelocityY(), this.getVelocityZ());
	}
	public synchronized Vector calcVelocity()
	{
		return this.innerVelocity(this.calcVelocityX(), this.calcVelocityY(), this.calcVelocityZ());
	}
	protected synchronized Vector innerVelocity(Double x, Double y, Double z)
	{
		if (x == null) return null;
		if (y == null) return null;
		if (z == null) return null;
		return new Vector(x, y, z);
	}
	
	//----------------------------------------------//
	// SET
	//----------------------------------------------//
	
	public synchronized void setDefault()
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
	}
	
	public synchronized void setPSTransparent(PS ps)
	{
		if (ps.worldName != null) this.worldName = ps.worldName;
		
		if (ps.blockX != null) this.blockX = ps.blockX;
		if (ps.blockY != null) this.blockY = ps.blockY;
		if (ps.blockZ != null) this.blockZ = ps.blockZ;
		
		if (ps.locationX != null) this.locationX = ps.locationX;
		if (ps.locationY != null) this.locationY = ps.locationY;
		if (ps.locationZ != null) this.locationZ = ps.locationZ;
		
		if (ps.chunkX != null) this.chunkX = ps.chunkX;
		if (ps.chunkZ != null) this.chunkZ = ps.chunkZ;
		
		if (ps.pitch != null) this.pitch = ps.pitch;
		if (ps.yaw != null) this.yaw = ps.yaw;
		
		if (ps.velocityX != null) this.velocityX = ps.velocityX;
		if (ps.velocityY != null) this.velocityY = ps.velocityY;
		if (ps.velocityZ != null) this.velocityZ = ps.velocityZ;
	}
	
	public synchronized void setPS(PS ps)
	{
		this.worldName = ps.worldName;
		
		this.blockX = ps.blockX;
		this.blockY = ps.blockY;
		this.blockZ = ps.blockZ;
		
		this.locationX = ps.locationX;
		this.locationY = ps.locationY;
		this.locationZ = ps.locationZ;
		
		this.chunkX = ps.chunkX;
		this.chunkZ = ps.chunkZ;
		
		this.pitch = ps.pitch;
		this.yaw = ps.yaw;
		
		this.velocityX = ps.velocityX;
		this.velocityY = ps.velocityY;
		this.velocityZ = ps.velocityZ;
	}
	
	// ---------------------
	
	public synchronized void setLocation(Location location)
	{
		this.setDefault();
		this.setLocationTransparent(location);
	}
	
	public synchronized void setLocationTransparent(Location location)
	{
		this.worldName = location.getWorld().getName();
		this.locationX = location.getX();
		this.locationY = location.getY();
		this.locationZ = location.getZ();
		this.setPitch(location.getPitch());
		this.yaw = location.getYaw();
	}
	
	// ---------------------
	
	public synchronized void setVelocity(Vector vector)
	{
		this.setDefault();
		this.setVelocityTransparent(vector);
	}
	
	public synchronized void setVelocityTransparent(Vector vector)
	{
		this.velocityX = vector.getX();
		this.velocityY = vector.getY();
		this.velocityZ = vector.getZ();
	}
	
	// ---------------------
	
	public synchronized void setEntity(Entity entity)
	{
		this.setDefault();
		this.setEntityTransparent(entity);
	}
	
	public synchronized void setEntityTransparent(Entity entity)
	{
		this.setLocationTransparent(entity.getLocation());
		this.setVelocityTransparent(entity.getVelocity());
	}
	
	// ---------------------
	
	public synchronized void setBlock(Block block)
	{
		this.setDefault();
		this.setBlockTransparent(block);
	}
	
	public synchronized void setBlockTransparent(Block block)
	{
		this.worldName = block.getWorld().getName();
		this.blockX = block.getX();
		this.blockY = block.getY();
		this.blockZ = block.getZ();
	}
	
	// ---------------------
	
	public synchronized void setChunk(Chunk chunk)
	{
		this.setDefault();
		this.setChunkTransparent(chunk);
	}
	
	public synchronized void setChunkTransparent(Chunk chunk)
	{
		this.worldName = chunk.getWorld().getName();
		this.chunkX = chunk.getX();
		this.chunkZ = chunk.getZ();
	}
	
	// ---------------------

	public synchronized void setOldString(String str)
	{
		this.setDefault();
		this.setOldStringTransparent(str);
	}
	
	public synchronized void setOldStringTransparent(String str)
	{
		String[] parts = str.split("\\|");
		
		if (parts.length == 4)
		{
			this.worldName = parts[0];
			this.blockX = Integer.parseInt(parts[1]);
			this.blockY = Integer.parseInt(parts[2]);
			this.blockZ = Integer.parseInt(parts[3]);
		}
		else if (parts.length == 6)
		{
			this.worldName = parts[0];
			this.locationX = Double.parseDouble(parts[1]);
			this.locationY = Double.parseDouble(parts[2]);
			this.locationZ = Double.parseDouble(parts[3]);
			this.pitch = Float.parseFloat(parts[4]);
			this.yaw = Float.parseFloat(parts[5]);
		}
	}
	
	
	//----------------------------------------------//
	// WRITERS
	//----------------------------------------------//
	
	public synchronized void write(Entity entity) throws TeleporterException
	{
		if (entity instanceof Player)
		{
			Mixin.teleport((Player)entity, this);
		}
		else
		{
			TeleportMixinDefault.teleportEntity(entity, this);
		}
	}
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	public PS()
	{
		
	}
	
	public PS(PS ps)
	{
		this.setPS(ps);
	}
	
	public PS(Location location)
	{
		this.setLocationTransparent(location);
	}
	
	public PS(Vector velocity)
	{
		this.setVelocityTransparent(velocity);
	}
	
	public PS(Entity entity)
	{
		this.setEntityTransparent(entity);
	}
	
	public PS(Block block)
	{
		this.setBlockTransparent(block);
	}
	
	public PS(Chunk chunk)
	{
		this.setChunkTransparent(chunk);
	}
	
	public PS(String oldString)
	{
		this.setOldStringTransparent(oldString);
	}
	
	//----------------------------------------------//
	// TO STRING
	//----------------------------------------------//
	
	@Override
	public synchronized String toString()
	{
		return this.getClass().getSimpleName()+MCore.gson.toJson(this);
	}
	
	protected final transient static DecimalFormat twoDForm = new DecimalFormat("#.##");
	public List<String> getDesc()
	{
		// ret.add("<h>World <a>"+this.worldName);
		return this.getDesc("<k>%s <v>%s");
	}
	public List<String> getDesc(String format)
	{
		List<String> ret = new ArrayList<String>();
		
		if (this.worldName != null) ret.add(Txt.parse(format, "World", this.worldName));

		if (this.blockX != null) ret.add(Txt.parse(format, "Block X", this.blockX));
		if (this.blockY != null) ret.add(Txt.parse(format, "Block Y", this.blockY));
		if (this.blockZ != null) ret.add(Txt.parse(format, "Block Z", this.blockZ));

		if (this.locationX != null) ret.add(Txt.parse(format, "Location X", twoDForm.format(this.locationX)));
		if (this.locationY != null) ret.add(Txt.parse(format, "Location Y", twoDForm.format(this.locationY)));
		if (this.locationZ != null) ret.add(Txt.parse(format, "Location Z", twoDForm.format(this.locationZ)));

		if (this.chunkX != null) ret.add(Txt.parse(format, "Chunk X", this.chunkX));
		if (this.chunkZ != null) ret.add(Txt.parse(format, "Chunk Z", this.chunkZ));

		if (this.pitch != null) ret.add(Txt.parse(format, "Pitch", twoDForm.format(this.pitch)));
		if (this.yaw != null) ret.add(Txt.parse(format, "Yaw", twoDForm.format(this.yaw)));

		if (this.velocityX != null) ret.add(Txt.parse(format, "Velocity X", twoDForm.format(this.velocityX)));
		if (this.velocityY != null) ret.add(Txt.parse(format, "Velocity Y", twoDForm.format(this.velocityY)));
		if (this.velocityZ != null) ret.add(Txt.parse(format, "Velocity Z", twoDForm.format(this.velocityZ)));
		
		return ret;
	}
	
	public String getShortDesc()
	{
		return this.getShortDesc("<k>%s <v>%s ");
	}
	public String getShortDesc(String format)
	{
		List<String> ret = new ArrayList<String>();
		
		if (this.worldName != null) ret.add(Txt.parse(format, "w", this.worldName));

		if (this.blockX != null) ret.add(Txt.parse(format, "bx", this.blockX));
		if (this.blockY != null) ret.add(Txt.parse(format, "by", this.blockY));
		if (this.blockZ != null) ret.add(Txt.parse(format, "bz", this.blockZ));

		if (this.locationX != null) ret.add(Txt.parse(format, "lx", twoDForm.format(this.locationX)));
		if (this.locationY != null) ret.add(Txt.parse(format, "ly", twoDForm.format(this.locationY)));
		if (this.locationZ != null) ret.add(Txt.parse(format, "lz", twoDForm.format(this.locationZ)));

		if (this.chunkX != null) ret.add(Txt.parse(format, "cx", this.chunkX));
		if (this.chunkZ != null) ret.add(Txt.parse(format, "cz", this.chunkZ));

		if (this.pitch != null) ret.add(Txt.parse(format, "p", twoDForm.format(this.pitch)));
		if (this.yaw != null) ret.add(Txt.parse(format, "y", twoDForm.format(this.yaw)));

		if (this.velocityX != null) ret.add(Txt.parse(format, "vx", twoDForm.format(this.velocityX)));
		if (this.velocityY != null) ret.add(Txt.parse(format, "vy", twoDForm.format(this.velocityY)));
		if (this.velocityZ != null) ret.add(Txt.parse(format, "vz", twoDForm.format(this.velocityZ)));
		
		return Txt.implode(ret, "").trim();
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
	// EQUALS AND HASH CODE
	//----------------------------------------------//
	// Generated by eclipse
	
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
		result = prime * result
				+ ((locationX == null) ? 0 : locationX.hashCode());
		result = prime * result
				+ ((locationY == null) ? 0 : locationY.hashCode());
		result = prime * result
				+ ((locationZ == null) ? 0 : locationZ.hashCode());
		result = prime * result + ((pitch == null) ? 0 : pitch.hashCode());
		result = prime * result
				+ ((velocityX == null) ? 0 : velocityX.hashCode());
		result = prime * result
				+ ((velocityY == null) ? 0 : velocityY.hashCode());
		result = prime * result
				+ ((velocityZ == null) ? 0 : velocityZ.hashCode());
		result = prime * result
				+ ((worldName == null) ? 0 : worldName.hashCode());
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
		if (pitch == null)
		{
			if (other.pitch != null) return false;
		}
		else if (!pitch.equals(other.pitch)) return false;
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
	
	//----------------------------------------------//
	// STATIC COMPARISON TOOLS
	//----------------------------------------------//
	
	public static Double locationDistanceSquared(PS one, PS two)
	{
		if (one == null) return null;
		if (two == null) return null;
		
		String w1 = one.getWorldName();
		String w2 = two.getWorldName();
		
		if (!MUtil.equals(w1, w2)) return null;
		
		Double x1 = one.calcLocationX();
		if (x1 == null) return null;
		
		Double y1 = one.calcLocationY();
		if (y1 == null) return null;
		
		Double z1 = one.calcLocationZ();
		if (z1 == null) return null;
		
		Double x2 = two.calcLocationX();
		if (x2 == null) return null;
		
		Double y2 = two.calcLocationY();
		if (y2 == null) return null;
		
		Double z2 = two.calcLocationZ();
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
		
		String w1 = one.getWorldName();
		String w2 = two.getWorldName();
		
		if (w1 == null) return false;
		if (w2 == null) return false;
		
		return w1.equalsIgnoreCase(w2);
	}
	
	public static boolean inSameUniverse(PS one, PS two, Multiverse multiverse)
	{
		if (one == null) return false;
		if (two == null) return false;
		
		String w1 = one.getWorldName();
		String w2 = two.getWorldName();
		
		if (w1 == null) return false;
		if (w2 == null) return false;
		
		String m1 = multiverse.getUniverseForWorldName(w1);
		String m2 = multiverse.getUniverseForWorldName(w2);
		
		return m1.equalsIgnoreCase(m2);
	}

	public static boolean inSameUniverse(PS one, PS two, Aspect aspect)
	{
		return inSameUniverse(one, two, aspect.multiverse());
	}
	

}
