package com.massivecraft.mcore5.util;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.massivecraft.mcore5.Lang;
import com.massivecraft.mcore5.MCore;

public class Perm
{
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public static boolean has(CommandSender sender, Permission permission)
	{
		return has(sender, permission.getName());
	}
	public static boolean has(CommandSender sender, String perm)
	{
		if (sender == null) return false;
		return sender.hasPermission(perm);
	}
	
	public static boolean has(CommandSender sender, Permission permission, boolean verbose)
	{
		return has(sender, permission.getName(), verbose);
	}
	public static boolean has(CommandSender sender, String perm, boolean verbose)
	{
		if (has(sender, perm))
		{
			return true;
		}
		else if (verbose && sender != null)
		{
			sender.sendMessage(getForbiddenMessage(perm));
		}
		return false;
	}
	
	// -------------------------------------------- //
	// DESCRIPTIONS AND MESSAGES
	// -------------------------------------------- //
	
	public static String getPermissionDescription(String perm)
	{
		if (perm == null) return Lang.permDoThat;
		Permission permission = Bukkit.getPluginManager().getPermission(perm);
		return getPermissionDescription(permission);
	}
	
	public static String getPermissionDescription(Permission perm)
	{
		if (perm == null) return Lang.permDoThat;
		String desc = perm.getDescription();
		if (desc == null || desc.length() == 0) return Lang.permDoThat;
		return desc;
	}
	
	public static String getForbiddenMessage(String perm)
	{
		return Txt.parse(Lang.permForbidden, getPermissionDescription(perm));
	}
	
	// -------------------------------------------- //
	// RANDOM UTILS
	// -------------------------------------------- //
	
	public static <T> T pickFirstVal(CommandSender me, Map<String, T> perm2val)
	{
		if (perm2val == null) return null;
		T ret = null;
		
		for ( Entry<String, T> entry : perm2val.entrySet())
		{
			ret = entry.getValue();
			if (has(me, entry.getKey())) break;
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// ENSURE HAS
	// -------------------------------------------- //
	
	public static void ensureHas(Player player, String permissionName)
	{
		if (player.hasPermission(permissionName))
		{
			return;
		}
		else
		{
			player.addAttachment(MCore.p, permissionName, true);
		}
	}
	
	public static void ensureHas(Player player, Permission permission)
	{
		ensureHas(player, permission.getName());
	}
	
	// -------------------------------------------- //
	// GET CREATIVE
	// -------------------------------------------- //
	
	public static Permission getCreative(String name)
	{
		return getCreative(name, null, null, null);
	}

	public static Permission getCreative(String name, String description)
	{
		return getCreative(name, description, null, null);
	}

	public static Permission getCreative(String name, PermissionDefault defaultValue)
	{
		return getCreative(name, null, defaultValue, null);
	}

	public static Permission getCreative(String name, String description, PermissionDefault defaultValue)
	{
		return getCreative(name, description, defaultValue, null);
	}

	public static Permission getCreative(String name, Map<String, Boolean> children)
	{
		return getCreative(name, null, null, children);
	}

	public static Permission getCreative(String name, String description, Map<String, Boolean> children)
	{
		return getCreative(name, description, null, children);
	}

	public static Permission getCreative(String name, PermissionDefault defaultValue, Map<String, Boolean> children)
	{
		return getCreative(name, null, defaultValue, children);
	}

	public static Permission getCreative(String name, String description, PermissionDefault defaultValue, Map<String, Boolean> children)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			ret = new Permission(name, description, defaultValue, children);
			Bukkit.getPluginManager().addPermission(ret);
		}
		return ret;
	}
}
