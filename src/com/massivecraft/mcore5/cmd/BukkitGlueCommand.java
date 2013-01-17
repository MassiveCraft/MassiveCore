package com.massivecraft.mcore5.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.MPlugin;
import com.massivecraft.mcore5.util.Txt;

public class BukkitGlueCommand extends Command
{
	public final MCommand mcommand;
	public final MPlugin mplugin;
	
	public BukkitGlueCommand(MCommand mcommand, MPlugin mplugin)
	{
		super(mcommand.getAliases().get(0), mcommand.getDesc(), mcommand.getUseageTemplate(), mcommand.getAliases());
		this.mcommand = mcommand;
		this.mplugin = mplugin;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args)
	{
		if ( ! mplugin.isEnabled()) return false;
		this.mcommand.execute(sender, Txt.tokenizeArguments(Txt.implode(args, " ")));
		return true;
	}
}
