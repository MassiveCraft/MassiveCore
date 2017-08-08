package com.massivecraft.massivecore.predicate;

import com.massivecraft.massivecore.Integration;

/**
 * This class is supposed to be used together with the class {@link Integration}.
 * This is a predicate that simply checks whether the MassiveCraft Factions plugin is installed.
 *
 * Why not just check for the plugin name "Factions"?
 * Because FactionsUUID and other forks exists and use our name.
 * So with that in mind we must check it's actually the real Factions that we support.
 */
public class PredicateIntegrationFactions extends PredicateIntegration
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PredicateIntegrationFactions i = new PredicateIntegrationFactions();
	public static PredicateIntegrationFactions get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE > PREDICATE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(Integration type)
	{
		return isPluginNamePresent("Factions") && isClassNamePresent("com.massivecraft.factions.engine.EnginePower");
	}
	
}
