package com.massivecraft.massivecore.util;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.inventory.meta.SkullMeta;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;

public class HeadUtil
{
	// -------------------------------------------- //
	// REFLECTION CACHE
	// -------------------------------------------- //
	
	public static Class<?> classCraftMetaSkull;
	public static Field fieldCraftMetaSkullDotProfile;
	
	public static Class<?> classGameProfile;
	public static Field fieldGameProfileDotId;
	public static Field fieldGameProfileDotName;
	
	static
	{
		try
		{
			classCraftMetaSkull = PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaSkull");
			fieldCraftMetaSkullDotProfile = ReflectionUtil.getField(classCraftMetaSkull, "profile");
			
			classGameProfile = Class.forName("com.mojang.authlib.GameProfile");
			fieldGameProfileDotId = ReflectionUtil.getField(classGameProfile, "id");
			fieldGameProfileDotName = ReflectionUtil.getField(classGameProfile, "name");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------- //
	// GAME PROFILE SIMPLE
	// -------------------------------------------- //
	
	public static Object getGameProfile(SkullMeta meta)
	{
		return ReflectionUtil.getField(fieldCraftMetaSkullDotProfile, meta);
	}
	
	public static void setGameProfile(SkullMeta meta, Object gameProfile)
	{
		ReflectionUtil.setField(fieldCraftMetaSkullDotProfile, meta, gameProfile);
	}
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public static String getGameProfileName(Object gameProfile)
	{
		return (String) ReflectionUtil.getField(fieldGameProfileDotName, gameProfile);
	}
	
	public static UUID getGameProfileId(Object gameProfile)
	{
		return (UUID) ReflectionUtil.getField(fieldGameProfileDotId, gameProfile);
	}
	
	// -------------------------------------------- //
	// SET
	// -------------------------------------------- //
	
	public static void setGameProfileName(Object gameProfile, String name)
	{
		ReflectionUtil.setField(fieldGameProfileDotName, gameProfile, name);
	}
	
	public static void setGameProfileId(Object gameProfile, UUID id)
	{
		ReflectionUtil.setField(fieldGameProfileDotId, gameProfile, id);
	}
	
	// -------------------------------------------- //
	// ASDF
	// -------------------------------------------- //
	
	public static String getName(SkullMeta meta)
	{
		// Object gameProfile = getGameProfile(meta);
		// if (gameProfile == null) return null;
		// return getGameProfileName(gameProfile);
		
		return meta.getOwner();
	}
	
	public static UUID getId(SkullMeta meta)
	{
		Object gameProfile = getGameProfile(meta);
		if (gameProfile == null) return null;
		return getGameProfileId(gameProfile);
	}
	
	public static void set(SkullMeta meta, String name, UUID id)
	{
		meta.setOwner(name != null ? name : "adsf");
		
		Object gameProfile = getGameProfile(meta);
		setGameProfileName(gameProfile, name);
		setGameProfileId(gameProfile, id);		
	}
	
}
