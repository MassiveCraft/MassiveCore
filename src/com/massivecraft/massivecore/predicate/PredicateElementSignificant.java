package com.massivecraft.massivecore.predicate;

import java.util.regex.Pattern;

public class PredicateElementSignificant extends PredicateElementRegexes
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PredicateElementSignificant i = new PredicateElementSignificant();
	public static PredicateElementSignificant get() { return i; }
	public PredicateElementSignificant()
	{
		super(
			// Command
			"^" + Pattern.quote("org.bukkit.craftbukkit.") + ".*" +Pattern.quote(".CraftServer.dispatchCommand") + ".*$", // CraftBukkit
		
			// Task
			"^" + Pattern.quote("org.bukkit.craftbukkit.") + ".*" +Pattern.quote(".scheduler.CraftTask.run") + ".*$", // CraftBukkit
			
			// Event
			"^" + Pattern.quote("org.bukkit.plugin.RegisteredListener.callEvent") + ".*$", // Bukkit
			"^" + Pattern.quote("us.Myles.PWP.TransparentListeners") + ".*$", // PerWorldPlugins <= 1.2.4
			"^" + Pattern.quote("me.incomprehendable.dev.pwp.transparentlisteners") + ".*$" // PerWorldPlugins >= 1.2.5
		);
	}
	
}
