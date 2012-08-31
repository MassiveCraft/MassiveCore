package com.massivecraft.mcore4.cmd;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import com.massivecraft.mcore4.Lang;
import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.cmd.arg.IArgHandler;
import com.massivecraft.mcore4.cmd.req.IReq;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.util.Perm;
import com.massivecraft.mcore4.util.Txt;

public abstract class MCommand
{
	public abstract MPlugin p();
	
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
	public void addAliases(String... aliases) { this.aliases.addAll(Arrays.asList(aliases)); }
	public void addAliases(List<String> aliases) { this.aliases.addAll(aliases); }
	
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
	protected boolean errorOnToManyArgs;
	public boolean getErrorOnToManyArgs() { return this.errorOnToManyArgs; }
	public void setErrorOnToManyArgs(boolean val) { this.errorOnToManyArgs = val; }
	
	// FIELD: requirements
	// All these requirements must be met for the command to be executable;
	protected List<IReq> requirements;
	public List<IReq> getRequirements() { return this.requirements; }
	public void setRequirements(List<IReq> val) { this.requirements = val; }
	public void addRequirements(IReq... requirements) { this.requirements.addAll(Arrays.asList(requirements)); }
	
	// FIELD: desc
	// This field may be left blank and will in such case be loaded from the permissions node instead.
	// Thus make sure the permissions node description is an action description like "eat hamburgers" or "do admin stuff".
	protected String desc = null;
	public void setDesc(String val) { this.desc = val; }
	public String getDesc()
	{
		if (this.desc != null) return this.desc;
		
		String perm = this.getDescPermission();
		if (perm != null)
		{
			String pdesc = Perm.getPermissionDescription(this.getDescPermission());
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
	public String getDescPermission()
	{
		if (this.descPermission != null) return this.descPermission;
		// Otherwise we try to find one.
		for (IReq req : this.requirements)
		{
			if ( ! (req instanceof ReqHasPerm)) continue;
			return ((ReqHasPerm)req).getPerm();
		}
		return null;
	}
	public void setDescPermission(String val) { this.descPermission = val; }
	
	// FIELD: help
	// This is a multi-line help text for the command.
	protected List<String> help = new ArrayList<String>();
	public void setHelp(List<String> val) { this.help = val; }
	public void setHelp(String... val) { this.help = Arrays.asList(val); }
	public List<String> getHelp() { return this.help; }
	
	// FIELD: visibilityMode
	protected VisibilityMode visibilityMode;
	public VisibilityMode getVisibilityMode() { return this.visibilityMode; }
	public void setVisibilityMode(VisibilityMode val) { this.visibilityMode = val; }
	
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
	
	// FIELDS: sender, me, senderIsConsole
	public CommandSender sender;
	public Player me;
	public boolean senderIsConsole;
	
	// -------------------------------------------- //
	// BUKKIT INTEGRATION
	// -------------------------------------------- //
	
	public boolean register()
	{
		return register(false);
	}
	
	public boolean register(boolean override)
	{
		BukkitGlueCommand bgc = new BukkitGlueCommand(this);
		SimpleCommandMap scm = Cmd.getBukkitCommandMap();
		
		if (override)
		{
			// Our commands are more important than your commands :P
			Map<String, Command> knownCommands = Cmd.getKnownCommandsFromSimpleCommandMap(scm);
			String lowerLabel = bgc.getName().trim().toLowerCase();
			knownCommands.remove(lowerLabel);
		}
		
		return scm.register("mcore", bgc);
	}
	
	// -------------------------------------------- //
	// CONSTRUCTORS AND EXECUTOR
	// -------------------------------------------- //
	
	public MCommand()
	{
		this.descPermission = null;
		
		this.subCommands = new ArrayList<MCommand>();
		this.aliases = new ArrayList<String>();
		
		this.requiredArgs = new ArrayList<String>();
		this.optionalArgs = new LinkedHashMap<String, String>();
		
		this.requirements = new ArrayList<IReq>();
		
		this.errorOnToManyArgs = true;
		
		this.desc = null;
		
		this.visibilityMode = VisibilityMode.VISIBLE; 
	}
	
	// The commandChain is a list of the parent command chain used to get to this command.
	public void execute(CommandSender sender, List<String> args, List<MCommand> commandChain)
	{
		// Set the execution-time specific variables
		this.sender = sender;
		this.senderIsConsole = true;
		this.me = null;
		if (sender instanceof Player)
		{
			this.me = (Player) sender;
			this.senderIsConsole = false;
		}
		
		this.fixSenderVars();
		
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
		
		perform();
	}
	
	public void fixSenderVars() {};
	
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
		if ( ! this.requirementsAreMet(sender, true))
		{
			return false;
		}
		
		if ( ! this.validArgs(args, sender))
		{
			return false;
		}
		
		return true;
	}
	
	public boolean visibleTo(CommandSender sender)
	{
		if (this.getVisibilityMode() == VisibilityMode.VISIBLE) return true;
		if (this.getVisibilityMode() == VisibilityMode.INVISIBLE) return false;
		return this.requirementsAreMet(sender, false);
	}
	
	public boolean requirementsAreMet(CommandSender sender, boolean informSenderIfNot)
	{
		for (IReq req : this.getRequirements())
		{
			if ( ! req.test(sender, this))
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
				msg(Lang.commandToManyArgs, Txt.implodeCommaAndDot(theToMany, Txt.parse("<aqua>%s"), Txt.parse("<b>, "), Txt.parse("<b> and "), ""));
				msg(Lang.commandToManyArgs2);
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
	
	public String getUseageTemplate(List<MCommand> commandChain, boolean addDesc, boolean onlyFirstAlias)
	{
		StringBuilder ret = new StringBuilder();
		ret.append(Txt.parse("<c>"));
		ret.append('/');
		
		boolean first = true;
		for (MCommand mc : commandChain)
		{
			if (first && onlyFirstAlias)
			{
				ret.append(mc.aliases.get(0));
			}
			else
			{
				ret.append(Txt.implode(mc.aliases, ","));
			}
			ret.append(' ');
			first = false;
		}
		
		ret.append(Txt.implode(this.aliases, ","));
		
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
	
	public String getUseageTemplate(List<MCommand> commandChain, boolean addDesc)
	{
		return getUseageTemplate(commandChain, addDesc, false);
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
		sender.sendMessage(Txt.parse(str, args));
	}
	
	public void msg(String str)
	{
		sender.sendMessage(Txt.parse(str));
	}
	
	public void msg(Collection<String> msgs)
	{
		for(String msg : msgs)
		{
			this.msg(msg);
		}
	}
	
	public void sendMessage(String msg)
	{
		sender.sendMessage(msg);
	}
	
	public void sendMessage(Collection<String> msgs)
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
		IArgHandler<T> handler = p().cmd.getArgHandler(clazz);
		
		if (handler == null)
		{
			p().log(Level.SEVERE, "There is no ArgHandler for " + clazz.getName());
		}
		
		T ret = handler.parse(this.arg(idx), style, this.sender, p());
		if (ret == null)
		{
			this.msg(handler.getErrors());
			return defaultNotFound;
		}
		return ret;
	}
	
	public <T> T argAs(int idx, Class<T> clazz, T defaultNotSet, T defaultNotFound)
	{
		return this.argAs(idx, clazz, null, defaultNotSet, defaultNotFound);
	}
	
	public <T> T argAs(int idx, Class<T> clazz, String style, T defaultNotSet)
	{
		return this.argAs(idx, clazz, style, defaultNotSet, null);
	}
	
	public <T> T argAs(int idx, Class<T> clazz, T defaultNotSet)
	{
		return this.argAs(idx, clazz, null, defaultNotSet, null);
	}
	
	public <T> T argAs(int idx, Class<T> clazz, String style)
	{
		return this.argAs(idx, clazz, style, null, null);
	}
	
	public <T> T argAs(int idx, Class<T> clazz)
	{
		return this.argAs(idx, clazz, (T)null, null);
	}
	
	public String argConcatFrom(int idx)
	{
		if ( ! this.argIsSet(idx)) return null;
		int from = idx;
		int to = args.size();
		if (to <= from) return "";
		return Txt.implode(args.subList(from, to), " ");
	}
}
