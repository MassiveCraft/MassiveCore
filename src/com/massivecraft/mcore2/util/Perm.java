package com.massivecraft.mcore2.util;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.massivecraft.mcore2.Lang;

public class Perm
{
	public static String getPermissionDescription (String perm)
	{
		if (perm == null) return Lang.permDoThat;
		Permission permission = Bukkit.getPluginManager().getPermission(perm);
		return getPermissionDescription(permission);
	}
	
	public static String getPermissionDescription (Permission perm)
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
	
	public static boolean has (CommandSender me, String perm)
	{
		if (me == null) return false;
		return me.hasPermission(perm);
	}
	
	public static boolean has (CommandSender me, String perm, boolean verbose)
	{
		if (has(me, perm))
		{
			return true;
		}
		else if (verbose && me != null)
		{
			me.sendMessage(getForbiddenMessage(perm));
		}
		return false;
	}
	
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
}
