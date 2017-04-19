package com.massivecraft.massivecore.command;


import com.massivecraft.massivecore.Active;
import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.requirement.Requirement;
import com.massivecraft.massivecore.command.requirement.RequirementAbstract;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateLevenshteinClose;
import com.massivecraft.massivecore.predicate.PredicateStartsWithIgnoreCase;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PermissionUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MassiveCommand implements Active, PluginIdentifiableCommand
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
	
	private static final transient Set<MassiveCommand> allInstances = new MassiveSet<>();
	public static Set<MassiveCommand> getAllInstances() { return allInstances; }
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	@Override
	public boolean isActive()
	{
		return getAllInstances().contains(this);
	}
	
	@Override
	public void setActive(boolean active)
	{
		// Validate
		this.validateActiveAndRoot(active, null);
		
		// Apply
		if (active)
		{
			getAllInstances().add(this);
			if (this.isSetupEnabled()) this.setup();
		}
		else
		{
			getAllInstances().remove(this);
		}
	}
	
	private MassivePlugin activePlugin = null;
	
	@Override
	public MassivePlugin setActivePlugin(MassivePlugin activePlugin)
	{
		MassivePlugin ret = this.activePlugin;
		this.activePlugin = activePlugin;
		return ret;
	}
	
	@Override
	public MassivePlugin getActivePlugin()
	{
		return this.activePlugin;
	}
	
	@Override
	public void setActive(MassivePlugin plugin)
	{
		this.setActivePlugin(plugin);
		this.setActive(plugin != null);
	}
	
	public void validateActiveAndRoot(Boolean active, Boolean root)
	{
		if (active == null) active = this.isActive();
		if (root == null) root = this.isRoot();
		if (active && ! root) throw new IllegalStateException("only root commands can be active");
	}
	
	// -------------------------------------------- //
	// PLUGIN IDENTIFIABLE COMMAND
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return this.getActivePlugin();
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	// Due to the large amount of methods in this class we place the fields alone here in the beginning.
	// Field access and other similar utility methods have their special sections below. 
	
	// === HIERARCHY ===
	
	// The parent command.
	protected MassiveCommand parent = null;
	
	// The child commands.
	protected List<MassiveCommand> children = Collections.emptyList();
	
	// === ALIASES ===
	
	// The different names this commands will react to  
	protected List<String> aliases = new MassiveList<>();
	
	// === PARAMETERS ===
	
	// The command parameters.
	protected List<Parameter<?>> parameters = new MassiveList<>();
	
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
	protected List<Requirement> requirements = new MassiveList<>();
	
	// === HELP ===
	
	// A short description of what the command does. Such as "eat hamburgers" or "do admin stuff".
	protected String desc = null;
	
	// A specific permission node to use for description if desc is null.
	protected String descPermission = null;
	
	// Free text displayed at the top of the help command.
	protected List<?> help = new ArrayList<>();
	
	// The visibility of this command in help command.
	protected Visibility visibility = Visibility.VISIBLE;

	// === SETUP ===

	// Determines whether the smart setup process will be used, works for most of commands
	protected boolean setupEnabled = false;

	// A base prefix such as "CmdFactions" that all names of commands in a plugin start with.
	// Used for finding the right permission.
	// Should be set in a super class for all the commands in a plugin.
	protected String setupPermBaseClassName = null;

	// The Class representing the permissions.
	protected Class<? extends Enum<?>> setupPermClass = null;
	
	// === EXECUTION ===
	
	// The raw string arguments passed upon execution. An empty list if there are none.
	protected List<String> args = new MassiveList<>();
	
	// The index of the next arg to read.
	public int nextArg = 0;
	
	// ...
	public CommandSender sender = null;
	
	// ...
	public Player me = null;
	
	// ...
	public boolean senderIsConsole = false;	
	
	// -------------------------------------------- //
	// HIERARCHY
	// -------------------------------------------- //
	
	public MassiveCommand getParent() { return this.parent; }
	public boolean hasParent() { return this.getParent() != null; }
	public boolean isChild() { return this.hasParent(); }
	public boolean isRoot() { return ! this.hasParent(); }
	
	public List<MassiveCommand> getChildren() { return this.children; }
	public boolean hasChildren() { return ! this.getChildren().isEmpty(); }
	public boolean isParent() { return this.hasChildren(); }
	public boolean isLeaf() { return ! this.hasChildren(); }
	
	public List<MassiveCommand> getVisibleChildren(CommandSender watcher)
	{
		// Create
		List<MassiveCommand> ret = new MassiveList<>();
		
		// Fill
		for (MassiveCommand child : this.getChildren())
		{
			if (child.isVisibleTo(watcher)) ret.add(child);
		}
		
		// Return
		return ret;
	}
	
	public MassiveCommand getRoot()
	{
		// Create
		MassiveCommand ret = this;
		
		// Fill
		while (ret.hasParent())
		{
			ret = ret.getParent();
		}
		
		// Return
		return ret;
	}
	
	// The parents is like a stack trace.
	// We start with ourselves. The root is at the end.
	public List<MassiveCommand> getParents(boolean includeSelf)
	{
		// Create
		List<MassiveCommand> ret = new MassiveList<>();
		
		// Fill
		if (includeSelf) ret.add(this);
		MassiveCommand parent = this.getParent();
		while (parent != null)
		{
			ret.add(parent);
			parent = parent.getParent();
		}
		
		// Return
		return ret;
	}
	
	// The chain is the parents in reversed order.
	public List<MassiveCommand> getChain(boolean includeSelf)
	{
		List<MassiveCommand> ret = this.getParents(includeSelf);
		Collections.reverse(ret);
		return ret;
	}
	
	public void removeParent()
	{
		// NoChange
		if (!this.hasParent()) return;
		
		// Apply
		MassiveCommand parent = this.getParent();
		parent.removeChild(this);
		this.parent = null;
	}
	
	public void setParent(MassiveCommand parent)
	{
		// NoChange
		if (MUtil.equals(this.getParent(), parent)) return;
		
		// Remove
		this.removeParent();
		
		// NoSet
		if (parent == null) return;
		
		// Validate
		this.validateActiveAndRoot(null, false);
		
		// Apply
		this.parent = parent;
		parent.addChild(this);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends MassiveCommand> T addChild(MassiveCommand child)
	{
		// NoChange
		if (this.getChildren().indexOf(child) != -1) return (T) this;
			
		// Apply
		return this.addChild(child, this.getChildren().size());
	}
	
	public <T extends MassiveCommand> T addChildAfter(MassiveCommand child, MassiveCommand after)
	{
		int index = this.getChildren().indexOf(after);
		if (index == -1)
		{
			index = this.getChildren().size();
		}
		else
		{
			index++;
		}
		return this.addChild(child, index);
	}
	
	public int replaceChild(MassiveCommand child, MassiveCommand replaced)
	{
		int index = this.removeChild(replaced);
		if (index < 0) return index;
		this.addChild(child, index);
		return index;
	}
	
	public int removeChild(MassiveCommand child)
	{
		List<MassiveCommand> children = new MassiveList<>(this.getChildren());
		int index = children.indexOf(child);
		if (index == -1) return -1;
		children.remove(index);
		this.children = Collections.unmodifiableList(children);
		child.removeParent();
		return index;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends MassiveCommand> T addChild(MassiveCommand child, int index)
	{
		if (!this.hasChildren() && !(child instanceof MassiveCommandHelp))
		{
			this.getHelpCommand();
			index++;
		}
		
		List<MassiveCommand> children = new MassiveList<>(this.getChildren());
		children.add(index, child);
		this.children = Collections.unmodifiableList(children);
		child.setParent(this);
		
		return (T) this;
	}
	
	public MassiveCommandHelp getHelpCommand()
	{
		if ( ! this.hasChildren()) this.addChild(new MassiveCommandHelp(), 0);
		List<MassiveCommand> children = this.getChildren();
		return (MassiveCommandHelp) children.get(0);
	}
	
	// -------------------------------------------- //
	// CHILDREN > GET
	// -------------------------------------------- //
	
	// The full version of the child matcher method.
	// Returns a set of child commands with similar aliases.
	//
	// token - the full alias or an alias prefix.
	// levenshtein - should we use levenshtein instead of starts with?
	// prioritizeExact - return single element set on full match.
	// 
	// An empty set means no child was found.
	// A single element set means we found an unambiguous match.
	// A larger set means the token was ambiguous.
	private Set<MassiveCommand> getChildren(String token, boolean levenshtein, CommandSender onlyRelevantToSender)
	{
		// Create Ret
		Set<MassiveCommand> ret = new MassiveSet<>();
		
		// Prepare
		token = token.toLowerCase();
		Predicate<String> predicate = levenshtein ? PredicateLevenshteinClose.get(token) : PredicateStartsWithIgnoreCase.get(token);
		
		// Fill Ret
		// Go through each child command
		for (MassiveCommand child : this.getChildren())
		{
			// See if any of the aliases has a match or close enough
			// If there is a direct match, return that
			for (String alias : child.getAliases())
			{
				// ... consider exact priority ...
				if (alias.equalsIgnoreCase(token))
				{
					return Collections.singleton(child);
				}
				
				if (ret.contains(child)) continue;
				
				// ... matches ...
				if (!predicate.apply(alias)) continue;
				
				// ... and put in ret.
				ret.add(child);
			}
		}
		
		// Only Relevant
		if (onlyRelevantToSender != null)
		{
			for  (Iterator<MassiveCommand> iterator = ret.iterator(); iterator.hasNext(); )
			{
				MassiveCommand command = iterator.next();
				if (command.isRelevant(onlyRelevantToSender)) continue;
				iterator.remove();
			}
		}
		
		// Return Ret
		return ret;
	}
	
	// A simplified version returning null on ambiguity and nothing found.
	public MassiveCommand getChild(String token)
	{
		Set<MassiveCommand> children = this.getChildren(token, false, null);
		
		if (children.isEmpty()) return null;
		if (children.size() > 1) return null;
		return children.iterator().next();
	}
	
	protected boolean isRelevant(CommandSender sender)
	{
		if (sender == null) return true;
		
		if (!this.isVisibleTo(sender)) return false;
		if (!this.isRequirementsMet(sender, false)) return false;
		
		return true;
	}
	
	// -------------------------------------------- //
	// ALIASES
	// -------------------------------------------- //
	
	public List<String> getAliases() { return this.aliases; }
	
	@SuppressWarnings("unchecked")
	public <T extends MassiveCommand> T setAliases(Collection<String> aliases) { this.aliases = new MassiveList<>(aliases); return (T) this; }
	public <T extends MassiveCommand> T setAliases(String... aliases) { return this.setAliases(Arrays.asList(aliases)); }
	
	@SuppressWarnings("unchecked")
	public <T extends MassiveCommand> T addAliases(Collection<String> aliases) { this.aliases.addAll(aliases); return (T) this; }
	public <T extends MassiveCommand> T addAliases(String... aliases) { return this.addAliases(Arrays.asList(aliases)); }
	
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
		return this.addParameter(new Parameter<>(defaultValue, type, requiredFromConsole, name, defaultDesc), concatFromHere);
	}
	
	// WITHOUT 1
	
	// Without defaultValue
	public <T> Parameter<T> addParameter(Type<T> type, boolean requiredFromConsole, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<>(type, requiredFromConsole, name, defaultDesc), concatFromHere);
	}
	
	// Without reqFromConsole.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<>(defaultValue, type, name, defaultDesc),  concatFromHere);
	}

	// Without defaultDesc.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, boolean requiredFromConsole, String name, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<>(defaultValue, type, requiredFromConsole, name), concatFromHere);
	}
	
	// Without concat.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, boolean requiredFromConsole, String name, String defaultDesc)
	{
		return this.addParameter(new Parameter<>(defaultValue, type, requiredFromConsole, name, defaultDesc), false);
	}
	
	// WITHOUT 2
	
	// Without defaultValue & reqFromConsole
	public <T> Parameter<T> addParameter(Type<T> type, String name, String defaultDesc, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<>(type, name, defaultDesc), concatFromHere);
	}
	
	// Without defaultValue & defaultDesc
	public <T> Parameter<T> addParameter(Type<T> type, boolean requiredFromConsole, String name, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<>(type, requiredFromConsole, name), concatFromHere);
	}
	
	// Without defaultValue & concat.
	public <T> Parameter<T> addParameter(Type<T> type, boolean requiredFromConsole, String name, String defaultDesc)
	{
		return this.addParameter(new Parameter<>(type, requiredFromConsole, name, defaultDesc));
	}

	// Without reqFromConsole & defaultDesc.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, String name, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<>(defaultValue, type, name), concatFromHere);
	}

	// Without reqFromConsole & concat.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, String name, String defaultDesc)
	{
		return this.addParameter(new Parameter<>(defaultValue, type, name, defaultDesc));
	}
	
	// Without defaultDesc & concat.
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, boolean requiredFromConsole, String name)
	{
		return this.addParameter(new Parameter<>(defaultValue, type, requiredFromConsole, name));
	}
	
	// WITHOUT 3
	
	// Without defaultValue, reqFromConsole & defaultDesc.
	public <T> Parameter<T> addParameter(Type<T> type, String name, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<>(type, name), concatFromHere);
	}
	
	// Without defaultValue, reqFromConsole & concat .
	public <T> Parameter<T> addParameter(Type<T> type, String name, String defaultDesc)
	{
		return this.addParameter(new Parameter<>(type, name, defaultDesc));
	}
	
	// Without defaultValue, defaultDesc & concat .
	public <T> Parameter<T> addParameter(Type<T> type, boolean requiredFromConsole, String name)
	{
		return this.addParameter(new Parameter<>(type, requiredFromConsole, name));
	}
	
	// Without reqFromConsole, defaultDesc & concat .
	public <T> Parameter<T> addParameter(T defaultValue, Type<T> type, String name)
	{
		return this.addParameter(new Parameter<>(defaultValue, type, name));
	}
	
	// WITHOUT 4

	// Without defaultValue, reqFromConsole, defaultDesc & concat.
	public <T> Parameter<T> addParameter(Type<T> type, String name)
	{
		return this.addParameter(new Parameter<>(type, name));
	}
	
	// Without defaultValue, name, reqFromConsole & defaultDesc.
	public <T> Parameter<T> addParameter(Type<T> type, boolean concatFromHere)
	{
		return this.addParameter(new Parameter<>(type), concatFromHere);
	}
	
	// Without 5
	
	// Without defaultValue, name, reqFromConsole, defaultDesc & concat.
	public <T> Parameter<T> addParameter(Type<T> type)
	{
		return this.addParameter(new Parameter<>(type));
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
		List<String> ret = new MassiveList<>();
		
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
	@SuppressWarnings("unchecked")
	public <T extends MassiveCommand> T setRequirements(List<Requirement> requirements) { this.requirements = requirements; return (T) this; }
	@SuppressWarnings("unchecked")
	public <T extends MassiveCommand> T addRequirements(Collection<Requirement> requirements) { this.requirements.addAll(requirements); return (T) this; }
	@SuppressWarnings("unchecked")
	public <T extends MassiveCommand> T addRequirements(Requirement... requirements) { this.addRequirements(Arrays.asList(requirements)); return (T) this; }
	
	public boolean isRequirementsMet(CommandSender sender, boolean verboose)
	{
		return RequirementAbstract.isRequirementsMet(this.getRequirements(), sender, this, verboose);
	}
	
	public String getRequirementsError(CommandSender sender, boolean verboose)
	{
		return RequirementAbstract.getRequirementsError(this.getRequirements(), sender, this, verboose);
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
			String pdesc = PermissionUtil.getPermissionAction(perm);
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
				return ((RequirementHasPerm)requirement).getPermissionId();
			}
			return null;
		}
		
	public void setHelp(List<?> val) { this.help = val; }
	public void setHelp(Object... val) { this.help = Arrays.asList(val); }
	public List<?> getHelp() { return this.help; }
	
	public Visibility getVisibility() { return this.visibility; }
	public void setVisibility(Visibility visibility) { this.visibility = visibility; }
	public boolean isVisibleTo(CommandSender sender)
	{
		if (this.getVisibility() == Visibility.VISIBLE) return true;
		if (this.getVisibility() == Visibility.INVISIBLE) return false;
		return this.isRequirementsMet(sender, false);
	}

	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //

	public boolean isSetupEnabled() { return this.setupEnabled; }
	public void setSetupEnabled(boolean setupEnabled) { this.setupEnabled = setupEnabled; }

	public String getSetupPermBaseClassName()
	{
		// Use field if present
		if (this.setupPermBaseClassName != null) return this.setupPermBaseClassName;

		// Otherwise guess
		// Since some commands such as CmdMassiveCoreUsys aren't the base command
		// but are still outer/root commands we can't always trust the root command.
		// Thus we try by using the plugin name first.
		Plugin plugin = this.getRoot().getPlugin();
		String pluginName = plugin.getName();
		String cmdPackageName = plugin.getClass().getName() + ".cmd.";

		// Try Cmd + plugin name
		String baseCommandName = "Cmd" + pluginName;
		if (ReflectionUtil.classExists(cmdPackageName + baseCommandName)) return baseCommandName;

		// Try Cmd + plugin name without "Massive prefix"
		if (pluginName.startsWith("Massive"))
		{
			baseCommandName = "Cmd" + pluginName.substring("Massive".length());
			if (ReflectionUtil.classExists(cmdPackageName + baseCommandName)) return baseCommandName;
		}

		// Use the name of the root command
		return this.getRoot().getClass().getSimpleName();
	}
	public void setSetupPermBaseClassName(String setupPermBaseClassName) { this.setupPermBaseClassName = setupPermBaseClassName; }

	public <T extends Enum<T>> Class<T> getSetupPermClass()
	{
		// Use field if present
		if (this.setupPermClass != null) return (Class) this.setupPermClass;

		// Otherwise guess
		String nameClass = this.getRoot().getPlugin().getClass().getPackage().getName() + ".Perm";
		try
		{
			return (Class) Class.forName(nameClass);
		}
		catch (ClassNotFoundException e)
		{
			// If setup is enabled but the class is neither specified nor findable
			// an error should be thrown to notify the developer.
			throw new RuntimeException(e);
		}
	}
	public <T extends Enum<T>> void setSetupPermClass(Class<T> setupPermClass) { this.setupPermClass = setupPermClass; }

	public void setup()
	{
		String name = this.calcName();
		if (name != null) this.getAliases().add(0, name);

		Object permission = this.calcPerm();
		if (permission != null) this.addRequirements(RequirementHasPerm.get(permission));

		this.setupAddChildren();
		this.setupChildren();
	}

	public void setupAddChildren()
	{
		String s = this.getClass().getSimpleName();

		for (Field field : this.getClass().getDeclaredFields())
		{
			ReflectionUtil.makeAccessible(field);
			Class<?> fieldType = field.getType();

			if (!MassiveCommand.class.isAssignableFrom(fieldType)) continue;
			if (Modifier.isStatic(field.getModifiers())) continue;

			MassiveCommand child = ReflectionUtil.getField(field, this);
			this.addChild(child);
		}
	}

	public void setupChildren()
	{
		for (MassiveCommand child : this.getChildren())
		{
			if (child.isSetupEnabled()) child.setup();
		}
	}

	protected String calcName()
	{
		// If this is a child command
		if (this.isRoot()) return null;

		// ... get name of parent ...
		String parentName = this.getParent().getClass().getSimpleName();

		// ... and only try if the names match ...
		String name = this.getClass().getSimpleName();
		if ( ! name.startsWith(parentName)) return null;

		// ... and without parent prefix ...
		String ret = name.substring(parentName.length());

		// ... and lowercase the first character.
		ret = Character.toLowerCase(ret.charAt(0)) + ret.substring(1);

		return ret;
	}

	protected <T extends Enum<T>> T calcPerm()
	{
		Class<T> permClass = this.getSetupPermClass();
		String basePrefix = this.getSetupPermBaseClassName();

		if (permClass == null) return null;
		if (basePrefix == null) return null;

		// Only try if the name matches with the expected prefix ...
		String name = this.getClass().getSimpleName();
		if ( ! name.startsWith(basePrefix)) return null;

		// ... and remove the prefix  ...
		String permName = name.substring(basePrefix.length());

		// ... split at new words and separate with underscore.
		permName = Txt.implode(Txt.camelsplit(permName), "_");

		// Enums are alway upper case.
		permName = permName.toUpperCase();

		// If the name is empty it is the base command
		if (permName.isEmpty()) permName = "BASECOMMAND";

		// Create ret
		T ret = null;

		// Try non-lenient
		ret = getPerm(permName, false, permClass);
		if (ret != null) return ret;

		// Try lenient
		ret = getPerm(permName, true, permClass);
		if (ret != null) return ret;

		throw new RuntimeException("Could not find permission matching: " + permName);
	}

	protected static <T extends Enum<T>> T getPerm(String permName, boolean lenient, Class<T> permClass)
	{
		permName = getPermCompareString(permName, lenient);
		for (T perm : TypeEnum.getEnumValues(permClass))
		{
			String compare = getPermCompareString(perm.name(), lenient);
			if (compare.equals(permName)) return perm;
		}
		return null;
	}

	protected static String getPermCompareString(String permName, boolean lenient)
	{
		if (lenient)
		{
			permName = permName.toUpperCase();
			permName = permName.replace("_", "");
		}
		return permName;
	}

	// -------------------------------------------- //
	// EXECUTION
	// -------------------------------------------- //
	
	public List<String> getArgs() { return this.args; }
	public void setArgs(List<String> args) { this.args = args; }
	
	// -------------------------------------------- //
	// EXECUTOR
	// -------------------------------------------- //
	
	public void execute(CommandSender sender, List<String> args)
	{
		try
		{
			// Sender Field - Setup
			this.senderFieldsOuter(sender);
					
			// Apply Puzzler
			args = this.applyPuzzler(args, sender);
			this.setArgs(args);
			
			// Requirements
			if (!this.isRequirementsMet(sender, true)) return;
			
			// Child Execution
			if (this.isParent() && args.size() > 0)
			{
				// Get matches
				String token = args.get(0);
				
				Set<MassiveCommand> matches = this.getChildren(token, false, null);
				
				// Score!
				if (matches.size() == 1)
				{
					MassiveCommand child = matches.iterator().next();
					args.remove(0);
					child.execute(sender, args);
				}
				// Crap!
				else
				{
					Mson base = null;
					Collection<MassiveCommand> suggestions = null;
					
					if (matches.isEmpty())
					{
						base = Lang.COMMAND_CHILD_NONE;
						suggestions = this.getChildren(token, true, sender);
					}
					else
					{
						base = Lang.COMMAND_CHILD_AMBIGUOUS;
						suggestions = this.getChildren(token, false, sender);
					}
					
					// Message: "The sub command X couldn't be found."
					// OR
					// Message: "The sub command X is ambiguous."
					Mson bluetoken = mson(token).color(ChatColor.AQUA);
					MixinMessage.get().messageOne(sender, base.replaceAll(Lang.COMMAND_REPLACEMENT, bluetoken).command(this));
				
					// Message: "/f access ..."
					// Message: "/f ally ..."
					for (MassiveCommand suggestion : suggestions)
					{
						MixinMessage.get().messageOne(sender, suggestion.getTemplate(false, false, sender));
					}
				
					// Message: "Use /Y to see all commands."
					MixinMessage.get().messageOne(sender, Lang.COMMAND_CHILD_HELP.replaceAll(Lang.COMMAND_REPLACEMENT, this.getTemplate(false, false, sender)).command(this));
				}
				
				// NOTE: This return statement will jump to the finally block.
				return;
			}
			
			// Self Execution > Arguments Valid
			if (!this.isArgsValid(this.getArgs(), this.sender)) return;
			
			// Self Execution > Perform
			this.perform();
		}
		catch (MassiveException ex)
		{
			// Sometimes Types (or commands themselves) throw exceptions, to stop executing and notify the user.
			if (ex.hasMessages())
			{
				MixinMessage.get().messageOne(sender, ex.getMessages());
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
	
	// This is where the command action is performed.
	public void perform() throws MassiveException
	{
		// Per default we just run the help command!
		this.getHelpCommand().execute(this.sender, this.getArgs());
	}

	// -------------------------------------------- //
	// CALL VALIDATION
	// -------------------------------------------- //
	
	public boolean isArgsValid(List<String> args, CommandSender sender)
	{
		if (args.size() < this.getParameterCountRequired(sender))
		{
			if (sender != null)
			{
				MixinMessage.get().msgOne(sender, Lang.COMMAND_TOO_FEW_ARGUMENTS);
				MixinMessage.get().messageOne(sender, this.getTemplate());
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
				MixinMessage.get().msgOne(sender, Lang.COMMAND_TOO_MANY_ARGUMENTS, Txt.implodeCommaAndDot(theTooMany, Txt.parse("<aqua>%s"), Txt.parse("<b>, "), Txt.parse("<b> and "), ""));
				MixinMessage.get().msgOne(sender, Lang.COMMAND_TOO_MANY_ARGUMENTS2);
				MixinMessage.get().messageOne(sender, this.getTemplate());
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
	
	public Mson getTemplate(boolean addDesc, boolean onlyFirstAlias, CommandSender sender)
	{
		// Create Ret
		Mson ret = TEMPLATE_CORE;
		
		// Get commands
		List<MassiveCommand> commands = this.getChain(true);
		
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
		List<Mson> ret = new MassiveList<>();
		
		for (Parameter<?> parameter : this.getParameters())
		{
			ret.add(parameter.getTemplate(sender));
		}
		
		return ret;
	}
	
	public Mson getTemplate(boolean addDesc, boolean onlyFirstAlias)
	{
		return getTemplate( addDesc, onlyFirstAlias, sender);
	}
	
	public Mson getTemplate(boolean addDesc)
	{
		return getTemplate(addDesc, false);
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
		for (MassiveCommand parent : this.getChain(false))
		{
			// Append parent
			ret.append(parent.getAliases().get(0));
			
			// Append space
			ret.append(' ');
		}
		
		// Then ourself
		if (this.getAliases().isEmpty()) throw new IllegalStateException(this.getClass().getSimpleName() + " has no aliases.");
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
		List<String> ret = new ArrayList<>();
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
		return MixinMessage.get().msgOne(this.sender, msg);
	}
	
	public boolean msg(String msg, Object... args)
	{
		return MixinMessage.get().msgOne(this.sender, msg, args);
	}
	
	public boolean msg(Collection<String> msgs)
	{
		return MixinMessage.get().msgOne(this.sender, msgs);
	}
	
	// MESSAGE
	
	public boolean message(Object message)
	{
		return MixinMessage.get().messageOne(this.sender, message);
	}
	
	public boolean message(Object... messages)
	{
		return MixinMessage.get().messageOne(this.sender, messages);
	}
	
	public boolean message(Collection<?> messages)
	{
		return MixinMessage.get().messageOne(this.sender, messages);
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
