package com.massivecraft.mcore.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.Txt;

public class MCoreBukkitCommand extends Command
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final MCommand mcommand;
	public MCommand getMcommand() { return this.mcommand; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCoreBukkitCommand(MCommand mcommand)
	{
		super(
			mcommand.getClass().getSimpleName(), // The name field is final. MCommand aliases/names are not final so we simply use the class name.
			null, // Set description to null. Instead we override the getter.
			null, // Set usage to null. Instead we override the getter.
			new ArrayList<String>() // Set aliases to "null". Instead we override the getter.
		);
		
		this.mcommand = mcommand;
	}

	// -------------------------------------------- //
	// OVERRIDE: GETTERS
	// -------------------------------------------- //
	
	@Override
	public String getDescription()
	{
		return this.getMcommand().getDesc();
	}
	
	@Override
	public String getUsage()
	{
		return this.getMcommand().getUseageTemplate();
	}
	
	@Override
	public List<String> getAliases()
	{
		return this.getMcommand().getAliases();
	}
	
	@Override
	public String getLabel()
	{
		return this.getMcommand().getAliases().get(0);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: EXECUTE
	// -------------------------------------------- //
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args)
	{
		List<String> argList;
		if (this.mcommand.isUsingTokenizer())
		{
			argList = Txt.tokenizeArguments(Txt.implode(args, " "));
		}
		else
		{
			argList = new ArrayList<String>(Arrays.asList(args));
		}
		
		if (this.mcommand.isUsingSmartQuotesRemoval())
		{
			List<String> oldArgList = argList;
			argList = new ArrayList<String>();
			for (String arg : oldArgList)
			{
				argList.add(Txt.removeSmartQuotes(arg));
			}
		}
		
		this.mcommand.execute(sender, argList);
		
		return true;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: TAB COMPLETE
	// -------------------------------------------- //
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException
	{
		List<String> superRet = super.tabComplete(sender, alias, args);
		if (args.length == 0) return superRet;
		
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		ret.addAll(superRet);
		
		String tokenlc = args[args.length - 1].toLowerCase();
		
		// Add ids of all online senders that match and isn't added yet. 
		for (String senderId : Mixin.getOnlineSenderIds())
		{
			if (!senderId.toLowerCase().startsWith(tokenlc)) continue;
			if (!Mixin.canSee(sender, senderId)) continue;
			ret.add(senderId);
		}
		
		return new ArrayList<String>(ret);
	}
	
}
