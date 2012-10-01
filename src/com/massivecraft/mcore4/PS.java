package com.massivecraft.mcore4;

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
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.mcore4.store.accessor.Accessor;
import com.massivecraft.mcore4.util.Txt;
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
	@Getter @Setter protected String worldName;
	
	// FakeField: world
	public World getWorld()
	{
		if (this.worldName == null) return null;
		return Bukkit.getWorld(this.worldName);
	}
	public PS setWorld(World val)
	{
		this.worldName = val.getName();
		return this;
	}
	
	// ---------------------
	
	// Field: blockX
	@SerializedName("bx")
	@Getter @Setter protected Integer blockX;
	public Integer calcBlockX()
	{
		return calcBlock(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: blockY
	@SerializedName("by")
	@Getter @Setter protected Integer blockY;
	public Integer calcBlockY()
	{
		return calcBlock(this.locationY, this.blockY, null);
	}
	
	// Field: blockZ
	@SerializedName("bz")
	@Getter @Setter protected Integer blockZ;
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
	@Getter @Setter protected Double locationX;
	public Double calcLocationX()
	{
		return calcLocation(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: locationY
	@SerializedName("ly")
	@Getter @Setter protected Double locationY;
	public Double calcLocationY()
	{
		return calcLocation(this.locationY, this.blockY, null);
	}
	
	// Field: locationZ
	@SerializedName("lz")
	@Getter @Setter protected Double locationZ;
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
	@Getter @Setter protected Integer chunkX;
	public Integer calcChunkX()
	{
		return calcChunk(this.locationX, this.blockX, this.chunkX);
	}
	
	// Field: chunkZ
	@SerializedName("xz")
	@Getter @Setter protected Integer chunkZ;
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
	@Getter protected Float pitch;
	public PS setPitch(Float val) 
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
	
	// Field: yaw
	@SerializedName("y")
	@Getter @Setter protected Float yaw;
	
	// ---------------------
	
	// Field: velocityX
	@SerializedName("vx")
	@Getter @Setter protected Double velocityX;
	public Double calcVelocityX()
	{
		return calcVelocity(this.locationX, this.blockX, this.chunkX, this.velocityX);
	}
	
	// Field: velocityY
	@SerializedName("vy")
	@Getter @Setter protected Double velocityY;
	public Double calcVelocityY()
	{
		return calcVelocity(this.locationY, this.blockY, 0, this.velocityY);
	}
	
	// Field: velocityZ
	@SerializedName("vz")
	@Getter @Setter protected Double velocityZ;
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
	// CONVERTERS
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
		
		return new Location(world, x, y, z, pitch, yaw);
	}
	
	public synchronized Block getBlock()
	{
		return this.innerBlock(this.getBlockX(), this.getBlockY(), this.getBlockZ());
	}
	public synchronized Block calcBlock()
	{
		return this.innerBlock(this.calcBlockX(), this.calcBlockY(), this.calcBlockZ());
	}
	public synchronized Block innerBlock(Integer x, Integer y, Integer z)
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
	public synchronized Chunk innerChunk(Integer x, Integer z)
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
	public synchronized Vector innerVelocity(Double x, Double y, Double z)
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
		this.setPitch(location.getPitch());
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
	
	// ---------------------
	// TODO: This should be removed later on when my converting phase is complete.
	public synchronized PS read(String str)
	{
		return this.readDefault().readTransparent(str);
	}
	
	public synchronized PS readTransparent(String str)
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
		
		return this;
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
