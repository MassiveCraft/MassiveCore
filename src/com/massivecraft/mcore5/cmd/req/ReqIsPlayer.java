package com.massivecraft.mcore5.cmd.req;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore5.Lang;
import com.massivecraft.mcore5.cmd.MCommand;

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
	
	public static ReqIsPlayer get()
	{
		return instance;
	}
}
