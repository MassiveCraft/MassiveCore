package com.massivecraft.mcore2.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
		
		List<String> argList = new ArrayList<String>(Arrays.asList(args));
		this.mcommand.execute(sender, argList);
		return true;
	}
}
