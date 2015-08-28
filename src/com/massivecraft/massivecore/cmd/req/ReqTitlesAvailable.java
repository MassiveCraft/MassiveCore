package com.massivecraft.massivecore.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

public class ReqTitlesAvailable extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqTitlesAvailable i = new ReqTitlesAvailable();
	public static ReqTitlesAvailable get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		return Mixin.isTitlesAvailable();
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		return Txt.parse(Lang.COMMAND_TITLES_MUST_BE_AVAILABLE);
	}
	
}
