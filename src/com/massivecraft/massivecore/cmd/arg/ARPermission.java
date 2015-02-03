package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.massivecraft.massivecore.MassiveException;

public class ARPermission extends ARAbstract<Permission>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARPermission i = new ARPermission();
	public static ARPermission get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Permission read(String arg, CommandSender sender) throws MassiveException
	{
		for (Permission permission : Bukkit.getPluginManager().getPermissions())
		{
			if ( ! permission.getName().equals(arg)) continue;
			return permission;
		}
		
		throw new MassiveException().addMsg("<b>No permission with the name \"<h>%s<b>\" was found.", arg);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (Permission perm : Bukkit.getPluginManager().getPermissions())
		{
			ret.add(perm.getName());
		}
		
		return ret;
	}

}
