package com.massivecraft.massivecore.command.requirement;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.command.CommandSender;

public class RequirementHasPerm extends RequirementAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static RequirementHasPerm get(Object permission) { return new RequirementHasPerm(permission); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public RequirementHasPerm(Object permission)
	{
		this.permissionId = PermissionUtil.asPermissionId(permission);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String permissionId;
	public String getPermissionId() { return this.permissionId; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		return sender.hasPermission(this.permissionId);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		return PermissionUtil.getPermissionDeniedMessage(this.permissionId);
	}
	
}
