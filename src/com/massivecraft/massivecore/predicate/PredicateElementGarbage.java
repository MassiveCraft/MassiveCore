package com.massivecraft.massivecore.predicate;

import java.util.regex.Pattern;

public class PredicateElementGarbage extends PredicateElementRegexes
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PredicateElementGarbage i = new PredicateElementGarbage();
	public static PredicateElementGarbage get() { return i; }
	public PredicateElementGarbage()
	{
		super(
			"^" + Pattern.quote("com.massivecraft.massivelag.inject") + ".*$", // MassiveLag - Injected stuff for commands, tasks and events.
			// TODO: Missing PerWorldPlugins equivalents?
			
			"^" + Pattern.quote("com.massivecraft.massivecore.command.MassiveCoreBukkitCommand.execute") + ".*$", // MassiveCommand - Execution Garbage
			"^" + Pattern.quote("com.massivecraft.massivecore.command.MassiveCommand.execute") + ".*$", // MassiveCommand - Execution Garbage
			
			"^" + Pattern.quote("com.sk89q.bukkit.util.DynamicPluginCommand.execute") + ".*$", // SK89Q
			"^" + Pattern.quote("com.sk89q.worldguard.bukkit.WorldGuardPlugin.onCommand") + ".*$", // SK89Q
			// TODO: Missing WorldEdit equivalents
			"^" + Pattern.quote("com.sk89q.minecraft.util.commands.CommandsManager") + ".*$", // SK89Q
			
			"^" + Pattern.quote("sun.reflect") + ".*$", // Event Reflection
			"^" + Pattern.quote("java.lang.reflect") + ".*$", // Event Reflection
			
			"^" + Pattern.quote("java.security.AccessController") + ".*$", // Event Reflection?
			"^" + Pattern.quote("sun.misc.Unsafe") + ".*$" // Event Reflection?
		);
	}
	
}
