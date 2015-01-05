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
	// OVERRIDE: PLUGIN IDENTIFIABLE COMMAND
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return massiveCommand.getRegisteredPlugin();
	}
	
	// -------------------------------------------- //
	// OVERRIDE: EXECUTE
	// -------------------------------------------- //
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args)
	{
		List<String> argList = this.createArgList(args);
		this.massiveCommand.execute(sender, argList);
		return true;
	}
	
	public List<String> createArgList(String[] args)
	{
		List<String> ret;
		if (this.massiveCommand.isUsingTokenizer())
		{
			ret = Txt.tokenizeArguments(Txt.implode(args, " "));
		}
		else
		{
			ret = new ArrayList<String>(Arrays.asList(args));
		}
		
		if (this.massiveCommand.isUsingSmartQuotesRemoval())
		{
			List<String> oldArgList = ret;
			ret = new ArrayList<String>();
			for (String arg : oldArgList)
			{
				ret.add(Txt.removeSmartQuotes(arg));
			}
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: TAB COMPLETE
	// -------------------------------------------- //
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		String tokenlc = args[args.length - 1].toLowerCase();
		
		// First we need a command to find the subcommands in.
		MassiveCommand cmd = this.getSubCommand(args, massiveCommand);
		
		// So if we found a command, and it has subcommands
		// we will suggest the aliases instead of senders.
		if (cmd != null && cmd.getSubCommands().size() > 0)
		{	
			// If we compiled in Java 8, I could have used streams :(
			for (MassiveCommand sub : cmd.getSubCommands())
			{
				if ( ! sub.isVisibleTo(sender)) continue;
				for (String subAlias : sub.getAliases())
				{
					if ( ! subAlias.toLowerCase().startsWith(tokenlc)) continue;
					ret.add(subAlias);
				}
			}
			
			// return, so senders is not suggested.
			return new ArrayList<String>(ret);
		}
		
		// If subcommands didn't work, we will try with senders.
		List<String> superRet = super.tabComplete(sender, alias, args);
		if (args.length == 0) return superRet;
		
		ret.addAll(superRet);
		
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
	// PRIVATE: TAB COMPLETE
	// -------------------------------------------- //
	
	private MassiveCommand getSubCommand(String[] args, MassiveCommand cmd)
	{
		// First we look in the basecommand, then in its subcommands,
		// and the next subcommand and so on.
		// Until we run out of args or no subcommand is found.
		for (int i = 0; i < args.length-1; i++)
		{
			String arg = args[i];
			// If no subcommand is found we will not look further.
			if (cmd == null) break;

			// We have to loop through the subcommands to see if any match the arg.
			// if none exists we will get null, thus we break in case of null.
			cmd = this.getSubCommand(arg, cmd);
		}
		
		return cmd;
	}
	
	private MassiveCommand getSubCommand(String arg, MassiveCommand cmd)
	{
		if (cmd == null || arg == null) return null;

		// We will look in all its subcommands
		for (MassiveCommand sub : cmd.getSubCommands())
		{
			// and in all those look for an alias that matches
			for (String subAlias : sub.getAliases())
			{
				// If it matched we had success
				if (subAlias.equalsIgnoreCase(arg)) return sub;
			}
		}
		return null;
	}
	
}
