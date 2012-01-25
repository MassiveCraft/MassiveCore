package com.massivecraft.mcore2dev.cmd.req;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore2dev.Lang;
import com.massivecraft.mcore2dev.cmd.MCommand;

public class ReqIsPlayer implements IReq
{
	@Override
	public boolean test(CommandSender sender, MCommand command)
	{
		return sender instanceof Player;
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return Lang.commandSenderMustBePlayer;
	}
	
	protected static ReqIsPlayer instance= new ReqIsPlayer();
	public static ReqIsPlayer getInstance()
	{
		return instance;
	}
}
