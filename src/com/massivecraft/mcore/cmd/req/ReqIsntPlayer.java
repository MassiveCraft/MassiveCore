package com.massivecraft.mcore.cmd.req;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.Lang;
import com.massivecraft.mcore.cmd.MCommand;

public class ReqIsntPlayer extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqIsntPlayer i = new ReqIsntPlayer();
	public static ReqIsntPlayer get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MCommand command)
	{
		return ! (sender instanceof Player);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return Lang.COMMAND_SENDER_MUSNT_BE_PLAYER;
	}
	
}
