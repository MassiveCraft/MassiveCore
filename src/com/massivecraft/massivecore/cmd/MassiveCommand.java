package com.massivecraft.massivecore.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.AR;
import com.massivecraft.massivecore.cmd.req.Req;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.mson.Mson;
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
	
	public boolean isParentCommand() { return this.getSubCommands().size() > 0; }
	
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
	
	public void setAliases(Collection<String> aliases) { this.aliases = new MassiveList<String>(aliases); }
	public void setAliases(String... aliases) { this.setAliases(Arrays.asList(aliases)); }
	
	public void addAliases(Collection<String> aliases) { this.aliases.addAll(aliases); }
	public void addAliases(String... aliases) { this.addAliases(Arrays.asList(aliases)); }
	
	// FIELD argSettings
	// Settings for all args.
	protected List<ArgSetting<?>> argSettings;
	public List<ArgSetting<?>> getArgSettings() { return this.argSettings; }
	public void setArgSettings(List<ArgSetting<?>> argSettings) { this.argSettings = argSettings; }
	
	// The index is the same as the argument index.
	// So argAt(x) should be read by getArgReader(x)
	
	public ArgSetting<?> getArgSetting(int index)
	{
		if (this.isUsingConcatFrom() && this.getConcatFromIndex() < index) index = this.getConcatFromIndex();
		return this.getArgSettings().get(index);
	}
	
	public AR<?> getArgReader(int index)
	{
		ArgSetting<?> setting = this.getArgSetting(index);
		return setting.getReader();
	}
	
	public boolean hasArgSettingForIndex(int index)
	{
		if (index < 0) return false;
		if (this.isUsingConcatFrom() && this.getConcatFromIndex() < index) index = this.getConcatFromIndex();
		if (this.getArgSettings().size() <= index) return false;
		return true;
	}
	
	// The actual setting.
	public <T> ArgSetting<T> addArg(ArgSetting<T> setting, boolean concatFromHere)
	{
		// Concat safety.
		if (this.isUsingConcatFrom())
		{
			throw new IllegalStateException("You can't add args if a prior one concatenates.");
		}
		
		// Req/optional safety.
		int prior = this.getArgSettings().size()-1;
		if (this.hasArgSettingForIndex(prior) && this.getArgSetting(prior).isOptional() && setting.isRequired())
		{
			throw new IllegalArgumentException("You can't add required args, if a prior one is optional.");
		}
		
		// If false no change is made.
		// If true change is made.
		this.setUsingConcatFrom(concatFromHere);
		
		this.getArgSettings().add(setting);
		return setting;
	}
	
	// The actual setting without concat.
	public <T> ArgSetting<T> addArg(ArgSetting<T> setting)
	{
		return this.addArg(setting, false);
	}
	
	// All
	public <T> ArgSetting<T> addArg(T defaultValue, AR<T> reader, boolean requiredFromConsole, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addArg(new ArgSetting<T>(defaultValue, reader, requiredFromConsole, name, defaultDesc), concatFromHere);
	}
	
	// WITHOUT 1
	
	// Without defaultValue
	public <T> ArgSetting<T> addArg(AR<T> reader, boolean requiredFromConsole, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addArg(new ArgSetting<T>(reader, requiredFromConsole, name, defaultDesc), concatFromHere);
	}
	
	// Without reqFromConsole.
	public <T> ArgSetting<T> addArg(T defaultValue, AR<T> reader, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addArg(new ArgSetting<T>(defaultValue, reader, name, defaultDesc),  concatFromHere);
	}

	// Without defaultDesc.
	public <T> ArgSetting<T> addArg(T defaultValue, AR<T> reader, boolean requiredFromConsole, String name, boolean concatFromHere)
	{
		return this.addArg(new ArgSetting<T>(defaultValue, reader, requiredFromConsole, name), concatFromHere);
	}
	
	// Without concat.
	public <T> ArgSetting<T> addArg(T defaultValue, AR<T> reader, boolean requiredFromConsole, String name, String defaultDesc)
	{
		return this.addArg(new ArgSetting<T>(defaultValue, reader, requiredFromConsole, name, defaultDesc), false);
	}
	
	// WITHOUT 2
	
	// Without defaultValue & reqFromConsole
	public <T> ArgSetting<T> addArg(AR<T> reader, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addArg(new ArgSetting<T>(reader, name, defaultDesc), concatFromHere);
	}
	
	// Without defaultValue & defaultDesc
	public <T> ArgSetting<T> addArg(AR<T> reader, boolean requiredFromConsole, String name, boolean concatFromHere)
	{
		return this.addArg(new ArgSetting<T>(reader, requiredFromConsole, name), concatFromHere);
	}
	
	// Without defaultValue & concat.
	public <T> ArgSetting<T> addArg(AR<T> reader, boolean requiredFromConsole, String name, String defaultDesc)
	{
		return this.addArg(new ArgSetting<T>(reader, requiredFromConsole, name, defaultDesc));
	}

	// Without reqFromConsole & defaultDesc.
	public <T> ArgSetting<T> addArg(T defaultValue, AR<T> reader, String name, boolean concatFromHere)
	{
		return this.addArg(new ArgSetting<T>(defaultValue, reader, name), concatFromHere);
	}

	// Without reqFromConsole & concat.
	public <T> ArgSetting<T> addArg(T defaultValue, AR<T> reader, String name, String defaultDesc)
	{
		return this.addArg(new ArgSetting<T>(defaultValue, reader, name, defaultDesc));
	}
	
	// Without defaultDesc & concat.
	public <T> ArgSetting<T> addArg(T defaultValue, AR<T> reader, boolean requiredFromConsole, String name)
	{
		return this.addArg(new ArgSetting<T>(defaultValue, reader, requiredFromConsole, name));
	}
	
	// WITHOUT 3
	
	// Without defaultValue, reqFromConsole & defaultDesc.
	public <T> ArgSetting<T> addArg(AR<T> reader, String name, boolean concatFromHere)
	{
		return this.addArg(new ArgSetting<T>(reader, name), concatFromHere);
	}
	
	// Without defaultValue, reqFromConsole & concat .
	public <T> ArgSetting<T> addArg(AR<T> reader, String name, String defaultDesc)
	{
		return this.addArg(new ArgSetting<T>(reader, name, defaultDesc));
	}
	
	// Without defaultValue, defaultDesc & concat .
	public <T> ArgSetting<T> addArg(AR<T> reader, boolean requiredFromConsole, String name)
	{
		return this.addArg(new ArgSetting<T>(reader, requiredFromConsole, name));
	}
	
	// Without reqFromConsole, defaultDesc & concat .
	public <T> ArgSetting<T> addArg(T defaultValue, AR<T> reader, String name)
	{
		return this.addArg(new ArgSetting<T>(defaultValue, reader, name));
	}
	
	// WITHOUT 4

	// Without defaultValue, reqFromConsole, defaultDesc & concat .
	public <T> ArgSetting<T> addArg(AR<T> reader, String name)
	{
		return this.addArg(new ArgSetting<T>(reader, name));
	}

	// FIELD: requiredArgs
	// These args must always be sent
	@Deprecated protected List<String> requiredArgs;
	@Deprecated public List<String> getRequiredArgs() { return this.requiredArgs; }
	@Deprecated public void setRequiredArgs(List<String> requiredArgs) { this.requiredArgs = requiredArgs; }
	
	@Deprecated public void addRequiredArg(String arg) { this.requiredArgs.add(arg); }
	
	// FIELD: optionalArgs
	// These args are optional
	@Deprecated protected Map<String, String> optionalArgs;
	@Deprecated public Map<String, String> getOptionalArgs() { return this.optionalArgs; }
	@Deprecated public void setOptionalArgs(Map<String, String> optionalArgs) { this.optionalArgs = optionalArgs; }
	
	@Deprecated public void addOptionalArg(String arg, String def) { this.optionalArgs.put(arg, def); }
	
	// FIELD: errorOnToManyArgs
	// Should an error be thrown if "too many" args are sent.
	protected boolean givingErrorOnTooManyArgs;
	public boolean isGivingErrorOnTooManyArgs() { return this.givingErrorOnTooManyArgs; }
	public void setGivingErrorOnTooManyArgs(boolean val) { this.givingErrorOnTooManyArgs = val; }
	@Deprecated public boolean getErrorOnToManyArgs() { return this.isGivingErrorOnTooManyArgs(); }
	@Deprecated public void setErrorOnToManyArgs(boolean val) { this.setGivingErrorOnTooManyArgs(val); }
	
	// FIELD concatFrom 
	// From which arg should the be concatenated.
	protected boolean usingConcatFrom;
	public boolean isUsingConcatFrom() { return this.usingConcatFrom; }
	public void setUsingConcatFrom(boolean usingConcatFrom) { this.usingConcatFrom = usingConcatFrom; }
	
	public int getConcatFromIndex() { return this.getArgSettings().size() -1; }
	
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
	
	// FIELD: usingArbitraryArgumentOrder
	// Can the order of the args which the player types, be arbitrary?
	protected boolean usingArbitraryArgumentOrder;
	public boolean isUsingArbitraryArgumentOrder() { return this.usingArbitraryArgumentOrder; }
	public void setUsingArbitraryArgumentOrder(boolean usingArbitraryArgumentOrder) { this.usingArbitraryArgumentOrder = usingArbitraryArgumentOrder; }
	
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
	
	public MassiveCommand getParentCommand()
	{
		List<MassiveCommand> commandChain = this.getCommandChain();
		if (commandChain == null) return null;
		if (commandChain.isEmpty()) return null;
		return commandChain.get(commandChain.size()-1);
	}
	
	public boolean hasParentCommand() { return this.getParentCommand() != null; }
	
	// FIELD: nextArg
	// The index of the next arg to read.
	public int nextArg;
	
	// FIELDS: sender, me, senderIsConsole
	public CommandSender sender;
	public Player me;
	public boolean senderIsConsole;
	
	// -------------------------------------------- //
	// BACKWARDS COMPAT
	// -------------------------------------------- //
	
	public boolean isUsingNewArgSystem()
	{
		return ! this.getArgSettings().isEmpty();
	}
	
	public int getRequiredArgsAmountFor(CommandSender sender)
	{
		if ( ! this.isUsingNewArgSystem()) return this.getRequiredArgs().size();
		
		int ret = 0;
		for (ArgSetting<?> setting : this.getArgSettings())
		{
			if (setting.isRequiredFor(sender)) ret++;
		}
		
		return ret;
	}
	
	public int getOptionalArgsAmountFor(CommandSender sender)
	{
		if ( ! this.isUsingNewArgSystem()) return this.getOptionalArgs().size();
		
		int ret = 0;
		for (ArgSetting<?> setting : this.getArgSettings())
		{
			if (setting.isOptionalFor(sender)) ret++;
		}
		
		return ret;
	}
	
	public int getAllArgsAmountFor(CommandSender sender)
	{
		return this.getOptionalArgsAmountFor(sender) + this.getRequiredArgsAmountFor(sender);
	}
	
	// -------------------------------------------- //
	// CONSTRUCTORS AND EXECUTOR
	// -------------------------------------------- //
	
	public MassiveCommand()
	{
		this.subCommands = new ArrayList<MassiveCommand>();
		
		this.aliases = new ArrayList<String>();
		
		this.argSettings = new ArrayList<ArgSetting<?>>();
		
		this.requiredArgs = new ArrayList<String>();
		this.optionalArgs = new LinkedHashMap<String, String>();
		
		this.requirements = new ArrayList<Req>();
		
		this.givingErrorOnTooManyArgs = true;
		this.usingConcatFrom = false;
		
		this.usingTokenizer = true;
		this.usingSmartQuotesRemoval = true;
		this.usingArbitraryArgumentOrder = true;
		
		this.desc = null;
		this.descPermission = null;
		
		this.visibilityMode = VisibilityMode.VISIBLE; 
	}
	
	// The commandChain is a list of the parent command chain used to get to this command.
	public void execute(CommandSender sender, List<String> args, List<MassiveCommand> commandChain)
	{
		args = this.applyConcatFrom(args);
		
		if (this.isUsingArbitraryArgumentOrder())
		{
			args = this.fixArgOrder(args, sender);
		}

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
			this.nextArg = 0;
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
			this.nextArg = 0;
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
	
	public List<String> applyConcatFrom(List<String> args)
	{
		if ( ! this.isUsingConcatFrom()) return args;
		
		List<String> ret = new MassiveList<String>();
		final int maxIdx = Math.min(this.getConcatFromIndex(), args.size());
		ret.addAll(args.subList(0, maxIdx)); // The args that should not be concatenated.
		
		if (args.size() > maxIdx)
		{
			ret.add(Txt.implode(args.subList(maxIdx, args.size()), " "));
		}
		
		return ret;
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
	// FIX ARG ORDER
	// -------------------------------------------- //

	public List<String> fixArgOrder(List<String> args, CommandSender sender)
	{
		// So if there is too many, or too few args. We can't do much here.
		if ( ! this.isArgsValid(args)) return args;
		// We can't do anything with the old arg system.
		if ( ! this.isUsingNewArgSystem()) return args;
		
		String[] ret = new String[this.getArgSettings().size()];
		
		args:
		for (String arg : args)
		{
			settings:
			for (int i = 0; i < this.getArgSettings().size(); i++)
			{
				AR<?> reader = this.getArgReader(i);
				
				if (ret[i] != null) continue settings; // If that index is already filled.
				
				// We do in fact want to allow null args.
				// Those are used by us in some special circumstances.
				if (arg != null && ! reader.isValid(arg, sender)) continue settings; // If this arg isn't valid for that index.
				
				ret[i] = arg;
				continue args; // That arg is now set :)
			}
			// We will only end up here if an arg didn't fit any of the arg readers.
			// In that case we failed.
			return args;
		}
		
		// Ensure that the required args are filled.
		for (int i = 0; i < this.getRequiredArgsAmountFor(sender); i++)
		{
			if (ret[i] != null) continue;
			// We end up here if an required arg wasn't filled. In that case we failed.
			return args;
		}
		
		return Arrays.asList(ret);
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
			if (req.apply(sender, this)) continue;
			
			if (informSenderIfNot)
			{
				Mixin.messageOne(sender, req.createErrorMessage(sender, this));
			}
			return false;
		}
		return true;
	}
	
	public boolean isArgsValid(List<String> args, CommandSender sender)
	{
		if (args.size() < this.getRequiredArgsAmountFor(sender))
		{
			if (sender != null)
			{
				Mixin.msgOne(sender, Lang.COMMAND_TOO_FEW_ARGS);
				Mixin.messageOne(sender, this.getUseageTemplate());
			}
			return false;
		}
		
		// We don't need to take argConcatFrom into account. Because at this point the args 
		// are already concatenated and thus cannot be too many.
		if (args.size() > this.getAllArgsAmountFor(sender) && this.isGivingErrorOnTooManyArgs())
		{
			if (sender != null)
			{
				if (this.isParentCommand())
				{
					String arg = args.get(0);
					
					// Try Levenshtein
					List<String> matches = this.getSimilarSubcommandAliases(arg, this.getMaxLevenshteinDistanceForArg(arg));
					
					Mixin.msgOne(sender, Lang.COMMAND_NO_SUCH_SUB, this.getUseageTemplate() + " " + arg);
					if ( ! matches.isEmpty())
					{
						String suggest = Txt.parse(Txt.implodeCommaAnd(matches, "<i>, <c>", " <i>or <c>"));
						Mixin.msgOne(sender, Lang.COMMAND_SUGGEST_SUB, this.getUseageTemplate() + " " + suggest);
					}
					else
					{
						Mixin.msgOne(sender, Lang.COMMAND_GET_HELP, this.getUseageTemplate());
					}
					
				}
				else
				{
					// Get the too many string slice
					List<String> theTooMany = args.subList(this.getAllArgsAmountFor(sender), args.size());
					Mixin.msgOne(sender, Lang.COMMAND_TOO_MANY_ARGS, Txt.implodeCommaAndDot(theTooMany, Txt.parse("<aqua>%s"), Txt.parse("<b>, "), Txt.parse("<b> and "), ""));
					Mixin.msgOne(sender, Lang.COMMAND_TOO_MANY_ARGS2);
					Mixin.messageOne(sender, this.getUseageTemplate());
				}
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
	// MATCHING SUGGESTIONS
	// -------------------------------------------- //
	
	public List<String> getSimilarAliases(String arg, int maxLevenshteinDistance)
	{
		if (arg == null) return Collections.emptyList();
		arg = arg.toLowerCase();
		
		List<String> matches = new ArrayList<String>();
		
		for (String alias : this.getAliases())
		{
			if (alias == null) continue;
			String aliaslc = alias.toLowerCase();
			int distance = StringUtils.getLevenshteinDistance(arg, aliaslc);
			if (distance > maxLevenshteinDistance) continue;
			matches.add(alias);
		}
		return matches;
	}
	
	public List<String> getSimilarSubcommandAliases(String arg, int maxLevenshteinDistance)
	{
		if (arg == null) return Collections.emptyList();
		
		// Try Levenshtein
		List<String> matches = new ArrayList<String>();
		
		for (MassiveCommand sub : this.getSubCommands())
		{
			matches.addAll(sub.getSimilarAliases(arg, maxLevenshteinDistance));
		}
		return matches;
	}
	
	public int getMaxLevenshteinDistanceForArg(String arg)
	{
		if (arg == null) return 0;
		if (arg.length() <= 1) return 0; // When dealing with 1 character aliases, there is way too many options. So we don't suggest.
		if (arg.length() <= 4) return 1; // When dealing with low length aliases, there too many options. So we won't suggest much
		if (arg.length() <= 7) return 2; // 2 is default.
		
		return 3; // If it were 8 characters or more, we end up here. Because many characters allow for more typos.
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
		
		List<String> args = this.getArgUseagesFor(sender);
		
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
	
	protected List<String> getArgUseagesFor(CommandSender sender)
	{
		List<String> ret = new MassiveList<String>();
		if (this.isUsingNewArgSystem())
		{
			for (ArgSetting<?> setting : this.getArgSettings())
			{
				ret.add(setting.getUseageTemplateDisplayFor(sender));
			}
		}
		else
		{
			for (String requiredArg : this.getRequiredArgs())
			{
				ret.add("<" + requiredArg + ">");
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
					val = "=" + val;
				}
				ret.add("[" + optionalArg.getKey() + val + "]");
			}
		}
		
		return ret;
	}
	
	public String getUseageTemplate(List<MassiveCommand> commandChain, boolean addDesc, boolean onlyFirstAlias)
	{
		return getUseageTemplate(commandChain, addDesc, onlyFirstAlias, sender);
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
	
	public String getCommandLine(String... args)
	{
		return getCommandLine(Arrays.asList(args));
	}
	
	public String getCommandLine(Iterable<String> args)
	{
		// Initiate ret
		StringBuilder ret = new StringBuilder();
		
		// First a slash
		ret.append('/');
		
		// Then parent commands
		for (MassiveCommand parent : this.getCommandChain())
		{
			// Append parent
			ret.append(parent.getAliases().get(0));
			
			// Append space
			ret.append(' ');
		}
		// Then ourself
		ret.append(this.getAliases().get(0));
		
		// Then args
		for (String arg : args)
		{
			// First a space
			ret.append(' ');
			
			// Then the arg
			ret.append(arg);
		}
		
		// Return ret
		return ret.toString();
	}

 	// -------------------------------------------- //
	// TAB
	// -------------------------------------------- //
	
	public List<String> getTabCompletions(List<String> args, CommandSender sender)
	{
		if (args == null) throw new IllegalArgumentException("args was mull");
		if (sender == null) throw new IllegalArgumentException("sender was null");
		if (args.isEmpty()) throw new IllegalArgumentException("args was empty");
		
		if (this.isParentCommand())
		{
			return this.getTabCompletionsSub(args, sender);
		}
		else if ( ! this.isUsingNewArgSystem())
		{
			return Collections.emptyList();
		}
		else
		{
			return this.getTabCompletionsArg(args, sender);
		}
	}
	
	protected List<String> getTabCompletionsSub(List<String> args, CommandSender sender)
	{
		// If this isn't the last argument...
		if (args.size() != 1)
		{
			// ...we will ask the subcommand for tab completions.
			MassiveCommand cmd = this.getSubCommand(args.get(0));
			if (cmd == null) return Collections.emptyList();
			args.remove(0);
			return cmd.getTabCompletions(args, sender);
		}

		// ...else check the subcommands.
		List<String> ret = new ArrayList<String>();
		String token = args.get(args.size()-1).toLowerCase();
		for (MassiveCommand sub : this.getSubCommands())
		{
			if ( ! this.shouldSenderTabCompleteSub(sender, sub)) continue;
			ret.addAll(Txt.getStartsWithIgnoreCase(sub.getAliases(), token));
		}
		
		return addSpaceAtEnd(ret);
	}
	
	protected boolean shouldSenderTabCompleteSub(CommandSender sender, MassiveCommand sub)
	{
		if ( ! sub.isVisibleTo(sender)) return false;
		if ( ! sub.isRequirementsMet(sender, false)) return false;
		return true;
	}
	
	protected List<String> getTabCompletionsArg(List<String> args, CommandSender sender)
	{
		args = this.applyConcatFrom(args);
		
		int index = args.size() - 1;
		if ( ! this.hasArgSettingForIndex(index)) return Collections.emptyList();
		AR<?> reader = this.getArgReader(index);
		
		List<String> ret = reader.getTabListFiltered(sender, args.get(index));
		
		// If the reader allows space after tab and this is not the last possible argument...
		if (reader.allowSpaceAfterTab() && this.hasArgSettingForIndex(args.size()))
		{
			// ...we will sometimes add a space at the end. Depending on list size.
			ret = addSpaceAtEnd(ret);
		}
		
		return ret;
	}
	
	protected static List<String> addSpaceAtEnd(List<String> suggestions)
	{
		if (suggestions.size() != 1) return suggestions;

		// Get the suggestion.
		String suggestion = suggestions.get(0);
		// Add the space at the end.
		suggestion += ' ';
		return Collections.singletonList(suggestion);
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
	
	// CONVENIENCE RAW
	
	public boolean sendRaw(Mson mson)
	{
		return Mixin.messageRawOne(this.sender, mson);
	}
	
	public boolean sendRaw(Mson... mson)
	{
		return Mixin.messageRawOne(this.sender, mson);
	}
	
	public boolean sendRaw(Collection<Mson> mson)
	{
		return Mixin.messageRawOne(this.sender, mson);
	}
	
	// CONVENIENCE MSON
	
	public Mson mson()
	{
		return Mson.mson();
	}
	
	public Mson mson(Object... parts)
	{
		return Mson.mson(parts);
	}
	
	public List<Mson> msons(Object... parts)
	{
		return Mson.msons(parts);
	}
	
	public List<Mson> msons(Collection<?> parts)
	{
		return Mson.msons(parts);
	}
	
	// -------------------------------------------- //
	// ARGUMENT READERS
	// -------------------------------------------- //
	
	// Util
	
	public boolean argIsSet(int idx)
	{
		if (idx < 0) return false;
		if (idx+1 > this.getArgs().size()) return false;
		if (this.getArgs().get(idx) == null) return false;
		return true;
	}
	
	public boolean argIsSet()
	{
		return this.argIsSet(nextArg);
	}
	
	// Implicit index
	
	public String arg()
	{
		return this.argAt(nextArg);
	}
	
	public <T> T readArg() throws MassiveException
	{
		return this.readArgAt(nextArg);
	}

	public <T> T readArg(T defaultNotSet) throws MassiveException
	{
		return this.readArgAt(nextArg, defaultNotSet);
	}

	// Index logic
	
	public String argAt(int idx)
	{
		nextArg = idx + 1;
		if ( ! this.argIsSet(idx)) return null;
		return this.getArgs().get(idx);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T readArgAt(int idx) throws MassiveException
	{
		// Make sure that an ArgSetting is present.
		if ( ! this.hasArgSettingForIndex(idx)) throw new IllegalArgumentException(idx + " is out of range. ArgSettings size: " + this.getArgSettings().size());

		// Increment
		nextArg = idx + 1;

		// Get the setting
		ArgSetting<T> setting = (ArgSetting<T>) this.getArgSetting(idx);
		// Return the default in the setting.
		if ( ! this.argIsSet(idx) && setting.isDefaultValueSet()) return setting.getDefaultValue();
		
		// The was no arg, or default value in the setting.
		if ( ! this.argIsSet(idx)) throw new IllegalArgumentException("Trying to access arg: " + idx + " but that is not set.");
		
		// Just read the arg normally.
		String arg = this.getArgs().get(idx);
		return setting.getReader().read(arg, sender);
	}

	public <T> T readArgAt(int idx, T defaultNotSet) throws MassiveException
	{
		// Return the default passed.
		if ( ! this.argIsSet(idx))
		{
			// Increment
			nextArg = idx + 1;

			// Use default
			return defaultNotSet;
		}

		// Increment is done in this method
		return readArgAt(idx);
	}

	// We don't even need this anymore

	@Deprecated
	public <T> T readArgFrom(AR<T> argReader) throws MassiveException
	{
		return this.readArgFrom(null, argReader);
	}

	@Deprecated
	public <T> T readArgFrom(String str, AR<T> argReader) throws MassiveException
	{
		if (argReader == null) throw new IllegalArgumentException("argReader is null");
		return argReader.read(str, this.sender);
	}

	@Deprecated
	public <T> T readArgFrom(String str, AR<T> argReader, T defaultNotSet) throws MassiveException
	{
		if (str == null) return defaultNotSet;
		return this.readArgFrom(str, argReader);
	}
	
	// -------------------------------------------- //
	// OLD ARGUMENT READERS
	// -------------------------------------------- //

	// arg
	
	@Deprecated
	public String arg(int idx)
	{
		return this.argAt(idx);
	}
	
	@Deprecated
	public <T> T arg(int idx, AR<T> argReader) throws MassiveException
	{
		String str = this.arg(idx);
		return this.arg(str, argReader);
	}
	
	@Deprecated
	public <T> T arg(int idx, AR<T> argReader, T defaultNotSet) throws MassiveException
	{
		String str = this.arg(idx);
		return this.arg(str, argReader, defaultNotSet);
	}
	
	// argConcatFrom
	
	@Deprecated
	public String argConcatFrom(int idx)
	{
		if ( ! this.argIsSet(idx)) return null;
		int from = idx;
		int to = this.getArgs().size();
		if (to <= from) return "";
		return Txt.implode(this.getArgs().subList(from, to), " ");
	}
	
	@Deprecated
	public <T> T argConcatFrom(int idx, AR<T> argReader) throws MassiveException
	{
		String str = this.argConcatFrom(idx);
		return this.arg(str, argReader);
	}
	
	@Deprecated
	public <T> T argConcatFrom(int idx, AR<T> argReader, T defaultNotSet) throws MassiveException
	{
		String str = this.argConcatFrom(idx);
		return this.arg(str, argReader, defaultNotSet);
	}
	
	// Core & Other
	
	@Deprecated
	public <T> T arg(AR<T> argReader) throws MassiveException
	{
		return this.arg(null, argReader);
	}
	
	@Deprecated
	public <T> T arg(String str, AR<T> argReader) throws MassiveException
	{
		return argReader.read(str, this.sender);
	}
	
	@Deprecated
	public <T> T arg(String str, AR<T> argReader, T defaultNotSet) throws MassiveException
	{
		if (str == null) return defaultNotSet;
		return this.arg(str, argReader);
	}
	
}
