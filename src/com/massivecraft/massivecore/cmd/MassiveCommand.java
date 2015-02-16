package com.massivecraft.massivecore.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ArgReader;
import com.massivecraft.massivecore.cmd.req.Req;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.PermUtil;
import com.massivecraft.massivecore.util.Txt;

public class MassiveCommand
{
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	// MassiveCore commands are a bit special when it comes to registration.
	//
	// I want my users to be able to edit the command aliases and I want
	// them to be able to do so during server runtime without having to use the /reload command.
	// 
	// To provide a truly neat experience I place the command aliases in a mstore database configuration file.
	// As such these config files are polled for changes and loaded into the server automatically.
	// If someone changed the command aliases we must update all Bukkit command registrations.
	// 
	// In order to achieve this we run a task once a second (see com.massivecraft.massivecore.MassiveCoreEngineCommandRegistration).
	// This task unregisters /all/ registered MCommands and then register them all again.
	// When registering again we use the fresh and current aliases.
	
	private static transient Map<MassiveCommand, Plugin> registry = new LinkedHashMap<MassiveCommand, Plugin>();
	
	public static Set<MassiveCommand> getRegisteredCommands()
	{
		return registry.keySet();
	}
	
	public static Map<MassiveCommand, Plugin> getRegistry()
	{
		return registry;
	}
	
	public static void unregister(Plugin plugin)
	{
		Iterator<Entry<MassiveCommand, Plugin>> iter = registry.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<MassiveCommand, Plugin> entry = iter.next();
			if (plugin.equals(entry.getValue()))
			{
				iter.remove();
			}
		}
	}
	
	@Deprecated
	public void register()
	{
		this.register(MassiveCore.get());
	}
	
	public Plugin register(Plugin plugin)
	{
		return registry.put(this, plugin);
	}
	
	public void unregister()
	{
		registry.remove(this);
	}
	
	public boolean isRegistered()
	{
		return registry.containsKey(this);
	}
	
	public Plugin getRegisteredPlugin()
	{
		return registry.get(this);
	}
	
	// -------------------------------------------- //
	// COMMAND BEHAVIOR
	// -------------------------------------------- //
	
	// FIELD: subCommands
	// The sub-commands to this command
	protected List<MassiveCommand> subCommands;
	public List<MassiveCommand> getSubCommands() { return this.subCommands; }
	public void setSubCommands(List<MassiveCommand> subCommands) { this.subCommands = subCommands; }
	
	public MassiveCommand getSubCommand(String alias)
	{
		for (MassiveCommand subCommand: this.getSubCommands())
		{
			for (String subAlias : subCommand.getAliases())
			{
				if (alias.equalsIgnoreCase(subAlias))
				{
					return subCommand;
				}
			}
		}
		return null;
	}
	
	public void addSubCommand(MassiveCommand subCommand)
	{
		this.addSubCommand(subCommand, this.subCommands.size());
	}
	
	public void addSubCommand(MassiveCommand subCommand, int index)
	{
		subCommand.commandChain.addAll(this.commandChain);
		subCommand.commandChain.add(this);
		this.subCommands.add(index, subCommand);
	}
	
	public void addSubCommandAfter(MassiveCommand subCommand, MassiveCommand after)
	{
		int index = this.subCommands.indexOf(after);
		if (index == -1)
		{
			index = this.subCommands.size();
		}
		else
		{
			index++;
		}
		this.addSubCommand(subCommand, index);
	}
	
	public int removeSubCommand(MassiveCommand subCommand)
	{
		int index = this.subCommands.indexOf(subCommand);
		this.subCommands.remove(index);
		return index;
	}
	
	public int replaceSubCommand(MassiveCommand subCommand, MassiveCommand replaced)
	{
		int index = this.removeSubCommand(replaced);
		if (index < 0) return index;
		this.addSubCommand(subCommand, index);
		return index;
	}
	
	// FIELD: aliases
	// The different names this commands will react to  
	protected List<String> aliases;
	public List<String> getAliases() { return this.aliases; }
	public void setAliases(List<String> aliases) { this.aliases = aliases; }
	
	public void addAliases(String... aliases) { this.aliases.addAll(Arrays.asList(aliases)); }
	public void addAliases(List<String> aliases) { this.aliases.addAll(aliases); }
	
	// FIELD: requiredArgs
	// These args must always be sent
	protected List<String> requiredArgs;
	public List<String> getRequiredArgs() { return this.requiredArgs; }
	public void setRequiredArgs(List<String> requiredArgs) { this.requiredArgs = requiredArgs; }
	
	public void addRequiredArg(String arg) { this.requiredArgs.add(arg); }
	
	// FIELD: optionalArgs
	// These args are optional
	protected Map<String, String> optionalArgs;
	public Map<String, String> getOptionalArgs() { return this.optionalArgs; }
	public void setOptionalArgs(Map<String, String> optionalArgs) { this.optionalArgs = optionalArgs; }
	
	public void addOptionalArg(String arg, String def) { this.optionalArgs.put(arg, def); }
	
	// FIELD: errorOnToManyArgs
	// Should an error be thrown if "to many" args are sent.
	protected boolean errorOnToManyArgs;
	public boolean getErrorOnToManyArgs() { return this.errorOnToManyArgs; }
	public void setErrorOnToManyArgs(boolean val) { this.errorOnToManyArgs = val; }
	
	// FIELD: usingTokenizer
	// Should the arguments be parsed considering quotes and backslashes and such?
	protected boolean usingTokenizer;
	public boolean isUsingTokenizer() { return this.usingTokenizer; }
	public void setUsingTokenizer(boolean usingTokenizer) { this.usingTokenizer = usingTokenizer; }
	
	// FIELD: usingSmartQuotesRemoval
	// Are "smart" quotes replaced with normal characters?
	protected boolean usingSmartQuotesRemoval;
	public boolean isUsingSmartQuotesRemoval() { return this.usingSmartQuotesRemoval; }
	public void setUsingSmartQuotesRemoval(boolean usingSmartQuotesRemoval) { this.usingSmartQuotesRemoval = usingSmartQuotesRemoval; }
	
	// FIELD: requirements
	// All these requirements must be met for the command to be executable;
	protected List<Req> requirements;
	public List<Req> getRequirements() { return this.requirements; }
	public void getRequirements(List<Req> requirements) { this.requirements = requirements; }
	
	public void addRequirements(Req... requirements) { this.requirements.addAll(Arrays.asList(requirements)); }
	
	// FIELD: desc
	// This field may be left blank and will in such case be loaded from the permissions node instead.
	// Thus make sure the permissions node description is an action description like "eat hamburgers" or "do admin stuff".
	protected String desc = null;
	public void setDesc(String desc) { this.desc = desc; }
	
	public String getDesc()
	{
		if (this.desc != null) return this.desc;
		
		String perm = this.getDescPermission();
		if (perm != null)
		{
			String pdesc = PermUtil.getDescription(this.getDescPermission());
			if (pdesc != null)
			{
				return pdesc;
			}
		}
			
		return "*info unavailable*";
	}
	
	// FIELD: descPermission
	// This permission node IS NOT TESTED AT ALL. It is rather used in the method above.
	protected String descPermission;
	public void setDescPermission(String descPermission) { this.descPermission = descPermission; }
	
	public String getDescPermission()
	{
		if (this.descPermission != null) return this.descPermission;
		// Otherwise we try to find one.
		for (Req req : this.getRequirements())
		{
			if ( ! (req instanceof ReqHasPerm)) continue;
			return ((ReqHasPerm)req).getPerm();
		}
		return null;
	}
	
	// FIELD: help
	// This is a multi-line help text for the command.
	protected List<String> help = new ArrayList<String>();
	public void setHelp(List<String> val) { this.help = val; }
	public void setHelp(String... val) { this.help = Arrays.asList(val); }
	public List<String> getHelp() { return this.help; }
	
	// FIELD: visibilityMode
	protected VisibilityMode visibilityMode;
	public VisibilityMode getVisibilityMode() { return this.visibilityMode; }
	public void setVisibilityMode(VisibilityMode visibilityMode) { this.visibilityMode = visibilityMode; }
	
	// -------------------------------------------- //
	// EXECUTION INFO
	// -------------------------------------------- //
	
	// FIELD: args
	// Will contain the arguments, or and empty list if there are none.
	protected List<String> args;
	public List<String> getArgs() { return this.args; }
	public void setArgs(List<String> args) { this.args = args; }

	// FIELD: commandChain
	// The command chain used to execute this command
	protected List<MassiveCommand> commandChain = new ArrayList<MassiveCommand>();
	public List<MassiveCommand> getCommandChain() { return this.commandChain; }
	public void setCommandChain(List<MassiveCommand> commandChain) { this.commandChain = commandChain; }
	
	// FIELDS: sender, me, senderIsConsole
	public CommandSender sender;
	public Player me;
	public boolean senderIsConsole;
	
	// -------------------------------------------- //
	// CONSTRUCTORS AND EXECUTOR
	// -------------------------------------------- //
	
	public MassiveCommand()
	{
		this.subCommands = new ArrayList<MassiveCommand>();
		
		this.aliases = new ArrayList<String>();
		
		this.requiredArgs = new ArrayList<String>();
		this.optionalArgs = new LinkedHashMap<String, String>();
		
		this.requirements = new ArrayList<Req>();
		
		this.errorOnToManyArgs = true;
		this.usingTokenizer = true;
		this.usingSmartQuotesRemoval = true;
		
		this.desc = null;
		this.descPermission = null;
		
		this.visibilityMode = VisibilityMode.VISIBLE; 
	}
	
	// The commandChain is a list of the parent command chain used to get to this command.
	public void execute(CommandSender sender, List<String> args, List<MassiveCommand> commandChain)
	{
		this.setArgs(args);
		this.setCommandChain(commandChain);

		// Is there a matching sub command?
		if (args.size() > 0)
		{
			MassiveCommand subCommand = this.getSubCommand(args.get(0));
			if (subCommand != null)
			{
				args.remove(0);
				commandChain.add(this);
				subCommand.execute(sender, args, commandChain);
				return;
			}
		}
		
		try
		{
			// Set Sender Variables
			this.sender = sender;
			this.senderIsConsole = true;
			this.me = null;
			if (sender instanceof Player)
			{
				this.me = (Player) sender;
				this.senderIsConsole = false;
			}
			this.fixSenderVars();
			
			if (isValidCall(this.sender, this.getArgs()))
			{
				perform();
			}
		}
		catch (MassiveException ex)
		{
			// Sometimes ArgReaders (or commands themself) throw exceptions, to stop executing and notify the user.
			if (ex.hasMessages())
			{
				Mixin.messageOne(sender, ex.getMessages());
			}
		}
		finally
		{
			// Unset Sender Variables
			this.sender = null;
			this.me = null;
			this.unsetSenderVars();
		}
	}
	
	public void fixSenderVars() 
	{
		
	}
	public void unsetSenderVars()
	{
		
	}
	
	public void execute(CommandSender sender, List<String> args)
	{
		execute(sender, args, new ArrayList<MassiveCommand>());
	}
	
	// This is where the command action is performed.
	public void perform() throws MassiveException
	{
		// Per default we just act as the help command!
		List<MassiveCommand> commandChain = new ArrayList<MassiveCommand>(this.getCommandChain());
		commandChain.add(this);
		
		HelpCommand.get().execute(this.sender, this.getArgs(), commandChain);
	}
	
	// -------------------------------------------- //
	// CALL VALIDATION
	// -------------------------------------------- //
	
	/**
	 * In this method we validate that all prerequisites to perform this command has been met.
	 */
	public boolean isValidCall(CommandSender sender, List<String> args)
	{
		if ( ! this.isRequirementsMet(sender, true))
		{
			return false;
		}
		
		if ( ! this.isArgsValid(args, sender))
		{
			return false;
		}
		
		return true;
	}
	
	public boolean isVisibleTo(CommandSender sender)
	{
		if (this.getVisibilityMode() == VisibilityMode.VISIBLE) return true;
		if (this.getVisibilityMode() == VisibilityMode.INVISIBLE) return false;
		return this.isRequirementsMet(sender, false);
	}
	
	public boolean isRequirementsMet(CommandSender sender, boolean informSenderIfNot)
	{
		for (Req req : this.getRequirements())
		{
			if ( ! req.apply(sender, this))
			{
				if (informSenderIfNot)
				{
					this.msg(req.createErrorMessage(sender, this));
				}
				return false;
			}
		}
		return true;
	}
	
	public boolean isArgsValid(List<String> args, CommandSender sender)
	{
		if (args.size() < this.getRequiredArgs().size())
		{
			if (sender != null)
			{
				msg(Lang.COMMAND_TO_FEW_ARGS);
				sender.sendMessage(this.getUseageTemplate());
			}
			return false;
		}
		
		if (args.size() > this.getRequiredArgs().size() + this.getOptionalArgs().size() && this.getErrorOnToManyArgs())
		{
			if (sender != null)
			{
				// Get the to many string slice
				List<String> theToMany = args.subList(this.getRequiredArgs().size() + this.optionalArgs.size(), args.size());
				msg(Lang.COMMAND_TO_MANY_ARGS, Txt.implodeCommaAndDot(theToMany, Txt.parse("<aqua>%s"), Txt.parse("<b>, "), Txt.parse("<b> and "), ""));
				msg(Lang.COMMAND_TO_MANY_ARGS2);
				sender.sendMessage(this.getUseageTemplate());
			}
			return false;
		}
		return true;
	}
	public boolean isArgsValid(List<String> args)
	{
		return this.isArgsValid(args, null);
	}
	
	// -------------------------------------------- //
	// HELP AND USAGE INFORMATION
	// -------------------------------------------- //
	
	public String getUseageTemplate(List<MassiveCommand> commandChain, boolean addDesc, boolean onlyFirstAlias, CommandSender sender)
	{
		StringBuilder ret = new StringBuilder();
		
		List<MassiveCommand> commands = new ArrayList<MassiveCommand>(commandChain);
		commands.add(this);
		
		String commandGoodColor = Txt.parse("<c>");
		String commandBadColor = Txt.parse("<bad>");
		
		ret.append(commandGoodColor);
		ret.append('/');
		
		boolean first = true;
		Iterator<MassiveCommand> iter = commands.iterator();
		while(iter.hasNext())
		{
			MassiveCommand mc = iter.next();
			if (sender != null && !mc.isRequirementsMet(sender, false))
			{
				ret.append(commandBadColor);
			}
			else
			{
				ret.append(commandGoodColor);
			}
			
			if (first && onlyFirstAlias)
			{
				ret.append(mc.getAliases().get(0));
			}
			else
			{
				ret.append(Txt.implode(mc.getAliases(), ","));
			}
			
			if (iter.hasNext())
			{
				ret.append(' ');
			}
			
			first = false;
		}
		
		List<String> args = new ArrayList<String>();
		
		for (String requiredArg : this.getRequiredArgs())
		{
			args.add("<"+requiredArg+">");
		}
		
		for (Entry<String, String> optionalArg : this.getOptionalArgs().entrySet())
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
			ret.append(Txt.parse("<p>"));
			ret.append(' ');
			ret.append(Txt.implode(args, " "));
		}
		
		if (addDesc)
		{
			ret.append(' ');
			ret.append(Txt.parse("<i>"));
			ret.append(this.getDesc());
		}
		
		return ret.toString();
	}
	
	public String getUseageTemplate(List<MassiveCommand> commandChain, boolean addDesc, boolean onlyFirstAlias)
	{
		return getUseageTemplate(commandChain, addDesc, onlyFirstAlias, null);
	}
	
	public String getUseageTemplate(List<MassiveCommand> commandChain, boolean addDesc)
	{
		return getUseageTemplate(commandChain, addDesc, false);
	}
	
	public String getUseageTemplate(boolean addDesc)
	{
		return getUseageTemplate(this.getCommandChain(), addDesc);
	}
	
	public String getUseageTemplate()
	{
		return getUseageTemplate(false);
	}
	
	// -------------------------------------------- //
	// MESSAGE SENDING HELPERS
	// -------------------------------------------- //
	
	// CONVENIENCE SEND MESSAGE
	
	public boolean sendMessage(String message)
	{
		return Mixin.messageOne(this.sender, message);
	}
	
	public boolean sendMessage(String... messages)
	{
		return Mixin.messageOne(this.sender, messages);
	}
	
	public boolean sendMessage(Collection<String> messages)
	{
		return Mixin.messageOne(this.sender, messages);
	}
	
	// CONVENIENCE MSG
	
	public boolean msg(String msg)
	{
		return Mixin.msgOne(this.sender, msg);
	}
	
	public boolean msg(String msg, Object... args)
	{
		return Mixin.msgOne(this.sender, msg, args);
	}
	
	public boolean msg(Collection<String> msgs)
	{
		return Mixin.msgOne(this.sender, msgs);
	}
	
	// -------------------------------------------- //
	// ARGUMENT READERS
	// -------------------------------------------- //
	
	// argIsSet
	
	public boolean argIsSet(int idx)
	{
		return ! (this.args.size() < idx+1);
	}
	
	// arg
	
	public String arg(int idx)
	{
		if ( ! this.argIsSet(idx)) return null;
		return this.getArgs().get(idx);
	}
	
	public <T> T arg(int idx, ArgReader<T> argReader) throws MassiveException
	{
		String str = this.arg(idx);
		return this.arg(str, argReader);
	}
	
	public <T> T arg(int idx, ArgReader<T> argReader, T defaultNotSet) throws MassiveException
	{
		String str = this.arg(idx);
		return this.arg(str, argReader, defaultNotSet);
	}
	
	// argConcatFrom
	
	public String argConcatFrom(int idx)
	{
		if ( ! this.argIsSet(idx)) return null;
		int from = idx;
		int to = this.getArgs().size();
		if (to <= from) return "";
		return Txt.implode(this.getArgs().subList(from, to), " ");
	}
	
	public <T> T argConcatFrom(int idx, ArgReader<T> argReader) throws MassiveException
	{
		String str = this.argConcatFrom(idx);
		return this.arg(str, argReader);
	}
	
	public <T> T argConcatFrom(int idx, ArgReader<T> argReader, T defaultNotSet) throws MassiveException
	{
		String str = this.argConcatFrom(idx);
		return this.arg(str, argReader, defaultNotSet);
	}
	
	// Core & Other
	
	public <T> T arg(ArgReader<T> argReader) throws MassiveException
	{
		return this.arg(null, argReader);
	}
	
	public <T> T arg(String str, ArgReader<T> argReader) throws MassiveException
	{
		T result = argReader.read(str, this.sender);
		return result;
	}
	
	public <T> T arg(String str, ArgReader<T> argReader, T defaultNotSet) throws MassiveException
	{
		if (str == null) return defaultNotSet;
		return this.arg(str, argReader);
	}
	
}
