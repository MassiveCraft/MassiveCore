package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.engine.EngineMassiveCoreCommandRegistration;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// We operate without the leading slash as much as possible.
// We return the command without leading slash.
// If the player supplies a leading slash we assume they mean some WorldEdit double slash first.
// Then we test without if nothing found.
public class TypeStringCommand extends TypeAbstract<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeStringCommand i = new TypeStringCommand();
	public static TypeStringCommand get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	public TypeStringCommand()
	{
		super(String.class);
		this.setVisualColor(ChatColor.AQUA);
	}

	@Override
	public String getName()
	{
		return "command";
	}
	
	@Override
	public String read(String arg, CommandSender sender) throws MassiveException
	{
		// Require base command (something at all).
		if (arg.isEmpty()) throw new MassiveException().addMsg("<b>You must at the very least supply a base command.");
		String[] args = argAsArgs(arg);
		
		// Smart management of first slash ...
		String alias = args[0];
		
		// ... if there is such a command just return ...
		Command command = getCommand(alias);
		if (command != null) return arg;
		
		// ... otherwise if starting with slash return it and hope for the best ...
		if (alias.startsWith("/")) return arg.substring(1);
		
		// ... otherwise it's slashless and we return it as is.
		return arg;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Split the arg into a list of args #inception-the-movie!
		String[] args = argAsArgs(arg);
		
		// Tab completion of base commands
		if (args.length <= 1) return getKnownCommands().keySet();
		
		// Get command alias and subargs
		String alias = args[0];
		
		// Attempt using the tab completion of that command.
		Command command = getCommandSmart(alias);
		if (command == null) return Collections.emptySet();
		List<String> subcompletions = command.tabComplete(sender, alias, Arrays.copyOfRange(args, 1, args.length));
		
		String prefix = Txt.implode(Arrays.copyOfRange(args, 0, args.length-1), " ") + " ";
		List<String> ret = new MassiveList<>();
		
		for (String subcompletion : subcompletions)
		{
			String completion = prefix + subcompletion;
			ret.add(completion);
		}
		
		return ret;
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return false;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String[] argAsArgs(String arg)
	{
		return arg.split(" ", -1);
	}
	
	public static Map<String, Command> getKnownCommands()
	{
		SimpleCommandMap simpleCommandMap = EngineMassiveCoreCommandRegistration.getSimpleCommandMap();
		Map<String, Command> knownCommands = EngineMassiveCoreCommandRegistration.getSimpleCommandMapDotKnownCommands(simpleCommandMap);
		return knownCommands;
	}
	
	public static Command getCommand(String name)
	{
		return getKnownCommands().get(name);
	}
	
	public static Command getCommandSmart(String name)
	{
		Command ret = getCommand(name);
		if (ret != null) return ret;
		
		if ( ! name.startsWith("/")) return null;
		name = name.substring(1);
		return getCommand(name);
	}

}
