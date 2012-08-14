package com.massivecraft.mcore4.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.cmd.MCommand;
import com.massivecraft.mcore4.util.Perm;

public class ReqHasPerm implements IReq
{
	private String perm;
	public String getPerm() { return this.perm; }
	public void setPerm(String val) { this.perm = val; }
	
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
		return Perm.getForbiddenMessage(this.perm);
	}
	
	public static ReqHasPerm get(String perm)
	{
		return new ReqHasPerm(perm);
	}

}
