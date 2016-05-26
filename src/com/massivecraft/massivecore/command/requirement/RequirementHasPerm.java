package com.massivecraft.massivecore.command.requirement;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.util.PermissionUtil;

public class RequirementHasPerm extends RequirementAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static RequirementHasPerm get(String permissionId) { return new RequirementHasPerm(permissionId); }
	public static RequirementHasPerm get(Identified identified) { return new RequirementHasPerm(identified); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public RequirementHasPerm(String permissionId)
	{
		this.permissionId = permissionId;
	}
	
	public RequirementHasPerm(Identified identified)
	{
		this(identified.getId());
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
