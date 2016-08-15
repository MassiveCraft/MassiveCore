package com.massivecraft.massivecore.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class NmsEntity extends NmsAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsEntity i = new NmsEntity();
	public static NmsEntity get () { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// org.bukkit.craftbukkit.CraftWorld#world
	public static Class<?> classCraftWorld;
	public static Field fieldCraftWorldDotWorld;
	
	// net.minecraft.server.WorldServer#entitiesByUUID
	public static Class<?> classWorldServer;
	public static Field fieldWorldServerDotEntitiesByUUID;
	
	// net.minecraft.server.Entity#getBukkitEntity()
	public static Class<?> classEntity;
	public static Method methodEntityDotGetBukkitEntity;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	// NOTE: This has been properly researched.
	// NOTE: The field "WorldServer.entitiesByUUID" was added in 1.8.
	@Override
	public int getRequiredVersion()
	{
		return 8;
	}
	
	@Override
	protected void setup() throws Throwable
	{
		classCraftWorld = PackageType.CRAFTBUKKIT.getClass("CraftWorld");
		fieldCraftWorldDotWorld = ReflectionUtil.getField(classCraftWorld, "world");
		
		classWorldServer = PackageType.MINECRAFT_SERVER.getClass("WorldServer");
		fieldWorldServerDotEntitiesByUUID = ReflectionUtil.getField(classWorldServer, "entitiesByUUID");
		
		classEntity = PackageType.MINECRAFT_SERVER.getClass("Entity");
		methodEntityDotGetBukkitEntity = ReflectionUtil.getMethod(classEntity, "getBukkitEntity");
	}
	
	// -------------------------------------------- //
	// REFLECTION METHODS > WORLD MAP
	// -------------------------------------------- //
	
	public Object getNmsWorld(World world)
	{
		if (world == null) throw new NullPointerException("world");
		
		return ReflectionUtil.getField(fieldCraftWorldDotWorld, world);
	}
	
	public Map<UUID, Object> getNmsWorldMap(Object nmsWorld)
	{
		if (nmsWorld == null) throw new NullPointerException("nmsWorld");
		
		return ReflectionUtil.getField(fieldWorldServerDotEntitiesByUUID, nmsWorld);
	}
	
	// -------------------------------------------- //
	// REFLECTION METHODS > ENTITY
	// -------------------------------------------- //
	
	public Entity getBukkitEntity(Object nmsEntity)
	{
		if (nmsEntity == null) throw new NullPointerException("nmsEntity");
		
		return ReflectionUtil.invokeMethod(methodEntityDotGetBukkitEntity, nmsEntity);
	}

	// -------------------------------------------- //
	// WORLD MAP
	// -------------------------------------------- //
	
	protected Map<World, Map<UUID, Object>> worldMaps = new WeakHashMap<World, Map<UUID, Object>>();
	
	public Map<UUID, Object> getWorldMap(World world)
	{
		if (world == null) throw new NullPointerException("world");
		
		Map<UUID, Object> ret = this.worldMaps.get(world);
		if (ret == null)
		{
			Object nmsWorld = this.getNmsWorld(world);
			ret = this.getNmsWorldMap(nmsWorld);
			this.worldMaps.put(world, ret);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// GET ENTITY
	// -------------------------------------------- //
	
	public Entity getEntity(World world, UUID uuid)
	{
		if (world == null) throw new NullPointerException("world");
		if (uuid == null) return null;
		
		Map<UUID, Object> worldMap = this.getWorldMap(world);
		
		Object nmsEntity = worldMap.get(uuid);
		if (nmsEntity == null) return null;
		
		return this.getBukkitEntity(nmsEntity);
	}
	
	public Entity getEntity(UUID uuid)
	{
		if (uuid == null) return null;
		
		for (World world : Bukkit.getWorlds())
		{
			Entity ret = this.getEntity(world, uuid);
			if (ret != null) return ret; 
		}
		
		return null;
	}
	
}
