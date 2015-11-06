package com.massivecraft.massivecore.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public class MassiveCoreBukkitCommand extends Command implements PluginIdentifiableCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final MassiveCommand massiveCommand;
	public MassiveCommand getMassiveCommand() { return this.massiveCommand; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MassiveCoreBukkitCommand(String name, MassiveCommand massiveCommand)
	{
		super(
			name,
			massiveCommand.getDesc(),
			massiveCommand.getTemplate().toPlain(true),
			Collections.<String>emptyList() // We don't use aliases
		);
		this.massiveCommand = massiveCommand;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: PLUGIN IDENTIFIABLE COMMAND
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return this.getMassiveCommand().getRegisteredPlugin();
	}
	
	// -------------------------------------------- //
	// OVERRIDE: EXECUTE
	// -------------------------------------------- //
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args)
	{
		List<String> argList = this.createArgList(args);
		this.getMassiveCommand().execute(sender, argList);
		return true;
	}
	
	public List<String> createArgList(String[] args)
	{
		List<String> ret;
		if (this.getMassiveCommand().isTokenizing())
		{
			ret = Txt.tokenizeArguments(Txt.implode(args, " "));
		}
		else
		{
			ret = MUtil.list(args);
		}
		
		if (this.getMassiveCommand().isUnsmart())
		{
			List<String> oldArgList = ret;
			ret = new ArrayList<String>(oldArgList.size());
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
	
	// NOTE: There is some Vanilla bugs, described here.
	
	// Test 1. These bugs occur when using commands provided by Bukkit plugins.
	// Test 2. These bugs do also occur when using BungeeCoord commands.
	// BungeeCoord commands are handled MUCH differently than Bukkit commands.
	// In fact BungeeCoord commands are not related to Bukkit at all.
	// Test 3. These bugs do also occur in plain singleplayer vanilla MineCraft.
	
	// These notes suggests that this is a client side bug and NOT a server side one.
	
	// BUG 1. Tab complete to first common prefix then normal.
	// Desc: Tab completes to the first common prefix of the available completions
	// after that it will tab complete normally.
	// Happens when:
	//	1. All possible suggestions has the same common prefix.
	//	2. The common prefix must be of at least two characters,
	//	3. There is more than one suggestion.
	//	4. Tab completing from the end of the chat bar. (There is only text to the left)
	//	5. The user typed in the beginning of the arg to tab complete.
	
	// BUG 2. Tab complete to first common prefix then nothing.
	// Desc: Tab completes to the first common prefix of the available completions
	// after that it will refuse to tab complete anymore.
	// Happens when:
	//	1. All possible suggestions has the same common prefix.
	//	3. There is more than one suggestion.
	//	4. Tab completing from the middle of the chat bar. (There text on both sides)
	//	5. The user typed in the beginning of the arg to tab complete.
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] rawArgs) throws IllegalArgumentException
	{
		// The JavaDocs for Command says these checks will be made.
		// So we should follow that contract.
		if (sender == null) throw new IllegalArgumentException("sender must not be null");
		if (rawArgs == null) throw new IllegalArgumentException("args must not be null");
		if (alias == null) throw new IllegalArgumentException("args must not be null");
		
		List<String> args = new MassiveList<String>();
		
		// When several spaces are next to each other, empty elements in the array will occur.
		// To avoid such whitespace we do the following
		// NOTE: The last arg can be empty, and will be in many cases.	
		for (int i = 0; i < rawArgs.length-1; i++ )
		{
			String str = rawArgs[i];
			if (str == null) continue;
			if (str.isEmpty()) continue;
			args.add(str);
		}
		// Here we add the last element.
		args.add(rawArgs[rawArgs.length-1]);
		
		List<String> ret = this.getMassiveCommand().getTabCompletions(args, sender);
		
		int retSize = ret.size();
		int maxSize = MassiveCoreMConf.get().maxTabCompletions;
		if (maxSize > 0 && retSize > maxSize)
		{
			Mixin.msgOne(sender, Lang.COMMAND_TOO_MANY_TAB_SUGGESTIONS, retSize);
			return Collections.emptyList();
		}
		
		return ret;
	}
	
}
