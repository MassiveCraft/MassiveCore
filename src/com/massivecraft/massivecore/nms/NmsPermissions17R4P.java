package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachment;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class NmsPermissions17R4P extends NmsPermissions
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsPermissions17R4P i = new NmsPermissions17R4P();
	public static NmsPermissions17R4P get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Class<?> classCraftHumanEntity;
	protected Field fieldCraftHumanEntityBase;
	
	protected Field fieldPermissibleBaseAttachments;
	
	protected Field fieldAttachmentPermissions;
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		this.classCraftHumanEntity = PackageType.CRAFTBUKKIT_ENTITY.getClass("CraftHumanEntity");
		this.fieldCraftHumanEntityBase = ReflectionUtil.getField(this.classCraftHumanEntity, "perm");
		
		this.fieldPermissibleBaseAttachments = ReflectionUtil.getField(PermissibleBase.class, "attachments");
		
		this.fieldAttachmentPermissions = ReflectionUtil.getField(PermissionAttachment.class, "permissions");
	}
	
	// -------------------------------------------- //
	// BASE
	// -------------------------------------------- //
	
	@Override
	public List<PermissionAttachment> getAttachments(PermissibleBase base)
	{
		return ReflectionUtil.getField(this.fieldPermissibleBaseAttachments, base);
	}
	
	// -------------------------------------------- //
	// PLAYER
	// -------------------------------------------- //
	
	@Override
	public PermissibleBase getBase(Permissible permissible)
	{
		if (permissible instanceof Player)
		{
			return ReflectionUtil.getField(this.fieldCraftHumanEntityBase, permissible);
		}
		return null;
	}
	
	// -------------------------------------------- //
	// ATTACHMENT
	// -------------------------------------------- //
	
	@Override
	public Map<String, Boolean> getAttachmentPermissions(PermissionAttachment permissionAttachment)
	{
		return ReflectionUtil.getField(this.fieldAttachmentPermissions, permissionAttachment);
	}
	
	@Override
	public void setAttachmentPermissions(PermissionAttachment permissionAttachment, Map<String, Boolean> permissions)
	{
		ReflectionUtil.setField(this.fieldAttachmentPermissions, permissionAttachment, permissions);
	}
	
}
