package com.massivecraft.massivecore.cmd.req;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.cmd.MassiveCommand;

public class ReqIsPlayer extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqIsPlayer i = new ReqIsPlayer();
	public static ReqIsPlayer get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		return sender instanceof Player;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		return Lang.COMMAND_SENDER_MUST_BE_PLAYER;
	}
	
}
