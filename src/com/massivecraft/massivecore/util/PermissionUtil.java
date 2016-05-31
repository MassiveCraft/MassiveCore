package com.massivecraft.massivecore.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.event.EventMassiveCorePermissionDeniedFormat;
import com.massivecraft.massivecore.nms.NmsPermissions;

public class PermissionUtil
{
	// -------------------------------------------- //
	// RANDOM UTILS
	// -------------------------------------------- //
	
	// TODO: Gather more versions spread out over plugins,
	// TODO: Place them all here. Were there some in MUtil?
	
	public static <T> T pickFirstVal(Permissible permissible, Map<String, T> perm2val)
	{
		if (perm2val == null) return null;
		T ret = null;
		
		for (Entry<String, T> entry : perm2val.entrySet())
		{
			ret = entry.getValue();
			if (hasPermission(permissible, entry.getKey())) break;
		}
		
		return ret;
	}
	
	public static String createPermissionId(Plugin plugin, Enum<?> e)
	{
		return plugin.getName().toLowerCase() + "." + e.name().toLowerCase().replace('_', '.'); 
	}
	
	// -------------------------------------------- //
	// ENSURE HAS
	// -------------------------------------------- //
	
	// TODO: This methodology I don't beleive in any more.
	// TODO: Rework and improve technology.
	
	public static void ensureHas(Permissible permissible, String permissionName)
	{
		if (permissible.hasPermission(permissionName))
		{
			return;
		}
		else
		{
			permissible.addAttachment(MassiveCore.get(), permissionName, true);
		}
	}
	
	public static void ensureHas(Permissible permissible, Permission permission)
	{
		ensureHas(permissible, permission.getName());
	}
	
	// -------------------------------------------- //
	// PERMISSION > CONSTRUCT
	// -------------------------------------------- //
	// Our own constructor for name symmetry.
	// Supplying null means using the default values.
	
	public static Permission constructPermission(String id, String description, PermissionDefault standard, Map<String, Boolean> children)
	{
		if (id == null) throw new NullPointerException("id");
		return new Permission(id, description, standard, children);
	}
	
	// -------------------------------------------- //
	// PERMISSION > GET
	// -------------------------------------------- //
	// We supply and make use of our own getters for the same of naming symmetry.
	// They are just wrappers but if we have setters we might as well have getters.
	
	// According to MassiveCraft names are changeable and ids are not.
	// Bukkit permissions lack names. They do however have ids. 
	public static String getPermissionId(Permission permission)
	{
		return permission.getName();
	}
	
	public static String getPermissionDescription(Permission permission)
	{
		return permission.getDescription();
	}
	
	// Default is a reserved Java keyword. Standard will work better.
	public static PermissionDefault getPermissionStandard(Permission permission)
	{
		return permission.getDefault();
	}
	
	public static Map<String, Boolean> getPermissionChildren(Permission permission)
	{
		return permission.getChildren();
	}
	
	// -------------------------------------------- //
	// PERMISSION > SET
	// -------------------------------------------- //
	// These setters offer bulk-ways of updating already created permissions.
	// For the best performance you should enter all information into the Permission constructor when creating the Permission.
	// At times you will however need to update permissions after they were created.
	// In these cases the order and approach with witch you alter the fields matter a lot performance wise.
	// These setter will ensure you get optimal performance.
	// NoChange detection is also included to avoid recalculating in vain.
	// Supplying null means no change is actually requested.

	// ONE FIELD
	
	public static boolean setPermissionDescription(Permission permission, String description)
	{
		if (permission == null) throw new NullPointerException("permission");
		if (description == null) return false;
		
		String before = getPermissionDescription(permission);
		if (description.equals(before)) return false;
		
		// Recalculation need created: FALSE
		// Recalculation auto-performed: FALSE
		permission.setDescription(description);
		
		return true;
	}
	
	public static boolean setPermissionStandard(Permission permission, PermissionDefault standard)
	{
		if (permission == null) throw new NullPointerException("permission");
		if (standard == null) return false;
		
		// NoChange
		PermissionDefault before = getPermissionStandard(permission);
		if (standard == before) return false;
		
		// Recalculation need created: TRUE
		// Recalculation auto-performed: TRUE
		permission.setDefault(standard);
		
		return true;
	}
	
	public static boolean setPermissionChildren(Permission permission, Map<String, Boolean> children)
	{
		if (permission == null) throw new NullPointerException("permission");
		if (children == null) return false;
		
		// NoChange
		Map<String, Boolean> before = getPermissionChildren(permission);
		if (children.equals(before)) return false;
		
		// Recalculation need created: TRUE
		// Recalculation auto-performed: FALSE
		permission.getChildren().clear();
		permission.getChildren().putAll(children);
		
		// Manual Recalculation
		permission.recalculatePermissibles();
		
		return true;
	}
	
	// TWO FIELDS
	
	public static boolean setPermissionDescriptionStandard(Permission permission, String description, PermissionDefault standard)
	{
		boolean ret = false;
		ret |= setPermissionDescription(permission, description);
		ret |= setPermissionStandard(permission, standard);
		return ret;
	}
	
	public static boolean setPermissionDescriptionChildren(Permission permission, String description, Map<String, Boolean> children)
	{
		boolean ret = false;
		ret |= setPermissionDescription(permission, description);
		ret |= setPermissionChildren(permission, children);
		return ret;
	}
	
	public static boolean setPermissionStandardChildren(Permission permission, PermissionDefault standard, Map<String, Boolean> children)
	{
		boolean childrenChanged = false;
		boolean standardChanged = false;
		
		
		if (children != null && ! children.equals(getPermissionChildren(permission)))
		{
			// Recalculation need created: TRUE
			// Recalculation auto-performed: FALSE
			permission.getChildren().clear();
			permission.getChildren().putAll(children);
			childrenChanged = true;
		}
		
		if (standard != null && standard != getPermissionStandard(permission))
		{
			// Recalculation need created: TRUE
			// Recalculation auto-performed: TRUE
			permission.setDefault(standard);
			standardChanged = true;
		}
		
		// Only recalculate if default wasn't changed since that would have caused a recalculation
		if (childrenChanged && ! standardChanged)
		{
			// Manual Recalculation
			permission.recalculatePermissibles();
		}
		
		return childrenChanged || standardChanged;
	}
	
	// THREE FIELDS
	
	public static boolean setPermissionDescriptionStandardChildren(Permission permission, String description, PermissionDefault standard, Map<String, Boolean> children)
	{
		boolean ret = false;
		ret |= setPermissionStandardChildren(permission, standard, children);
		ret |= setPermissionDescription(permission, description);
		return ret;
	}
	
	// -------------------------------------------- //
	// PERMISSION > GET (CREATE / UPDATE)
	// -------------------------------------------- //
	
	public static Permission getPermission(boolean create, boolean update, String id, String description, PermissionDefault standard, Map<String, Boolean> children)
	{
		Permission ret = Bukkit.getPluginManager().getPermission(id);
		if (ret == null)
		{
			if (create)
			{
				ret = constructPermission(id, description, standard, children);
				Bukkit.getPluginManager().addPermission(ret);
			}
		}
		else
		{
			if (update)
			{
				setPermissionDescriptionStandardChildren(ret, description, standard, children);
			}
		}
		return ret;
	}
	
	// ZERO FIELDS
	
	public static Permission getPermission(boolean create, String id)
	{
		return getPermission(create, false, id, null, null, null);
	}
	
	// ONE FIELD
	
	public static Permission getPermission(boolean create, boolean update, String id, String description)
	{
		return getPermission(create, update, id, description, null, null);
	}
	
	public static Permission getPermission(boolean create, boolean update, String id, PermissionDefault standard)
	{
		return getPermission(create, update, id, null, standard, null);
	}
	
	public static Permission getPermission(boolean create, boolean update, String id, Map<String, Boolean> children)
	{
		return getPermission(create, update, id, null, null, children);
	}
	
	// TWO FIELDS
	
	public static Permission getPermission(boolean create, boolean update, String id, String description, PermissionDefault standard)
	{
		return getPermission(create, update, id, description, standard, null);
	}
	
	public static Permission getPermission(boolean create, boolean update, String id, String description, Map<String, Boolean> children)
	{
		return getPermission(create, update, id, description, null, children);
	}
	
	public static Permission getPermission(boolean create, boolean update, String id, PermissionDefault standard, Map<String, Boolean> children)
	{
		return getPermission(create, update, id, null, standard, children);
	}
	
	// -------------------------------------------- //
	// PERMISSION > ACTION
	// -------------------------------------------- //
	// We declare a fake field called "action".
	// The action is usually the description.
	// It can however never be null.
	// The action should fit into the format for a denied message such as:
	// You don't have permission to FLY TO THE MOON.
	
	public static String getPermissionAction(String permissionId)
	{
		if (permissionId == null) return Lang.PERM_DEFAULT_DESCRIPTION;
		Permission permission = Bukkit.getPluginManager().getPermission(permissionId);
		return getPermissionAction(permission);
	}
	
	public static String getPermissionAction(Permission permission)
	{
		if (permission == null) return Lang.PERM_DEFAULT_DESCRIPTION;
		String ret = getPermissionDescription(permission);
		if (ret == null || ret.isEmpty()) ret = Lang.PERM_DEFAULT_DESCRIPTION;
		return ret;
	}
	
	// -------------------------------------------- //
	// PERMISSION > DENIED FORMAT
	// -------------------------------------------- //
	
	public static String getPermissionDeniedFormat(String permissionId)
	{
		EventMassiveCorePermissionDeniedFormat event = new EventMassiveCorePermissionDeniedFormat(permissionId);
		event.run();
		String ret = event.getFormat();
		if (ret == null) ret = Lang.PERM_DEFAULT_DENIED_FORMAT;
		return ret;
	}
	
	public static String getPermissionDeniedFormat(Permission permission)
	{
		return getPermissionDeniedFormat(permission == null ? null : permission.getName());
	}
	
	// -------------------------------------------- //
	// PERMISSION > DENIED MESSAGE
	// -------------------------------------------- //
	
	public static String getPermissionDeniedMessage(String permissionId)
	{
		String deniedFormat = getPermissionDeniedFormat(permissionId);
		String action = getPermissionAction(permissionId);
		return Txt.parse(deniedFormat, action);
	}
	
	public static String getPermissionDeniedMessage(Permission permission)
	{
		String deniedFormat = getPermissionDeniedFormat(permission);
		String action = getPermissionAction(permission);
		return Txt.parse(deniedFormat, action);
	}
	
	// -------------------------------------------- //
	// PERMISSION > HAS
	// -------------------------------------------- //
	
	public static boolean hasPermission(Permissible permissable, Permission permission)
	{
		return hasPermission(permissable, permission.getName());
	}
	
	public static boolean hasPermission(Permissible permissable, String permissionId)
	{
		if (permissable == null) return false;
		return permissable.hasPermission(permissionId);
	}
	
	public static boolean hasPermission(Permissible permissable, Permission permission, boolean verbose)
	{
		return hasPermission(permissable, permission.getName(), verbose);
	}
	
	public static boolean hasPermission(Permissible permissible, String permissionId, boolean verbose)
	{
		if (hasPermission(permissible, permissionId))
		{
			return true;
		}
		else if (verbose && permissible != null)
		{
			if (permissible instanceof CommandSender)
			{
				CommandSender sender = (CommandSender)permissible;
				sender.sendMessage(getPermissionDeniedMessage(permissionId));
			}
		}
		return false;
	}
	
	// -------------------------------------------- //
	// PERMISSIBLE > BASE
	// -------------------------------------------- //
	
	public static PermissibleBase getPermissibleBase(Permissible permissible)
	{
		return NmsPermissions.get().getBase(permissible);
	}
	
	// -------------------------------------------- //
	// PERMISSIBLE > ATTACHMENT
	// -------------------------------------------- //
	
	// The Bukkit version recalculates permissions which does not make any sense.
	// An empty attachment is not going to affect the effective permissions.
	// Thus we make use of our own attachment creator that does not waste CPU.
	public static PermissionAttachment createPermissibleAttachment(Permissible permissible, Plugin plugin)
	{
		if (permissible == null) throw new NullPointerException("permissible");
		if (plugin == null) throw new NullPointerException("plugin");
		
		List<PermissionAttachment> attachments = getPermissibleAttachments(permissible);
		if (attachments == null) return null;
		
		PermissionAttachment ret = new PermissionAttachment(plugin, permissible);
		attachments.add(ret);
		
		return ret;
	}
	
	public static List<PermissionAttachment> getPermissibleAttachments(Permissible permissible)
	{
		PermissibleBase base = getPermissibleBase(permissible);
		if (base == null) return null;
		return getBaseAttachments(base);
	}
	
	// This method returns the first attachment belonging to the plugin.
	// The thought process here is that plugins rarely need more than one attachment.
	// With that in mind we offer this per plugin singleton getter.
	public static PermissionAttachment getPermissibleAttachment(Permissible permissible, Plugin plugin, boolean creative)
	{
		List<PermissionAttachment> attachments = getPermissibleAttachments(permissible);
		if (attachments == null) return null;
		
		for (PermissionAttachment attachment : attachments)
		{
			if (MUtil.equals(attachment.getPlugin(), plugin)) return attachment;
		}
		
		if (creative) return createPermissibleAttachment(permissible, plugin);
		
		return null;
	}
	
	// -------------------------------------------- //
	// BASE > ATTACHMENT
	// -------------------------------------------- //
	
	public static List<PermissionAttachment> getBaseAttachments(PermissibleBase base)
	{
		return NmsPermissions.get().getAttachments(base);
	}
	
	// -------------------------------------------- //
	// ATTACHMENT > PERMISSIONS
	// -------------------------------------------- //
	
	public static Map<String, Boolean> getAttachmentPermissions(PermissionAttachment attachment)
	{
		return NmsPermissions.get().getAttachmentPermissions(attachment);
	}	
	
	public static boolean setAttachmentPermissions(PermissionAttachment attachment, Map<String, Boolean> permissions)
	{
		if (attachment == null) throw new NullPointerException("attachment");
		if (permissions == null) return false;
		
		Map<String, Boolean> before = getAttachmentPermissions(attachment);
		if (before.equals(permissions)) return false;
		
		before.clear();
		before.putAll(permissions);
		
		Permissible permissible = attachment.getPermissible();
		if (permissible != null) permissible.recalculatePermissions();
		
		return true;
	}
	
}
