package com.massivecraft.massivecore.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class MassiveCoreBukkitCommand extends Command implements PluginIdentifiableCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final MassiveCommand massiveCommand;
	public MassiveCommand getMassiveCommand() { return this.massiveCommand; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MassiveCoreBukkitCommand(String name, MassiveCommand massiveCommand)
	{
		super(
			name,
			massiveCommand.getDesc(),
			massiveCommand.getUseageTemplate(),
			new ArrayList<String>() // We don't use aliases
		);
		this.massiveCommand = massiveCommand;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: EXECUTE
	// -------------------------------------------- //
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args)
	{
		List<String> argList;
		if (this.massiveCommand.isUsingTokenizer())
		{
			argList = Txt.tokenizeArguments(Txt.implode(args, " "));
		}
		else
		{
			argList = new ArrayList<String>(Arrays.asList(args));
		}
		
		if (this.massiveCommand.isUsingSmartQuotesRemoval())
		{
			List<String> oldArgList = argList;
			argList = new ArrayList<String>();
			for (String arg : oldArgList)
			{
				argList.add(Txt.removeSmartQuotes(arg));
			}
		}
		
		this.massiveCommand.execute(sender, argList);
		
		return true;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: TAB COMPLETE
	// -------------------------------------------- //
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException
	{
		List<String> superRet = super.tabComplete(sender, alias, args);
		if (args.length == 0) return superRet;
		
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		ret.addAll(superRet);
		
		String tokenlc = args[args.length - 1].toLowerCase();
		
		// Add names of all online senders that match and isn't added yet.
		for (String senderName : IdUtil.getOnlineNames())
		{
			if (!senderName.toLowerCase().startsWith(tokenlc)) continue;
			if (!Mixin.canSee(sender, senderName)) continue;
			ret.add(senderName);
		}
		
		return new ArrayList<String>(ret);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: PLUGIN IDENTIFIABLE COMMAND
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return massiveCommand.getRegisteredPlugin();
	}
	
}
