package com.massivecraft.mcore.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.util.PermUtil;

public class ReqHasPerm extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqHasPerm get(String perm) { return new ReqHasPerm(perm); }
	public ReqHasPerm(String perm) { this.perm = perm; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String perm;
	public String getPerm() { return this.perm; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MCommand command)
	{
		return sender.hasPermission(this.perm);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return PermUtil.getDeniedMessage(this.perm);
	}
	
}
