package com.massivecraft.mcore1.cmd;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore1.Lang;
import com.massivecraft.mcore1.MCore;
import com.massivecraft.mcore1.persist.IClassManager;
import com.massivecraft.mcore1.persist.Persist;
import com.massivecraft.mcore1.plugin.MPlugin;

public abstract class MCommand
{
	public abstract MPlugin getPlugin();
	
	// -------------------------------------------- //
	// COMMAND BEHAVIOR
	// -------------------------------------------- //
	
	// FIELD: subCommands
	// The sub-commands to this command
	protected List<MCommand> subCommands;
	public List<MCommand> getSubCommands() { return this.subCommands; }
	public void setSubCommands(List<MCommand> val) { this.subCommands = val; }
	public void addSubCommand(MCommand subCommand)
	{
		subCommand.commandChain.addAll(this.commandChain);
		subCommand.commandChain.add(this);
		this.subCommands.add(subCommand);
	}
	
	// FIELD: aliases
	// The different names this commands will react to  
	protected List<String> aliases;
	public List<String> getAliases() { return this.aliases; }
	public void setAliases(List<String> val) { this.aliases = val; }
	
	// FIELD: requiredArgs
	// These args must always be sent
	protected List<String> requiredArgs;
	public List<String> getRequiredArgs() { return this.requiredArgs; }
	public void setRequiredArgs(List<String> val) { this.requiredArgs = val; }
	public void addRequiredArg(String arg) { this.requiredArgs.add(arg); }
	
	// FIELD: optionalArgs
	// These args are optional
	protected Map<String, String> optionalArgs;
	public Map<String, String> getOptionalArgs() { return this.optionalArgs; }
	public void setOptionalArgs(Map<String, String> val) { this.optionalArgs = val; }
	public void addOptionalArg(String arg, String def) { this.optionalArgs.put(arg, def); }
	
	// FIELD: errorOnToManyArgs
	// Should an error be thrown if "to many" args are sent.
	protected boolean errorOnToManyArgs = true;
	public boolean getErrorOnToManyArgs() { return this.errorOnToManyArgs; }
	public void setErrorOnToManyArgs(boolean val) { this.errorOnToManyArgs = val; }
	
	// FIELD: desc
	// This field may be left blank and will in such case be loaded from the permissions node instead.
	// Thus make sure the permissions node description is an action description like "eat hamburgers" or "do admin stuff".
	protected String desc = null;
	public void setDesc(String val) { this.desc = val; }
	public String getDesc()
	{
		if (this.desc == null)
		{
			String pdesc = getPlugin().perm.getPermissionDescription(this.permission);
			if (pdesc != null)
			{
				return pdesc;
			}
			return "*info unavailable*";
		}
		return this.desc;
	}
	
	// -------------------------------------------- //
	// EXECUTION INFO
	// -------------------------------------------- //
	
	// FIELD: args
	// Will contain the arguments, or and empty list if there are none.
	protected List<String> args;
	public List<String> getArgs() { return this.args; }
	public void setArgs(List<String> val) { this.args = val; }

	// FIELD: commandChain
	// The command chain used to execute this command
	protected List<MCommand> commandChain = new ArrayList<MCommand>();
	public List<MCommand> getCommandChain() { return this.commandChain; }
	public void setCommandChain(List<MCommand> val) { this.commandChain = val; }
	
	// FIELD: sender
	protected CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	public boolean getSenderIsConsole() { return ! (this.sender instanceof Player); }
	public Player getMe()
	{
		if (sender instanceof Player)
		{
			return (Player) sender;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public <T> T getSenderAs(Class<T> clazz)
	{
		if (clazz.isInstance(sender)) return (T) sender;
		
		for (Persist realm : MCore.getPersistInstances().values())
		{
			for (IClassManager<?> manager : realm.getClassManagers().values())
			{
				if ( ! manager.getManagedClass().equals(clazz)) continue;
				if (manager.idCanFix(sender.getClass()) == false) continue;
				return (T) manager.get(sender);
			}
		}
		return null;
	}
	
	// -------------------------------------------- //
	// TODO: PURE DERP
	// -------------------------------------------- //
	
	// Some information on permissions
	// this is part of validating if the sender is ok...
	public boolean senderMustBePlayer;
	public String permission;
	
	public MCommand()
	{
		this.permission = null;
		
		this.subCommands = new ArrayList<MCommand>();
		this.aliases = new ArrayList<String>();
		
		this.requiredArgs = new ArrayList<String>();
		this.optionalArgs = new LinkedHashMap<String, String>();
		
		this.desc = null;
	}
	
	// The commandChain is a list of the parent command chain used to get to this command.
	public void execute(CommandSender sender, List<String> args, List<MCommand> commandChain)
	{
		// Set the execution-time specific variables
		this.sender = sender;
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
	
	public String getUseageTemplate(List<MCommand> commandChain, boolean addDesc)
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
		
		if (addDesc)
		{
			ret.append(' ');
			ret.append(getPlugin().txt.getDesign().getColorInfo());
			ret.append(this.getDesc());
		}
		
		return ret.toString();
	}
	
	public String getUseageTemplate(boolean addDesc)
	{
		return getUseageTemplate(this.commandChain, addDesc);
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
			this.msg(handler.getError());
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
