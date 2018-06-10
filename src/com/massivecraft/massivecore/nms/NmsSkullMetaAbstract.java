package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.UUID;

public abstract class NmsSkullMetaAbstract extends NmsSkullMeta
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// org.bukkit.craftbukkit.inventory.CraftMetaSkull 
	public Class<?> classCraftMetaSkull;
	// org.bukkit.craftbukkit.inventory.CraftMetaSkull#profile
	public Field fieldCraftMetaSkullProfile;
	
	// 17R4: net.minecraft.util.com.mojang.authlib.GameProfile
	// 18R1P: com.mojang.authlib.GameProfile
	public Class<?> classGameProfile;
	// ...#id
	public Field fieldGameProfileId;
	// ...#name
	public Field fieldGameProfileName;
	// ...(UUID id, String name);
	public Constructor<?> constructorGameProfile;
	
	// -------------------------------------------- //
	// GAME PROFILE CLASS NAME
	// -------------------------------------------- //
	
	public abstract String getGameProfileClassName();
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		this.classCraftMetaSkull = PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaSkull");
		this.fieldCraftMetaSkullProfile = ReflectionUtil.getField(this.classCraftMetaSkull, "profile");
		
		this.classGameProfile = Class.forName(this.getGameProfileClassName());
		this.fieldGameProfileId = ReflectionUtil.getField(this.classGameProfile, "id");
		this.fieldGameProfileName = ReflectionUtil.getField(this.classGameProfile, "name");
		this.constructorGameProfile = ReflectionUtil.getConstructor(this.classGameProfile, UUID.class, String.class);
	}
	
	// -------------------------------------------- //
	// RAW
	// -------------------------------------------- //
	
	@Override
	public UUID getId(SkullMeta meta)
	{
		Object gameProfile = getGameProfile(meta);
		if (gameProfile == null) return null;
		return getGameProfileId(gameProfile);
	}
	
	@Override
	public void set(SkullMeta meta, String name, UUID id)
	{
		final Object gameProfile = createGameProfile(id, name);
		setGameProfile(meta, gameProfile);
	}
	
	// -------------------------------------------- //
	// GAMEPROFILE
	// -------------------------------------------- //
	
	protected <T> T createGameProfile(UUID id, String name) {
		return ReflectionUtil.invokeConstructor(this.constructorGameProfile, id, name);
	}
	
	protected <T> T getGameProfile(SkullMeta meta)
	{
		return ReflectionUtil.getField(this.fieldCraftMetaSkullProfile, meta);
	}
	
	protected void setGameProfile(SkullMeta meta, Object gameProfile)
	{
		ReflectionUtil.setField(this.fieldCraftMetaSkullProfile, meta, gameProfile);
	}
	
	// -------------------------------------------- //
	// GAMEPROFILE > GET
	// -------------------------------------------- //
	
	protected String getGameProfileName(Object gameProfile)
	{
		return ReflectionUtil.getField(this.fieldGameProfileName, gameProfile);
	}
	
	protected UUID getGameProfileId(Object gameProfile)
	{
		return ReflectionUtil.getField(this.fieldGameProfileId, gameProfile);
	}
	
	// -------------------------------------------- //
	// GAMEPROFILE > SET
	// -------------------------------------------- //
	
	protected void setGameProfileName(Object gameProfile, String name)
	{
		ReflectionUtil.setField(this.fieldGameProfileName, gameProfile, name);
	}
	
	protected void setGameProfileId(Object gameProfile, UUID id)
	{
		ReflectionUtil.setField(this.fieldGameProfileId, gameProfile, id);
	}
	
}
