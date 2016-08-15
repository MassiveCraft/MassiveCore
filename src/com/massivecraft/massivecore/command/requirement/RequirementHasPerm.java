package com.massivecraft.massivecore.command.requirement;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.util.PermUtil;

public class RequirementHasPerm extends RequirementAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static RequirementHasPerm get(String perm) { return new RequirementHasPerm(perm); }
	public RequirementHasPerm(String perm) { this.perm = perm; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String perm;
	public String getPerm() { return this.perm; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		return sender.hasPermission(this.perm);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		return PermUtil.getDeniedMessage(this.perm);
	}
	
}
