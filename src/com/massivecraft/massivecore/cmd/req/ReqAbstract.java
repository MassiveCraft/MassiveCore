package com.massivecraft.massivecore.cmd.req;

import java.io.Serializable;

import org.bukkit.command.CommandSender;

public abstract class ReqAbstract implements Req, Serializable
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean apply(CommandSender sender)
	{
		return this.apply(sender, null);
	}

	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return this.createErrorMessage(sender, null);
	}
	
}
