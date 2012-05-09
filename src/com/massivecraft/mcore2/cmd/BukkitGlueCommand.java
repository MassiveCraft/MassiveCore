package com.massivecraft.mcore2.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore2.util.Txt;

public class BukkitGlueCommand extends Command
{
	protected MCommand mcommand;
	public BukkitGlueCommand(MCommand mcommand)
	{
		super(mcommand.getAliases().get(0), mcommand.getDesc(), mcommand.getUseageTemplate(), mcommand.getAliases());
		this.mcommand = mcommand;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args)
	{
		if ( ! mcommand.p().isEnabled())
		{
            return false;
        }
		
		this.mcommand.execute(sender, Txt.tokenizeArguments(Txt.implode(args, " ")));
		return true;
	}
}
