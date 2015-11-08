package com.massivecraft.massivecore.command.type;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

public class TypePermission extends TypeAbstractChoice<Permission>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePermission i = new TypePermission();
	public static TypePermission get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getIdInner(Permission value)
	{
		return value.getName();
	}

	@Override
	public Collection<Permission> getAll()
	{
		return Bukkit.getPluginManager().getPermissions();
	}
	
	@Override
	public Permission getExactMatch(String arg)
	{
		return Bukkit.getPluginManager().getPermission(arg);
	}

}
