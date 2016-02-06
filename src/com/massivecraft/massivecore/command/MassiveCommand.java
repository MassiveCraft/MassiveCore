package com.massivecraft.massivecore.command;

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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.command.requirement.Requirement;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.predicate.PredicateStartsWithIgnoreCase;
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
	
	// STATIC
	private static final transient Map<MassiveCommand, Plugin> registry = new LinkedHashMap<MassiveCommand, Plugin>();
	public static Map<MassiveCommand, Plugin> getRegistry() { return registry; }
	public static Set<MassiveCommand> getRegisteredCommands() { return registry.keySet(); }
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
	
	// INSTANCE
	public Plugin register(Plugin plugin) { return registry.put(this, plugin); }
	public void unregister() { registry.remove(this); }
	public boolean isRegistered() { return registry.containsKey(this); }
	public Plugin getRegisteredPlugin() { return registry.get(this); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	// Due to the large amount of methods in this class we place the fields alone here in the beginning.
	// Field access and other similar utility methods have their special sections below. 
	
	// === CHILDREN ===
	
	// The children. Previously called subcommands.
	protected List<MassiveCommand> children = new ArrayList<MassiveCommand>();
	
	// === ALIASES ===
	
	// The different names this commands will react to  
	protected List<String> aliases = new ArrayList<String>();
	
	// === PARAMETERS ===
	
	// The command parameters.
	protected List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
	
	// === PREPROCESS ===
	
	// Should the arguments be parsed considering quotes and backslashes and such?
	protected boolean tokenizing = true;
	
	// Are "smart" quotes replaced with normal characters?
	protected boolean unsmart = true;
	
	// === PUZZLER ===
	
	// Should an error be thrown if "too many" arguments are provided?
	protected boolean overflowSensitive = true;
	
	// Should the last parameter concatenate all surplus arguments?
	protected boolean concatenating = false;
	
	// Should we try to automatically swap the arguments around if they were typed in invalid order?
	protected boolean swapping = true;
	
	// === REQUIREMENTS ===
	
	// All these requirements must be met for the command to be executable;
	protected List<Requirement> requirements = new ArrayList<Requirement>();
	
	// === HELP ===
	
	// A short description of what the command does. Such as "eat hamburgers" or "do admin stuff".
	protected String desc = null;
	
	// A specific permission node to use for description if desc is null.
	protected String descPermission = null;
	
	// Free text displayed at the top of the help command.
	protected List<String> help = new ArrayList<String>();
	
	// The visibility of this command in help command.
	protected Visibility visibility = Visibility.VISIBLE; 
	
	// === EXECUTION ===
	
	// The raw string arguments passed upon execution. An empty list if there are none.
	protected List<String> args = new ArrayList<String>();
	
	// The chain of commands used to reach this command during execution.
	protected List<MassiveCommand> chain = new ArrayList<MassiveCommand>();
	
	// The index of the next arg to read.
	public int nextArg = 0;
	
	// ...
	public CommandSender sender = null;
	
	// ...
	public Player me = null;
	
	// ...
	public boolean senderIsConsole = false;	
	
	// -------------------------------------------- //
	// CHILDREN
	// -------------------------------------------- //
	
	public List<MassiveCommand> getChildren() { return this.children; }
	public void setChildren(List<MassiveCommand> children) { this.children = children; }
	
	public boolean isParent()
	{
		return this.getChildren().size() > 0;
	}
	
	public List<MassiveCommand> getVisibleChildren(CommandSender watcher)
	{
		List<MassiveCommand> ret = new MassiveList<>();
		for (MassiveCommand child : this.getChildren())
		{
			if (child.isVisibleTo(watcher)) ret.add(child);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// CHILDREN > GET
	// -------------------------------------------- //
		
	// The full version of the child matcher method.
	// Returns a map from alias to command.
	//
	// token - the full alias or an alias prefix.
	// levenshtein - should we use levenshtein instead of starts with?
	// prioritizeExact - return single entry map on full match.
	// 
	// An empty map means no child was found.
	// A single entry map means we found an unambiguous match.
	// A larger map means the token was ambiguous.
	public Map<String, MassiveCommand> getChildMatches(String token, boolean levenshtein, CommandSender onlyRelevantToSender)
	{
		// Create Ret
		Map<String, MassiveCommand> ret = new MassiveMap<String, MassiveCommand>();
		
		// Prepare
		token = token.toLowerCase();
		PredicateStartsWithIgnoreCase predicate = PredicateStartsWithIgnoreCase.get(token);
		
		// Fill Ret
		for (MassiveCommand child : this.getChildren())
		{
			for (String alias : child.getAliases())
			{
				// If this alias has not already been reserved ...
				if (ret.containsKey(alias)) continue;
				
				// ... consider exact priority ...
				if (alias.equalsIgnoreCase(token))
				{
					return new MassiveMap<String, MassiveCommand>(alias, child);
				}
				
				// ... matches ...
				if (levenshtein)
				{
					// ... is levenshteinish ...
					if ( ! this.isLevenshteinClose(token, alias)) continue;
				}
				else
				{
					// ... the alias startsWithIgnoreCase the token ...
					if ( ! predicate.apply(alias)) continue;
				}
				
				// ... and put in ret.
				ret.put(alias, child);
			}
		}
		
		// Only Relevant
		if (onlyRelevantToSender != null)
		{
			Iterator<Entry<String, MassiveCommand>> iter = ret.entrySet().iterator();
			while (iter.hasNext())
			{
				Entry<String, MassiveCommand> entry = iter.next();
				if (entry.getValue().isRelevant(onlyRelevantToSender)) continue;
				iter.remove();
			}
		}
		
		// Return Ret
		return ret;
	}
	
	// A simplified version returning null on ambiguity and nothing found.
	public MassiveCommand getChild(String token)
	{
		Map<String, MassiveCommand> childMatches = this.getChildMatches(token, false, null);
		
		if (childMatches.isEmpty()) return null;
		if (childMatches.size() > 1) return null;
		
		return childMatches.entrySet().iterator().next().getValue();
	}
	
	protected boolean isRelevant(CommandSender sender)
	{
		if (sender == null) return true;
		
		if ( ! this.isVisibleTo(sender)) return false;
		if ( ! this.isRequirementsMet(sender, false)) return false;
		
		return true;
	}
	
	public boolean isLevenshteinClose(String argument, String alias)
	{
		int levenshteinDistanceMax = this.getLevenshteinMax(argument);
		int distance = StringUtils.getLevenshteinDistance(argument, alias);
		return distance <= levenshteinDistanceMax;
	}
	
	public int getLevenshteinMax(String argument)
	{
		if (argument == null) return 0;
		if (argument.length() <= 1) return 0; // When dealing with 1 character aliases, there is way too many options. So we don't suggest.
		if (argument.length() <= 4) return 1; // When dealing with low length aliases, there too many options. So we won't suggest much
		if (argument.length() <= 7) return 2; // 2 is default.
		
		return 3; // If it were 8 characters or more, we end up here. Because many characters allow for more typos.
	}
	
	// -------------------------------------------- //
	// CHILDREN > ADD & REMOVE
	// -------------------------------------------- //
	
	public void addChild(MassiveCommand child)
	{
		this.addChild(child, this.children.size());
	}
	
	public void addChild(MassiveCommand child, int index)
	{
		if (this.children.isEmpty() && ! (child instanceof HelpCommand))
		{
			this.children.add(0, HelpCommand.get());
			index++;
		}
		child.addToChain(this);
		this.children.add(index, child);
	}
	
	public void addChildAfter(MassiveCommand child, MassiveCommand after)
	{
		int index = this.children.indexOf(after);
		if (index == -1)
		{
			index = this.children.size();
		}
		else
		{
			index++;
		}
		this.addChild(child, index);
	}
	
	public int removeChild(MassiveCommand child)
	{
		int index = this.children.indexOf(child);
		this.children.remove(index);
		return index;
	}
	
	public int replaceChild(MassiveCommand child, MassiveCommand replaced)
	{
		int index = this.removeChild(replaced);
		if (index < 0) return index;
		this.addChild(child, index);
		return index;
	}
	
	// -------------------------------------------- //
	// ALIASES
	// -------------------------------------------- //
	
	public List<String> getAliases() { return this.aliases; }
	
	public void setAliases(Collection<String> aliases) { this.aliases = new MassiveList<String>(aliases); }
	public void setAliases(String... aliases) { this.setAliases(Arrays.asList(aliases)); }
	
	public void addAliases(Collection<String> aliases) { this.aliases.addAll(aliases); }
	public void addAliases(String... aliases) { this.addAliases(Arrays.asList(aliases)); }
	
	// -------------------------------------------- //
	// PARAMETERS
	// -------------------------------------------- //
	
	public List<Parameter<?>> getParameters() { return this.parameters; }
	public void setParameters(List<Parameter<?>> parameters) { this.parameters = parameters; }
	
	public Parameter<?> getParameter(int index)
	{
		if (this.isConcatenating() && this.getConcatenationIndex() < index) index = this.getConcatenationIndex();
		return this.getParameters().get(index);
	}
	
	public Type<?> getParameterType(int index)
	{
		Parameter<?> parameter = this.getParameter(index);
		return parameter.getType();
	}
	
	public void setParameter(int index, Parameter<?> parameter)
	{
		if (this.isConcatenating() && this.getConcatenationIndex() < index) index = this.getConcatenationIndex();
		this.getParameters().set(index, parameter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setParameterType(int index, Type<?> type)
	{
		this.getParameter(index).setType((Type)type);
	}
	
	public boolean hasParameterForIndex(int index)
	{
		if (index < 0) return false;
		if (this.isConcatenating() && this.getConcatenationIndex() < index) index = this.getConcatenationIndex();
		if (this.getParameters().size() <= index) return false;
		return true;
	}
	
	// -------------------------------------------- //
	// PARAMETERS > COUNT
	// -------------------------------------------- //
	
	public int getParameterCount(CommandSender sender)
	{
		return this.getParameterCountRequired(sender) + this.getParameterCountOptional(sender);
	}
	
	public int getParameterCountRequired(CommandSender sender)
	{
		int ret = 0;
		
		for (Parameter<?> parameter : this.getParameters())
		{
			if (parameter.isRequiredFor(sender)) ret++;
		}
		
		return ret;
	}
	
	public int getParameterCountOptional(CommandSender sender)
	{
		int ret = 0;
		
		for (Parameter<?> parameter : this.getParameters())
		{
			if (parameter.isOptionalFor(sender)) ret++;
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// PARAMETERS > ADD
	// -------------------------------------------- //
	
	// The actual parameter.
	public <T> Parameter<T> addParameter(Parameter<T> parameter, boolean concatFromHere)
	{
		// Concat safety.
		if (this.isConcatenating())
		{
			throw new IllegalStateException("You can't add args if a prior one concatenates.");
		}
		
		// Req/optional safety.
		int prior = this.getParameters().size()-1;
		if (this.hasParameterForIndex(prior) && this.getParameter(prior).isOptional() && parameter.isRequired())
		{
			throw new IllegalArgumentException("You can't add required args, if a prior one is optional.");
		}
		
		// If false no change is made.
		// If true change is made.
		this.setConcatenating(concatFromHere);
		
		this.getParameters().add(parameter);
		return parameter;
	}
	
	// The actual parameter without concat.
	public <T> Parameter<T> addParameter(Parameter<T> parameter)
	{
		return this.addParameter(parameter, false);
	}
	
	// All
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, boolean requiredFromConsole, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(defaultValue, type, requiredFromConsole, name, defaultDesc), concatFromHere);
	}
	
	// WITHOUT 1
	
	// Without defaultValue
	public <T> Parameter<T> addParameter(Type<T> type, boolean requiredFromConsole, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(type, requiredFromConsole, name, defaultDesc), concatFromHere);
	}
	
	// Without reqFromConsole.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(defaultValue, type, name, defaultDesc),  concatFromHere);
	}

	// Without defaultDesc.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, boolean requiredFromConsole, String name, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(defaultValue, type, requiredFromConsole, name), concatFromHere);
	}
	
	// Without concat.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, boolean requiredFromConsole, String name, String defaultDesc)
	{
		return this.addParameter(new Parameter<T>(defaultValue, type, requiredFromConsole, name, defaultDesc), false);
	}
	
	// WITHOUT 2
	
	// Without defaultValue & reqFromConsole
	public <T> Parameter<T> addParameter(Type<T> type, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(type, name, defaultDesc), concatFromHere);
	}
	
	// Without defaultValue & defaultDesc
	public <T> Parameter<T> addParameter(Type<T> type, boolean requiredFromConsole, String name, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(type, requiredFromConsole, name), concatFromHere);
	}
	
	// Without defaultValue & concat.
	public <T> Parameter<T> addParameter(Type<T> type, boolean requiredFromConsole, String name, String defaultDesc)
	{
		return this.addParameter(new Parameter<T>(type, requiredFromConsole, name, defaultDesc));
	}

	// Without reqFromConsole & defaultDesc.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, String name, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(defaultValue, type, name), concatFromHere);
	}

	// Without reqFromConsole & concat.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, String name, String defaultDesc)
	{
		return this.addParameter(new Parameter<T>(defaultValue, type, name, defaultDesc));
	}
	
	// Without defaultDesc & concat.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, boolean requiredFromConsole, String name)
	{
		return this.addParameter(new Parameter<T>(defaultValue, type, requiredFromConsole, name));
	}
	
	// WITHOUT 3
	
	// Without defaultValue, reqFromConsole & defaultDesc.
	public <T> Parameter<T> addParameter(Type<T> type, String name, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(type, name), concatFromHere);
	}
	
	// Without defaultValue, reqFromConsole & concat .
	public <T> Parameter<T> addParameter(Type<T> type, String name, String defaultDesc)
	{
		return this.addParameter(new Parameter<T>(type, name, defaultDesc));
	}
	
	// Without defaultValue, defaultDesc & concat .
	public <T> Parameter<T> addParameter(Type<T> type, boolean requiredFromConsole, String name)
	{
		return this.addParameter(new Parameter<T>(type, requiredFromConsole, name));
	}
	
	// Without reqFromConsole, defaultDesc & concat .
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, String name)
	{
		return this.addParameter(new Parameter<T>(defaultValue, type, name));
	}
	
	// WITHOUT 4

	// Without defaultValue, reqFromConsole, defaultDesc & concat.
	public <T> Parameter<T> addParameter(Type<T> type, String name)
	{
		return this.addParameter(new Parameter<T>(type, name));
	}
	
	// Without defaultValue, name, reqFromConsole & defaultDesc.
	public <T> Parameter<T> addParameter(Type<T> type, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<T>(type), concatFromHere);
	}
	
	// Without 5
	
	// Without defaultValue, name, reqFromConsole, defaultDesc & concat.
	public <T> Parameter<T> addParameter(Type<T> type)
	{
		return this.addParameter(new Parameter<T>(type));
	}
	
	
	// -------------------------------------------- //
	// PREPROCESS
	// -------------------------------------------- //
	// These options are applied very early.
	// The code is located in the MassiveCoreBukkitCommand.

	public boolean isTokenizing() { return this.tokenizing; }
	public void setTokenizing(boolean tokenizing) { this.tokenizing = tokenizing; }
	
	public boolean isUnsmart() { return this.unsmart; }
	public void setUnsmart(boolean unsmart) { this.unsmart = unsmart; }
	
	// -------------------------------------------- //
	// PUZZLER
	// -------------------------------------------- //
	// At the puzzler phase of execution we massage the raw arguments.
	// The end result is still raw arguments but their order and composition is better adapted to our parameters.
	
	public boolean isOverflowSensitive() { return this.overflowSensitive; }
	public void setOverflowSensitive(boolean overflowSensitive) { this.overflowSensitive = overflowSensitive; }
	
	public boolean isConcatenating() { return this.concatenating; }
	public void setConcatenating(boolean concatenating) { this.concatenating = concatenating; }
	public int getConcatenationIndex() { return this.getParameters().size() - 1; }
	
	public boolean isSwapping() { return this.swapping; }
	public void setSwapping(boolean swapping) { this.swapping = swapping; }
	
	// -------------------------------------------- //
	// PUZZLER > APPLY
	// -------------------------------------------- //
	
	public List<String> applyPuzzler(List<String> args, CommandSender sender)
	{
		args = this.applyConcatenating(args);
		args = this.applySwapping(args, sender);
		return args;
	}
	
	public List<String> applyConcatenating(List<String> args)
	{
		// Really?
		if ( ! this.isConcatenating()) return args;
		
		// Create Ret
		List<String> ret = new MassiveList<String>();
		
		// Fill Ret
		final int maxIdx = Math.min(this.getConcatenationIndex(), args.size());
		ret.addAll(args.subList(0, maxIdx)); // The args that should not be concatenated.
		if (args.size() > maxIdx)
		{
			ret.add(Txt.implode(args.subList(maxIdx, args.size()), " "));
		}
		
		// Return Ret
		return ret;
	}
	
	public List<String> applySwapping(List<String> args, CommandSender sender)
	{
		// Really?
		if ( ! this.isSwapping()) return args;
		
		// So if there is too many, or too few args. We can't do much here.
		if ( ! this.isArgsValid(args)) return args;
		
		String[] ret = new String[this.getParameters().size()];
		
		args:
		for (String arg : args)
		{
			parameters:
			for (int i = 0; i < this.getParameters().size(); i++)
			{
				Type<?> type = this.getParameterType(i);
				
				if (ret[i] != null) continue parameters; // If that index is already filled.
				
				// We do in fact want to allow null args.
				// Those are used by us in some special circumstances.
				if (arg != null && ! type.isValid(arg, sender)) continue parameters; // If this arg isn't valid for that index.
				
				ret[i] = arg;
				continue args; // That arg is now set :)
			}
			// We will only end up here if an arg didn't fit any of the types.
			// In that case we failed.
			return args;
		}
		
		// Ensure that the required args are filled.
		for (int i = 0; i < this.getParameterCountRequired(sender); i++)
		{
			if (ret[i] != null) continue;
			// We end up here if an required arg wasn't filled. In that case we failed.
			return args;
		}
		
		return Arrays.asList(ret);
	}
	
	// -------------------------------------------- //
	// REQUIREMENTS
	// -------------------------------------------- //
	
	public List<Requirement> getRequirements() { return this.requirements; }
	public void setRequirements(List<Requirement> requirements) { this.requirements = requirements; }
	public void addRequirements(Requirement... requirements) { this.requirements.addAll(Arrays.asList(requirements)); }
	
	public boolean isRequirementsMet(CommandSender sender, boolean verboose)
	{
		String error = this.getRequirementsError(sender, verboose);
		if (error != null && verboose) Mixin.messageOne(sender, error);
		return error == null;
	}
	
	public String getRequirementsError(CommandSender sender, boolean verboose)
	{
		for (Requirement requirement : this.getRequirements())
		{
			if (requirement.apply(sender, this)) continue;
			if ( ! verboose) return "";
			return requirement.createErrorMessage(sender, this);
		}
		return null;
	}
	
	// -------------------------------------------- //
	// HELP
	// -------------------------------------------- //
	
	public void setDesc(String desc) { this.desc = desc; }
	public String getDesc()
	{
		if (this.desc != null) return this.desc;
		
		String perm = this.getDescPermission();
		if (perm != null)
		{
			String pdesc = PermUtil.getDescription(perm);
			if (pdesc != null)
			{
				return pdesc;
			}
		}
			
		return "*info unavailable*";
	}
	
	public void setDescPermission(String descPermission) { this.descPermission = descPermission; }
	public String getDescPermission()
	{
		if (this.descPermission != null) return this.descPermission;
		// Otherwise we try to find one.
		for (Requirement requirement : this.getRequirements())
		{
			if ( ! (requirement instanceof RequirementHasPerm)) continue;
			return ((RequirementHasPerm)requirement).getPerm();
		}
		return null;
	}
		
	public void setHelp(List<String> val) { this.help = val; }
	public void setHelp(String... val) { this.help = Arrays.asList(val); }
	public List<String> getHelp() { return this.help; }
	
	public Visibility getVisibility() { return this.visibility; }
	public void setVisibility(Visibility visibility) { this.visibility = visibility; }
	public boolean isVisibleTo(CommandSender sender)
	{
		if (this.getVisibility() == Visibility.VISIBLE) return true;
		if (this.getVisibility() == Visibility.INVISIBLE) return false;
		return this.isRequirementsMet(sender, false);
	}
	
	// -------------------------------------------- //
	// EXECUTION
	// -------------------------------------------- //
	
	public List<String> getArgs() { return this.args; }
	public void setArgs(List<String> args) { this.args = args; }

	public List<MassiveCommand> getChain() { return this.chain; }
	public void setChain(List<MassiveCommand> chain) { this.chain = chain; }
	
	// Adds command to tree structure
	public void addToChain(MassiveCommand command)
	{
		this.chain.add(0, command);
		
		List<MassiveCommand> children = this.getChildren();
		
		for (MassiveCommand child : children)
		{
			child.addToChain(command);
		}
	}
	
	public MassiveCommand getParent()
	{
		List<MassiveCommand> chain = this.getChain();
		if (chain == null) return null;
		if (chain.isEmpty()) return null;
		return chain.get(chain.size()-1);
	}
	
	public boolean hasParent()
	{
		return this.getParent() != null;
	}
	
	// -------------------------------------------- //
	// EXECUTOR
	// -------------------------------------------- //
	
	public void execute(CommandSender sender, List<String> args, List<MassiveCommand> chain)
	{
		try
		{
			// Update Chain
			this.setChain(chain);
						
			// Sender Field - Setup
			this.senderFieldsOuter(sender);
					
			// Apply Puzzler
			args = this.applyPuzzler(args, sender);
			this.setArgs(args);
			
			// Child Execution
			if (this.isParent() && args.size() > 0)
			{
				// Get matches
				String token = args.get(0);
				Map<String, MassiveCommand> matches = this.getChildMatches(token, false, null);
				
				// Score!
				if (matches.size() == 1)
				{
					MassiveCommand child = matches.entrySet().iterator().next().getValue();
					args.remove(0);
					chain.add(this);
					child.execute(sender, args, chain);
				}
				// Crap!
				else
				{
					Mson base = null;
					Collection<MassiveCommand> suggestions = null;
					
					if (matches.isEmpty())
					{
						base = Lang.COMMAND_CHILD_NONE;
						suggestions = this.getChildMatches(token, true, sender).values();
					}
					else
					{
						base = Lang.COMMAND_CHILD_AMBIGUOUS;
						suggestions = this.getChildMatches(token, false, sender).values();
					}
					
					// Message: "The sub command X couldn't be found."
					// OR
					// Message: "The sub command X is ambiguous."
					Mson bluetoken = mson(token).color(ChatColor.AQUA);
					Mixin.messageOne(sender, base.replaceAll(Lang.COMMAND_REPLACEMENT, bluetoken).command(this));
				
					// Message: "/f access ..."
					// Message: "/f ally ..."
					for (MassiveCommand suggestion : suggestions)
					{
						Mixin.messageOne(sender, suggestion.getTemplate(suggestion.getChain(), false, false, sender));
					}
				
					// Message: "Use /Y to see all commands."
					Mixin.messageOne(sender, Lang.COMMAND_CHILD_HELP.replaceAll(Lang.COMMAND_REPLACEMENT, this.getTemplate(chain, false, false, sender)).command(this));
				}
				
				// NOTE: This return statement will jump to the finally block.
				return;
			}
			
			// Self Execution (Perform)
			if (this.isValidCall(this.sender, this.getArgs()))
			{
				this.perform();
			}
		}
		catch (MassiveException ex)
		{
			// Sometimes Types (or commands themselves) throw exceptions, to stop executing and notify the user.
			if (ex.hasMessages())
			{
				Mixin.messageOne(sender, ex.getMessages());
			}
		}
		finally
		{
			// Sender Sender - Cleanup
			this.senderFieldsOuter(null);
		}
	}
	
	public void senderFieldsOuter(CommandSender sender)
	{
		this.nextArg = 0;
		this.sender = sender;
		this.senderIsConsole = true;
		this.me = null;
		if (sender instanceof Player)
		{
			this.me = (Player) sender;
			this.senderIsConsole = false;
		}
		
		boolean set = (sender != null);
		this.senderFields(set);
	}
	
	public void senderFields(boolean set) 
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
		List<MassiveCommand> chain = new ArrayList<MassiveCommand>(this.getChain());
		chain.add(this);
		
		HelpCommand.get().execute(this.sender, this.getArgs(), chain);
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
	
	public boolean isArgsValid(List<String> args, CommandSender sender)
	{
		if (args.size() < this.getParameterCountRequired(sender))
		{
			if (sender != null)
			{
				Mixin.msgOne(sender, Lang.COMMAND_TOO_FEW_ARGUMENTS);
				Mixin.messageOne(sender, this.getTemplate());
			}
			return false;
		}
		
		// We don't need to take argConcatFrom into account. Because at this point the args 
		// are already concatenated and thus cannot be too many.
		if (args.size() > this.getParameterCount(sender) && this.isOverflowSensitive())
		{
			if (sender != null)
			{
				// Get the too many string slice
				List<String> theTooMany = args.subList(this.getParameterCount(sender), args.size());
				Mixin.msgOne(sender, Lang.COMMAND_TOO_MANY_ARGUMENTS, Txt.implodeCommaAndDot(theTooMany, Txt.parse("<aqua>%s"), Txt.parse("<b>, "), Txt.parse("<b> and "), ""));
				Mixin.msgOne(sender, Lang.COMMAND_TOO_MANY_ARGUMENTS2);
				Mixin.messageOne(sender, this.getTemplate());
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
	// TEMPLATE
	// -------------------------------------------- //
	
	public static final Mson TEMPLATE_CORE = Mson.mson("/").color(ChatColor.AQUA);
	
	public Mson getTemplate(List<MassiveCommand> chain, boolean addDesc, boolean onlyFirstAlias, CommandSender sender)
	{
		// Create Ret
		Mson ret = TEMPLATE_CORE;
		
		// Get chain
		List<MassiveCommand> commands = new ArrayList<MassiveCommand>(chain);
		commands.add(this);
		
		// Add commands
		boolean first = true;
		for (MassiveCommand command : commands)
		{
			Mson mson = null;
			
			if (first && onlyFirstAlias)
			{
				mson = mson(command.getAliases().get(0));
			}
			else
			{
				mson = mson(Txt.implode(command.getAliases(), ","));
			}
			
			if (sender != null && ! command.isRequirementsMet(sender, false))
			{
				mson = mson.color(ChatColor.RED);
			}
			else
			{
				mson = mson.color(ChatColor.AQUA);
			}
			
			if ( ! first) ret = ret.add(Mson.SPACE);
			ret = ret.add(mson); 
			first = false;
		}

		// Check if last command is parentCommand and make command suggestable/clickable
		if (commands.get(commands.size() - 1).isParent())
		{
			ret = ret.command(this);
		}
		else
		{
			ret = ret.suggest(this);
		}
		
		// Add args
		for (Mson parameter : this.getTemplateParameters(sender))
		{
			ret = ret.add(Mson.SPACE);
			ret = ret.add(parameter.color(ChatColor.DARK_AQUA));
		}
		
		// Add desc
		if (addDesc)
		{
			ret = ret.add(Mson.SPACE);
			ret = ret.add(mson(this.getDesc()).color(ChatColor.YELLOW));
		}
		
		// Return Ret
		return ret;
	}
	
	protected List<Mson> getTemplateParameters(CommandSender sender)
	{
		List<Mson> ret = new MassiveList<Mson>();
		
		for (Parameter<?> parameter : this.getParameters())
		{
			ret.add(parameter.getTemplate(sender));
		}
		
		return ret;
	}
	
	public Mson getTemplate(List<MassiveCommand> chain, boolean addDesc, boolean onlyFirstAlias)
	{
		return getTemplate(chain, addDesc, onlyFirstAlias, sender);
	}
	
	public Mson getTemplate(List<MassiveCommand> chain, boolean addDesc)
	{
		return getTemplate(chain, addDesc, false);
	}
	
	public Mson getTemplate(boolean addDesc)
	{
		return getTemplate(this.getChain(), addDesc);
	}
	
	public Mson getTemplate()
	{
		return getTemplate(false);
	}
	
 	// -------------------------------------------- //
	// GET COMMAND LINE
	// -------------------------------------------- //
	
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
		for (MassiveCommand parent : this.getChain())
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
			// Check if null
			if (arg == null) continue;
				
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
		if (args == null) throw new NullPointerException("args");
		if (sender == null) throw new IllegalArgumentException("sender was null");
		if (args.isEmpty()) throw new IllegalArgumentException("args was empty");
		
		if (this.isParent())
		{
			return this.getTabCompletionsChild(args, sender);
		}
		else
		{
			return this.getTabCompletionsArg(args, sender);
		}
	}
	
	protected List<String> getTabCompletionsChild(List<String> args, CommandSender sender)
	{
		// If this isn't the last argument ...
		if (args.size() != 1)
		{
			// ... and there is a matching child ...
			MassiveCommand child = this.getChild(args.get(0));
			if (child == null) return Collections.emptyList();
			
			// ... get tab completions for that child.
			args.remove(0);
			return child.getTabCompletions(args, sender);
		}

		// ... else check the children.
		List<String> ret = new ArrayList<String>();
		String token = args.get(args.size()-1).toLowerCase();
		for (MassiveCommand child : this.getChildren())
		{
			if ( ! child.isRelevant(sender)) continue;
			ret.addAll(Txt.getStartsWithIgnoreCase(child.getAliases(), token));
		}
		
		return addSpaceAtEnd(ret);
	}
	
	protected List<String> getTabCompletionsArg(List<String> args, CommandSender sender)
	{
		args = this.applyConcatenating(args);
		
		int index = args.size() - 1;
		if ( ! this.hasParameterForIndex(index)) return Collections.emptyList();
		Type<?> type = this.getParameterType(index);
		
		List<String> ret = type.getTabListFiltered(sender, args.get(index));
		
		// If the type allows space after tab and this is not the last possible argument...
		if (type.allowSpaceAfterTab() && this.hasParameterForIndex(args.size()))
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
	
	// MSG
	
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
	
	// MESSAGE
	
	public boolean message(Object message)
	{
		return Mixin.messageOne(this.sender, message);
	}
	
	public boolean message(Object... messages)
	{
		return Mixin.messageOne(this.sender, messages);
	}
	
	public boolean message(Collection<?> messages)
	{
		return Mixin.messageOne(this.sender, messages);
	}
	
	// CONVENIENCE MSON
	
	public static Mson mson()
	{
		return Mson.mson();
	}
	
	public static Mson mson(Object... parts)
	{
		return Mson.mson(parts);
	}
	
	public static List<Mson> msons(Object... parts)
	{
		return Mson.msons(parts);
	}
	
	public static List<Mson> msons(Collection<?> parts)
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
		// Make sure that a Parameter is present.
		if ( ! this.hasParameterForIndex(idx)) throw new IllegalArgumentException(idx + " is out of range. Parameters size: " + this.getParameters().size());

		// Increment
		nextArg = idx + 1;

		// Get the parameter
		Parameter<T> parameter = (Parameter<T>) this.getParameter(idx);
		// Return the default in the parameter.
		if ( ! this.argIsSet(idx) && parameter.isDefaultValueSet()) return parameter.getDefaultValue();
		
		// OLD: Throw error if there was no arg, or default value in the parameter.
		// OLD: if ( ! this.argIsSet(idx)) throw new IllegalArgumentException("Trying to access arg: " + idx + " but that is not set.");
		// NOTE: This security actually blocks some functionality. Certain AR handle null argument values and specify their own default from within.
		// NOTE: An example is the MassiveQuest ARMNode which defaults to the used node of the player but must error when the player has no used node: "You must use a quest to skip the optional argument.".
		
		// Get the arg.
		String arg = null;
		if (this.argIsSet(idx)) arg = this.getArgs().get(idx);
		
		// Read and return the arg.
		return parameter.getType().read(arg, sender);
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

	// TODO: Some of these are still used by external plugins.
	// TODO: Fix those plugins.

	@Deprecated
	public <T> T readArgFrom(Type<T> type) throws MassiveException
	{
		return this.readArgFrom(null, type);
	}

	@Deprecated
	public <T> T readArgFrom(String str, Type<T> type) throws MassiveException
	{
		if (type == null) throw new IllegalArgumentException("type is null");
		return type.read(str, this.sender);
	}

	@Deprecated
	public <T> T readArgFrom(String str, Type<T> type, T defaultNotSet) throws MassiveException
	{
		if (str == null) return defaultNotSet;
		return this.readArgFrom(str, type);
	}
	
}
