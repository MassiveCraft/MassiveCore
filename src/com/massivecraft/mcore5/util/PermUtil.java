package com.massivecraft.mcore5.util;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.massivecraft.mcore5.Lang;
import com.massivecraft.mcore5.MCore;

public class PermUtil
{
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public static boolean has(Permissible permissable, Permission permission)
	{
		return has(permissable, permission.getName());
	}
	public static boolean has(Permissible permissable, String perm)
	{
		if (permissable == null) return false;
		return permissable.hasPermission(perm);
	}
	
	public static boolean has(Permissible permissable, Permission permission, boolean verbose)
	{
		return has(permissable, permission.getName(), verbose);
	}
	public static boolean has(Permissible permissable, String perm, boolean verbose)
	{
		if (has(permissable, perm))
		{
			return true;
		}
		else if (verbose && permissable != null)
		{
			if (permissable instanceof CommandSender)
			{
				CommandSender sender = (CommandSender)permissable;
				sender.sendMessage(getForbiddenMessage(perm));
			}
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
	
	public static void ensureHas(Permissible permissible, String permissionName)
	{
		if (permissible.hasPermission(permissionName))
		{
			return;
		}
		else
		{
			permissible.addAttachment(MCore.p, permissionName, true);
		}
	}
	
	public static void ensureHas(Permissible permissible, Permission permission)
	{
		ensureHas(permissible, permission.getName());
	}
	
	// -------------------------------------------- //
	// EFFICIENT UPDATERS
	// -------------------------------------------- //
	// These setters offer bulk-ways of updating already created permissions.
	// For the best performance you should enter all information into the Permission constructor when creating the Permission.
	// At times you will however need to update permissions after they were created.
	// In these cases the order and approach with witch you alter the fields matter a lot performance wise.
	// These setter will ensure you get optimal performance.
	
	// ONE FIELD
	
	public static void set(Permission permission, String description)
	{
		// Recalculation need created: FALSE
		// Recalculation auto-performed: FALSE
		permission.setDescription(description);
	}
	
	public static void set(Permission permission, PermissionDefault defaultValue)
	{
		if (defaultValue == null) return;
		if (permission.getDefault() == defaultValue) return;
		
		// Recalculation need created: TRUE
		// Recalculation auto-performed: TRUE
		permission.setDefault(defaultValue);
	}
	
	public static void set(Permission permission, Map<String, Boolean> children)
	{
		if (children == null) return;
		if (permission.getChildren().equals(children)) return;
		
		// Recalculation need created: TRUE
		// Recalculation auto-performed: FALSE
		permission.getChildren().clear();
		permission.getChildren().putAll(children);
		
		// Manual Recalculation
		permission.recalculatePermissibles();
	}
	
	// TWO FIELDS
	
	public static void set(Permission permission, String description, PermissionDefault defaultValue)
	{
		set(permission, defaultValue);
		set(permission, description);
	}
	
	public static void set(Permission permission, String description, Map<String, Boolean> children)
	{
		set(permission, children);
		set(permission, description);
	}
	
	public static void set(Permission permission, PermissionDefault defaultValue, Map<String, Boolean> children)
	{
		boolean childrenChanged = false;
		boolean defaultChanged = false;
				
		if ( ! permission.getChildren().equals(children))
		{
			// Recalculation need created: TRUE
			// Recalculation auto-performed: FALSE
			permission.getChildren().clear();
			permission.getChildren().putAll(children);
			childrenChanged = true;
		}
		
		if (permission.getDefault() != defaultValue)
		{
			// Recalculation need created: TRUE
			// Recalculation auto-performed: TRUE
			permission.setDefault(defaultValue);
			defaultChanged = true;
		}
		
		// Only recalculate if default wasn't changed since that would have caused a recalculation
		if (childrenChanged && ! defaultChanged)
		{
			// Manual Recalculation
			permission.recalculatePermissibles();
		}
	}
	
	// THREE FIELDS
	
	public static void set(Permission permission, String description, PermissionDefault defaultValue, Map<String, Boolean> children)
	{
		set(permission, defaultValue, children);
		set(permission, description);
	}
	
	// -------------------------------------------- //
	// GET PERMISSION
	// -------------------------------------------- //
	
	// This is the original logic
	// The other below are just copy pastes with argument permutation 
	public static Permission get(boolean create, boolean update, String name, String description, PermissionDefault defaultValue, Map<String, Boolean> children)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			if (create)
			{
				ret = new Permission(name, description, defaultValue, children);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		else
		{
			if (update)
			{
				set(ret, description, defaultValue, children);
			}
		}
		return ret;
	}
	
	// ZERO FIELDS
	
	public static Permission get(boolean create, String name)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			if (create)
			{
				ret = new Permission(name);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		return ret;
	}
	
	// ONE FIELD
	
	public static Permission get(boolean create, boolean update, String name, String description)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			if (create)
			{
				ret = new Permission(name, description);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		else
		{
			if (update)
			{
				set(ret, description);
			}
		}
		return ret;
	}
	
	public static Permission get(boolean create, boolean update, String name, PermissionDefault defaultValue)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			if (create)
			{
				ret = new Permission(name, defaultValue);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		else
		{
			if (update)
			{
				set(ret, defaultValue);
			}
		}
		return ret;
	}
	
	public static Permission get(boolean create, boolean update, String name, Map<String, Boolean> children)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			if (create)
			{
				ret = new Permission(name, children);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		else
		{
			if (update)
			{
				set(ret, children);
			}
		}
		return ret;
	}
	
	// TWO FIELDS
	
	public static Permission get(boolean create, boolean update, String name, String description, PermissionDefault defaultValue)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			if (create)
			{
				ret = new Permission(name, description, defaultValue);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		else
		{
			if (update)
			{
				set(ret, description, defaultValue);
			}
		}
		return ret;
	}
	
	public static Permission get(boolean create, boolean update, String name, String description, Map<String, Boolean> children)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			if (create)
			{
				ret = new Permission(name, description, children);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		else
		{
			if (update)
			{
				set(ret, description, children);
			}
		}
		return ret;
	}
	
	public static Permission get(boolean create, boolean update, String name, PermissionDefault defaultValue, Map<String, Boolean> children)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(name);
		if (ret == null)
		{
			if (create)
			{
				ret = new Permission(name, defaultValue, children);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		else
		{
			if (update)
			{
				set(ret, defaultValue, children);
			}
		}
		return ret;
	}
}
