package com.massivecraft.massivecore.predicate;

import com.massivecraft.massivecore.Integration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class PredicateIntegration implements Predicate<Integration>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PredicateIntegration i = new PredicateIntegration();
	public static PredicateIntegration get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(Integration integration)
	{
		return isPluginNamesPresent(integration.getPluginNames()) && isClassNamesPresent(integration.getClassNames());
	}
	
	// -------------------------------------------- //
	// PLUGINS
	// -------------------------------------------- //
	
	public static boolean isPluginNamesPresent(Collection<String> pluginNames)
	{
		for (String pluginName : pluginNames)
		{
			if (isPluginNamePresent(pluginName)) continue;
			return false;
		}
		return true;
	}
	
	public static boolean isPluginNamePresent(String pluginName)
	{
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null) return false;
		return plugin.isEnabled();
	}
	
	// -------------------------------------------- //
	// CLASSES
	// -------------------------------------------- //
	
	public static boolean isClassNamesPresent(Collection<String> classNames)
	{
		for (String className : classNames)
		{
			if (isClassNamePresent(className)) continue;
			return false;
		}
		return true;
	}
	
	public static boolean isClassNamePresent(String className)
	{
		try
		{
			Class.forName(className);
			return true;
		}
		catch (Throwable t)
		{
			return false;
		}
	}
	
}
