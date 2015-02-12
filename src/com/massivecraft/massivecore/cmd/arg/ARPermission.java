package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.massivecraft.massivecore.MassiveException;

public class ARPermission extends ArgReaderAbstract<Permission>
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
		Permission ret = null;
		
		for (Permission permission : Bukkit.getPluginManager().getPermissions())
		{
			if ( ! permission.getName().equals(arg)) continue;
			ret = permission;
			break;
		}
		
		if (ret == null)
		{
			throw new MassiveException().addMsg("<b>No permission with the name \"<h>%s<b>\" was found.", arg);
		}
		
		return ret;
	}
	
}
