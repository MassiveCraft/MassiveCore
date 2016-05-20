package com.massivecraft.massivecore.nms;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachment;
import com.massivecraft.massivecore.mixin.Mixin;

public class NmsPermissions extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsPermissions d = new NmsPermissions().setAlternatives(
		NmsPermissions17R4P.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsPermissions i = d;
	public static NmsPermissions get() { return i; }
	
	// -------------------------------------------- //
	// BASE
	// -------------------------------------------- //
	
	public List<PermissionAttachment> getAttachments(PermissibleBase base)
	{
		throw this.notImplemented();
	}
	
	// -------------------------------------------- //
	// PLAYER
	// -------------------------------------------- //
	
	public PermissibleBase getBase(Player player)
	{
		throw this.notImplemented();
	}
	
	public List<PermissionAttachment> getAttachments(Player player)
	{
		PermissibleBase base = this.getBase(player);
		return this.getAttachments(base);
	}
	
	// -------------------------------------------- //
	// ATTACHMENT
	// -------------------------------------------- //
	
	public Map<String, Boolean> getAttachmentPermissionsRaw(PermissionAttachment permissionAttachment)
	{
		throw this.notImplemented();
	}
	
	public void setAttachmentPermissionsRaw(PermissionAttachment permissionAttachment, Map<String, Boolean> permissions)
	{
		throw this.notImplemented();
	}
	
	public boolean updateAttachmentPermissions(PermissionAttachment attachment, Map<String, Boolean> permissions)
	{
		if (attachment == null) throw new NullPointerException("attachment");
		if (permissions == null) throw new NullPointerException("permissions");
		
		Map<String, Boolean> inner = this.getAttachmentPermissionsRaw(attachment);
		if (inner.equals(permissions)) return false;
		
		inner.clear();
		inner.putAll(permissions);
		
		Permissible permissible = attachment.getPermissible();
		if (permissible != null) permissible.recalculatePermissions();
		
		return true;
	}
	

	
}
