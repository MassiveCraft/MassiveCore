package com.massivecraft.mcore5.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.MPlugin;
import com.massivecraft.mcore5.mixin.Mixin;
import com.massivecraft.mcore5.util.Txt;

public class BukkitGlueCommand extends Command
{
	public final MCommand mcommand;
	public final MPlugin mplugin;
	
	public BukkitGlueCommand(String name, MCommand mcommand, MPlugin mplugin)
	{
		super(name, mcommand.getDesc(), mcommand.getUseageTemplate(), new ArrayList<String>());
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
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException
	{
		List<String> superRet = super.tabComplete(sender, alias, args);
		if (args.length == 0) return superRet;
		
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		ret.addAll(superRet);
		
		String tokenlc = args[args.length - 1].toLowerCase();
		
		// Add ids of all online senders that match and isn't added yet. 
		for (String senderId : Mixin.getOnlineSenderIds())
		{
			if (!senderId.toLowerCase().startsWith(tokenlc)) continue;
			if (!Mixin.isVisible(sender, senderId)) continue;
			ret.add(senderId);
		}
		
		return new ArrayList<String>(ret);
	}
}
