package com.massivecraft.mcore5.cmd.req;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.mcore5.util.Perm;

public class ReqHasPerm implements IReq
{
	@Getter @Setter private String perm;
	
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
