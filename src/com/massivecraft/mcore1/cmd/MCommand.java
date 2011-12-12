package com.massivecraft.mcore1.cmd;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore1.Lang;
import com.massivecraft.mcore1.plugin.MPlugin;

public abstract class MCommand
{
	public abstract MPlugin getPlugin();
	
	// The sub-commands to this command
	public List<MCommand> subCommands;
	public void addSubCommand(MCommand subCommand)
	{
		subCommand.commandChain.addAll(this.commandChain);
		subCommand.commandChain.add(this);
		this.subCommands.add(subCommand);
	}
	
	// The different names this commands will react to  
	public List<String> aliases;
	
	// Information on the args
	public List<String> requiredArgs;
	public LinkedHashMap<String, String> optionalArgs;
	public boolean errorOnToManyArgs = true;
	
	// FIELD: Help Short
	// This field may be left blank and will in such case be loaded from the permissions node instead.
	// Thus make sure the permissions node description is an action description like "eat hamburgers" or "do admin stuff".
	private String helpShort;
	public void setHelpShort(String val) { this.helpShort = val; }
	public String getHelpShort()
	{
		if (this.helpShort == null)
		{
			String pdesc = getPlugin().perm.getPermissionDescription(this.permission);
			if (pdesc != null)
			{
				return pdesc;
			}
			return "*info unavailable*";
		}
		return this.helpShort;
	}
	
	public List<String> helpLong;
	//public CommandVisibility visibility; // ??? abstract method only??
	
	// Some information on permissions
	public boolean senderMustBePlayer;
	public String permission;
	
	// Information available on execution of the command
	public CommandSender sender; // Will always be set
	public Player me; // Will only be set when the sender is a player
	public boolean senderIsConsole;
	public List<String> args; // Will contain the arguments, or and empty list if there are none.
	public List<MCommand> commandChain = new ArrayList<MCommand>(); // The command chain used to execute this command
	
	public MCommand()
	{
		this.permission = null;
		
		this.subCommands = new ArrayList<MCommand>();
		this.aliases = new ArrayList<String>();
		
		this.requiredArgs = new ArrayList<String>();
		this.optionalArgs = new LinkedHashMap<String, String>();
		
		this.helpShort = null;
		this.helpLong = new ArrayList<String>();
		//this.visibility = CommandVisibility.VISIBLE;
	}
	
	// The commandChain is a list of the parent command chain used to get to this command.
	public void execute(CommandSender sender, List<String> args, List<MCommand> commandChain)
	{
		// Set the execution-time specific variables
		this.sender = sender;
		if (sender instanceof Player)
		{
			this.me = (Player)sender;
			this.senderIsConsole = false;
		}
		else
		{
			this.me = null;
			this.senderIsConsole = true;
		}
		this.args = args;
		this.commandChain = commandChain;

		// Is there a matching sub command?
		if (args.size() > 0 )
		{
			for (MCommand subCommand: this.subCommands)
			{
				if (subCommand.aliases.contains(args.get(0)))
				{
					args.remove(0);
					commandChain.add(this);
					subCommand.execute(sender, args, commandChain);
					return;
				}
			}
		}
		
		if ( ! validCall(this.sender, this.args)) return;
		
		if ( ! this.isEnabled()) return;
		
		perform();
	}
	
	public void execute(CommandSender sender, List<String> args)
	{
		execute(sender, args, new ArrayList<MCommand>());
	}
	
	// This is where the command action is performed.
	public abstract void perform();
	
	
	// -------------------------------------------- //
	// Call Validation
	// -------------------------------------------- //
	
	/**
	 * In this method we validate that all prerequisites to perform this command has been met.
	 */
	public boolean validCall(CommandSender sender, List<String> args)
	{
		if ( ! validSenderType(sender, true))
		{
			return false;
		}
		
		if ( ! validSenderPermissions(sender, true))
		{
			return false;
		}
		
		if ( ! validArgs(args, sender))
		{
			return false;
		}
		
		return true;
	}
	
	public boolean isEnabled()
	{
		return true;
	}
	
	public boolean validSenderType(CommandSender sender, boolean informSenderIfNot)
	{
		if (this.senderMustBePlayer && ! (sender instanceof Player))
		{
			if (informSenderIfNot)
			{
				msg(Lang.commandSenderMustBePlayer);
			}
			return false;
		}
		return true;
	}
	
	public boolean validSenderPermissions(CommandSender sender, boolean informSenderIfNot)
	{
		if (this.permission == null) return true;
		return getPlugin().perm.has(sender, this.permission, informSenderIfNot);
	}
	
	public boolean validArgs(List<String> args, CommandSender sender)
	{
		if (args.size() < this.requiredArgs.size())
		{
			if (sender != null)
			{
				msg(Lang.commandToFewArgs);
				sender.sendMessage(this.getUseageTemplate());
			}
			return false;
		}
		
		if (args.size() > this.requiredArgs.size() + this.optionalArgs.size() && this.errorOnToManyArgs)
		{
			if (sender != null)
			{
				// Get the to many string slice
				List<String> theToMany = args.subList(this.requiredArgs.size() + this.optionalArgs.size(), args.size());
				msg(Lang.commandToManyArgs, getPlugin().txt.implode(theToMany, " "));
				sender.sendMessage(this.getUseageTemplate());
			}
			return false;
		}
		return true;
	}
	public boolean validArgs(List<String> args)
	{
		return this.validArgs(args, null);
	}
	
	// -------------------------------------------- //
	// Help and Usage information
	// -------------------------------------------- //
	
	public String getUseageTemplate(List<MCommand> commandChain, boolean addShortHelp)
	{
		StringBuilder ret = new StringBuilder();
		ret.append(getPlugin().txt.getDesign().getColorCommand());
		ret.append('/');
		
		for (MCommand mc : commandChain)
		{
			ret.append(getPlugin().txt.implode(mc.aliases, ","));
			ret.append(' ');
		}
		
		ret.append(getPlugin().txt.implode(this.aliases, ","));
		
		List<String> args = new ArrayList<String>();
		
		for (String requiredArg : this.requiredArgs)
		{
			args.add("<"+requiredArg+">");
		}
		
		for (Entry<String, String> optionalArg : this.optionalArgs.entrySet())
		{
			String val = optionalArg.getValue();
			if (val == null)
			{
				val = "";
			}
			else
			{
				val = "="+val;
			}
			args.add("["+optionalArg.getKey()+val+"]");
		}
		
		if (args.size() > 0)
		{
			ret.append(getPlugin().txt.getDesign().getColorParameter());
			ret.append(' ');
			ret.append(getPlugin().txt.implode(args, " "));
		}
		
		if (addShortHelp)
		{
			ret.append(' ');
			ret.append(getPlugin().txt.getDesign().getColorInfo());
			ret.append(this.getHelpShort());
		}
		
		return ret.toString();
	}
	
	public String getUseageTemplate(boolean addShortHelp)
	{
		return getUseageTemplate(this.commandChain, addShortHelp);
	}
	
	public String getUseageTemplate()
	{
		return getUseageTemplate(false);
	}
	
	// -------------------------------------------- //
	// Message Sending Helpers
	// -------------------------------------------- //
	
	public void msg(String str, Object... args)
	{
		sender.sendMessage(getPlugin().txt.parse(str, args));
	}
	
	public void msg(String str)
	{
		sender.sendMessage(getPlugin().txt.parse(str));
	}
	
	public void sendMessage(String msg)
	{
		sender.sendMessage(msg);
	}
	
	public void sendMessage(List<String> msgs)
	{
		for(String msg : msgs)
		{
			this.sendMessage(msg);
		}
	}
	
	// -------------------------------------------- //
	// Argument Readers
	// -------------------------------------------- //
	
	public String arg(int idx)
	{
		if ( ! this.argIsSet(idx)) return null;
		return this.args.get(idx);
	}
	
	public boolean argIsSet(int idx)
	{
		if (this.args.size() < idx+1)
		{
			return false;
		}
		return true;
	}
	
	public synchronized <T> T argAs(int idx, Class<T> clazz, String style, T defaultNotSet, T defaultNotFound)
	{
		if ( ! this.argIsSet(idx))
		{
			return defaultNotSet;
		}
		IArgHandler<T> handler = getPlugin().cmd.getArgHandler(clazz);
		T ret = handler.parse(this.arg(idx), style, this.sender, getPlugin());
		if (ret == null)
		{
			this.msg(handler.error());
			return defaultNotFound;
		}
		return ret;
	}
	
	public <T> T argAs(int idx, Class<T> clazz,T defaultNotSet, T defaultNotFound)
	{
		return this.argAs(idx, clazz, null, defaultNotSet, defaultNotFound);
	}
	
	public <T> T argAs(int idx, Class<T> clazz,T defaultNotSet)
	{
		return this.argAs(idx, clazz, null, defaultNotSet, null);
	}
	
	public <T> T argAs(int idx, Class<T> clazz)
	{
		return this.argAs(idx, clazz, null, null);
	}
}
