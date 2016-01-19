package com.massivecraft.massivecore.nms;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.inventory.meta.SkullMeta;

import com.massivecraft.massivecore.Couple;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.IdData;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class NmsHead extends NmsAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsHead i = new NmsHead();
	public static NmsHead get () { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static Class<?> classCraftMetaSkull;
	public static Field fieldCraftMetaSkullDotProfile;
	
	public static Class<?> classGameProfile;
	public static Field fieldGameProfileDotId;
	public static Field fieldGameProfileDotName;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getRequiredVersion()
	{
		return 8;
	}
	
	@Override
	protected void setup() throws Throwable
	{
		classCraftMetaSkull = PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaSkull");
		fieldCraftMetaSkullDotProfile = ReflectionUtil.getField(classCraftMetaSkull, "profile");
		
		classGameProfile = Class.forName("com.mojang.authlib.GameProfile");
		fieldGameProfileDotId = ReflectionUtil.getField(classGameProfile, "id");
		fieldGameProfileDotName = ReflectionUtil.getField(classGameProfile, "name");
	}

	// -------------------------------------------- //
	// GAMEPROFILE: SIMPLE
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
	// GAMEPROFILE: GET
	// -------------------------------------------- //
	
	public static String getGameProfileName(Object gameProfile)
	{
		return ReflectionUtil.getField(fieldGameProfileDotName, gameProfile);
	}
	
	public static UUID getGameProfileId(Object gameProfile)
	{
		return ReflectionUtil.getField(fieldGameProfileDotId, gameProfile);
	}
	
	// -------------------------------------------- //
	// GAMEPROFILE: SET
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
	// SKULLMETA: RAW
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
	
	// -------------------------------------------- //
	// RESOLVE
	// -------------------------------------------- //
	// We resolve the locally best possible information using IdUtil.
	
	public static Couple<String, UUID> resolve(String name, UUID id)
	{
		// Create Ret
		// We default to the input.
		String retName = name;
		UUID retId = id;
		
		// Fetch IdData
		// First by name then id. 
		IdData data = null;
		if (name != null) data = IdUtil.getNameToData().get(name);
		if (data == null && id != null) data = IdUtil.getIdToData().get(id.toString());
		
		// Use that data if found
		if (data != null)
		{
			retName = data.getName();
			retId = MUtil.asUuid(data.getId());
		}
		
		// Return Ret
		return new Couple<String, UUID>(retName, retId);
	}
	
	public static Couple<String, UUID> resolve(SkullMeta meta)
	{
		return resolve(getName(meta), getId(meta));
	}
	
}
