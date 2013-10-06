package com.massivecraft.mcore.cmd.arg;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

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
	public ArgResult<Permission> read(String arg, CommandSender sender)
	{
		ArgResult<Permission> ret = new ArgResult<Permission>();
		
		for (Permission permission : Bukkit.getPluginManager().getPermissions())
		{
			if (!permission.getName().equals(arg)) continue;
			ret.setResult(permission);
			break;
		}
		
		if (!ret.hasResult())
		{
			ret.setErrors("<b>No permission with the name \"<h>"+arg+"<b>\" was found.");
		}
		
		return ret;
	}
	
}
