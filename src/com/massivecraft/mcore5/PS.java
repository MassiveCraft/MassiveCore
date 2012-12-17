package com.massivecraft.mcore5;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.massivecraft.mcore5.util.Txt;
import com.massivecraft.mcore5.xlib.gson.annotations.SerializedName;

/**
 * PS stands for PhysicalState.
 * This class stores data related to just that.
 * When coding plugins you may find yourself wanting to store a player location.
 * Another time you may want to store the player location but without the worldName info.
 * Another time you may want to store pitch and yaw only.
 * This class is supposed to be usable in all those cases.
 * Hopefully this class will save you from implementing special classes for all those combinations.
 */

@EqualsAndHashCode
public class PS implements Cloneable
{
	// -------------------------------------------- //
	// TELEPORTER
	// -------------------------------------------- //
	
	public static transient PSTeleporter teleporter = PSTeleporterDefault.get();
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Field: worldName
	@SerializedName("w")
	@Getter @Setter protected String worldName = null;
	
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
	@Getter @Setter protected Integer blockX = null;
	public Integer calcBlockX()
	{
		return calcBlock(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: blockY
	@SerializedName("by")
	@Getter @Setter protected Integer blockY = null;
	public Integer calcBlockY()
	{
		return calcBlock(this.locationY, this.blockY, null);
	}
	
	// Field: blockZ
	@SerializedName("bz")
	@Getter @Setter protected Integer blockZ = null;
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
	@Getter @Setter protected Double locationX = null;
	public Double calcLocationX()
	{
		return calcLocation(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: locationY
	@SerializedName("ly")
	@Getter @Setter protected Double locationY = null;
	public Double calcLocationY()
	{
		return calcLocation(this.locationY, this.blockY, null);
	}
	
	// Field: locationZ
	@SerializedName("lz")
	@Getter @Setter protected Double locationZ = null;
	public Double calclocationZ()
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
	@Getter @Setter protected Integer chunkX = null;
	public Integer calcChunkX()
	{
		return calcChunk(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: chunkZ
	@SerializedName("xz")
	@Getter @Setter protected Integer chunkZ = null;
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
	@Getter @Setter protected Float pitch = null;
	/*public void setPitch(Float val) 
	{
		if (val == null)
		{
			this.pitch = null;
		}
		else
		{
			this.pitch = (val + 360F) % 360F;
		}
	}*/
	
	// Field: yaw
	@SerializedName("y")
	@Getter @Setter protected Float yaw = null;
	
	// ---------------------
	
	// Field: velocityX
	@SerializedName("vx")
	@Getter @Setter protected Double velocityX = null;
	public Double calcVelocityX()
	{
		return calcVelocity(this.locationX, this.blockX, this.chunkX, this.velocityX);
	}
	
	// Field: velocityY
	@SerializedName("vy")
	@Getter @Setter protected Double velocityY = null;
	public Double calcVelocityY()
	{
		return calcVelocity(this.locationY, this.blockY, 0, this.velocityY);
	}
	
	// Field: velocityZ
	@SerializedName("vz")
	@Getter @Setter protected Double velocityZ = null;
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
		return this.innerLocation(this.calcLocationX(), this.calcLocationY(), this.calclocationZ());
	}
	protected synchronized Location innerLocation(Double x, Double y, Double z)
	{
		World world = this.getWorld();
		
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
	
	public synchronized void write(Entity entity)
	{
		teleporter.teleport(entity, this);
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

}
