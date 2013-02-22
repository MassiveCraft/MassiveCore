package com.massivecraft.mcore5.cmd;

import java.util.*;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import com.massivecraft.mcore5.Lang;
import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.MPlugin;
import com.massivecraft.mcore5.cmd.arg.ArgReader;
import com.massivecraft.mcore5.cmd.arg.ArgResult;
import com.massivecraft.mcore5.cmd.req.IReq;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.mcore5.mixin.Mixin;
import com.massivecraft.mcore5.util.BukkitCommandUtil;
import com.massivecraft.mcore5.util.PermUtil;
import com.massivecraft.mcore5.util.Txt;

public abstract class MCommand
{	
	// -------------------------------------------- //
	// COMMAND BEHAVIOR
	// -------------------------------------------- //
	
	// FIELD: subCommands
	// The sub-commands to this command
	@Getter @Setter protected List<MCommand> subCommands;
	public void addSubCommand(MCommand subCommand)
	{
		subCommand.commandChain.addAll(this.commandChain);
		subCommand.commandChain.add(this);
		this.subCommands.add(subCommand);
	}
	
	// FIELD: aliases
	// The different names this commands will react to  
	@Getter @Setter protected List<String> aliases;
	public void addAliases(String... aliases) { this.aliases.addAll(Arrays.asList(aliases)); }
	public void addAliases(List<String> aliases) { this.aliases.addAll(aliases); }
	
	// FIELD: requiredArgs
	// These args must always be sent
	@Getter @Setter protected List<String> requiredArgs;
	public void addRequiredArg(String arg) { this.requiredArgs.add(arg); }
	
	// FIELD: optionalArgs
	// These args are optional
	@Getter @Setter protected Map<String, String> optionalArgs;
	public void addOptionalArg(String arg, String def) { this.optionalArgs.put(arg, def); }
	
	// FIELD: errorOnToManyArgs
	// Should an error be thrown if "to many" args are sent.
	protected boolean errorOnToManyArgs;
	public boolean getErrorOnToManyArgs() { return this.errorOnToManyArgs; }
	public void setErrorOnToManyArgs(boolean val) { this.errorOnToManyArgs = val; }
	
	// FIELD: requirements
	// All these requirements must be met for the command to be executable;
	@Getter @Setter protected List<IReq> requirements;
	public void addRequirements(IReq... requirements) { this.requirements.addAll(Arrays.asList(requirements)); }
	
	// FIELD: desc
	// This field may be left blank and will in such case be loaded from the permissions node instead.
	// Thus make sure the permissions node description is an action description like "eat hamburgers" or "do admin stuff".
	@Setter protected String desc = null;
	public String getDesc()
	{
		if (this.desc != null) return this.desc;
		
		String perm = this.getDescPermission();
		if (perm != null)
		{
			String pdesc = PermUtil.getPermissionDescription(this.getDescPermission());
			if (pdesc != null)
			{
				return pdesc;
			}
		}
			
		return "*info unavailable*";
	}
	
	// FIELD: descPermission
	// This permission node IS NOT TESTED AT ALL. It is rather used in the method above.
	@Setter protected String descPermission;
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
	
	// FIELD: help
	// This is a multi-line help text for the command.
	protected List<String> help = new ArrayList<String>();
	public void setHelp(List<String> val) { this.help = val; }
	public void setHelp(String... val) { this.help = Arrays.asList(val); }
	public List<String> getHelp() { return this.help; }
	
	// FIELD: visibilityMode
	@Getter @Setter protected VisibilityMode visibilityMode;
	
	// -------------------------------------------- //
	// EXECUTION INFO
	// -------------------------------------------- //
	
	// FIELD: args
	// Will contain the arguments, or and empty list if there are none.
	@Getter @Setter protected List<String> args;

	// FIELD: commandChain
	// The command chain used to execute this command
	@Getter @Setter protected List<MCommand> commandChain = new ArrayList<MCommand>();
	
	// FIELDS: sender, me, senderIsConsole
	public CommandSender sender;
	public Player me;
	public boolean senderIsConsole;
	
	// -------------------------------------------- //
	// BUKKIT INTEGRATION
	// -------------------------------------------- //
	
	public boolean register()
	{
		return register(MCore.p, false);
	}
	
	public boolean register(MPlugin mplugin)
	{
		return this.register(mplugin, false);
	}
	
	public boolean register(boolean override)
	{
		return this.register(MCore.p, override);
	}
	
	public boolean register(MPlugin mplugin, boolean override)
	{
		boolean ret = false;
		
		SimpleCommandMap scm = BukkitCommandUtil.getBukkitCommandMap();
		
		for (String alias : this.getAliases())
		{
			BukkitGlueCommand bgc = new BukkitGlueCommand(alias, this, mplugin);
			
			if (override)
			{
				// Our commands are more important than your commands :P
				Map<String, Command> knownCommands = BukkitCommandUtil.getKnownCommandsFromSimpleCommandMap(scm);
				String lowerLabel = bgc.getName().trim().toLowerCase();
				knownCommands.remove(lowerLabel);
			}
			
			if (scm.register(MCore.p.getDescription().getName(), bgc))
			{
				ret = true;
			}
		}
		
		return ret;
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
	// CALL VALIDATION
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
	// HELP AND USAGE INFORMATION
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
	// MESSAGE SENDING HELPERS
	// -------------------------------------------- //
	
	// CONVENIENCE SEND MESSAGE
	
	public boolean sendMessage(String message)
	{
		return Mixin.message(this.sender, message);
	}
	
	public boolean sendMessage(String... messages)
	{
		return Mixin.message(this.sender, messages);
	}
	
	public boolean sendMessage(Collection<String> messages)
	{
		return Mixin.message(this.sender, messages);
	}
	
	// CONVENIENCE MSG
	
	public boolean msg(String msg)
	{
		return Mixin.msg(this.sender, msg);
	}
	
	public boolean msg(String msg, Object... args)
	{
		return Mixin.msg(this.sender, msg, args);
	}
	
	public boolean msg(Collection<String> msgs)
	{
		return Mixin.msg(this.sender, msgs);
	}
	
	// -------------------------------------------- //
	// ARGUMENT READERS
	// -------------------------------------------- //
	
	public String argConcatFrom(int idx)
	{
		if ( ! this.argIsSet(idx)) return null;
		int from = idx;
		int to = args.size();
		if (to <= from) return "";
		return Txt.implode(this.args.subList(from, to), " ");
	}
	
	public boolean argIsSet(int idx)
	{
		if (this.args.size() < idx+1)
		{
			return false;
		}
		return true;
	}
	
	public String arg(int idx)
	{
		if ( ! this.argIsSet(idx)) return null;
		return this.args.get(idx);
	}
	
	public <T> T arg(int idx, ArgReader<T> ar)
	{
		return this.arg(idx, ar, null);
	}
	
	public <T> T arg(int idx, ArgReader<T> argReader, T defaultNotSet)
	{
		String str = this.arg(idx);
		if (str == null) return defaultNotSet;
		ArgResult<T> result = argReader.read(str, this.sender);
		if (result.hasErrors()) this.msg(result.getErrors());
		return result.getResult();
	}
}
