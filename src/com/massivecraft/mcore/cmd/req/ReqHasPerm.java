package com.massivecraft.mcore.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.util.PermUtil;

public class ReqHasPerm implements IReq
{
	private String perm;
	public String getPerm() { return this.perm; }
	public void setPerm(String perm) { this.perm = perm; }
	
	public ReqHasPerm(String perm)
	{
		this.perm = perm;
	}
	
	@Override
	public boolean test(CommandSender sender, MCommand command)
	{
		return sender.hasPermission(this.perm);
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return PermUtil.getForbiddenMessage(this.perm);
	}
	
	public static ReqHasPerm get(String perm)
	{
		return new ReqHasPerm(perm);
	}

}
