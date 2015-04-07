package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;

public abstract class ARAbstract<T> implements AR<T>
{
	// -------------------------------------------- //
	// OVERRIDE: READ
	// -------------------------------------------- //
	
	@Override
	public T read(CommandSender sender) throws MassiveException
	{
		return this.read(null, sender);
	}

	@Override
	public T read(String arg) throws MassiveException
	{
		return this.read(arg, null);
	}

	@Override
	public T read() throws MassiveException
	{
		return this.read(null, null);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: VALID
	// -------------------------------------------- //
	
	@Override
	public boolean isValid(String arg, CommandSender sender)
	{
		try
		{
			this.read(arg, sender);
			return true;
		}
		catch (MassiveException ex)
		{
			return false;
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE: NAME
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		int prefixLength = "AR".length();
		String name = this.getClass().getSimpleName();
		
		// We don't want the "AR" part
		name = name.substring(prefixLength);
		
		// We split at uppercase letters, because most class names are camel-case.
		final String[] words = name.split("(?=[A-Z])");
		return Txt.implode(words, " ").toLowerCase();
	}
	
	// -------------------------------------------- //
	// OVERRIDE: TAB
	// -------------------------------------------- //

	@Override
	public boolean allowSpaceAfterTab()
	{
		return true;
	}
	
	@Override
	public List<String> getTabListFiltered(CommandSender sender, String arg)
	{
		// Filter them to start with what the user typed in.
		Collection<String> raw = this.getTabList(sender, arg);
		if (raw == null || raw.isEmpty()) return Collections.emptyList();
		
		List<String> ret = Txt.getStartsWithIgnoreCase(raw, arg);
		
		// Here we do a lot of things related to spaces.
		// Because spaces and tab completion desn't go well together.
		// In the future we might be able to do something better,
		// but MineCraft has its limitations.
		ret = prepareForSpaces(ret);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// PRIVATE: TAB COMPLETE CALCULATIONS
	// -------------------------------------------- //
	
	public static List<String> prepareForSpaces(List<String> suggestions)
	{
		List<List<String>> suggestionParts = getParts(suggestions);
		
		// This will get the common prefix for all passed in suggestions.
		// This will allow us to tab complete somethings with spaces
		// if we know they all start with the same value,
		// so we don't have to replace all of it.
		List<String> prefix = getPrefix(suggestionParts);
		// This is all the suggetions without the common prefix.
		List<String> ret = withoutPreAndSuffix(suggestionParts, prefix);
		// If it isn't empty and there is a prefix...
		if ( ! ret.isEmpty() && ! prefix.isEmpty())
		{
			// ...then we want the first one to have the prefix.
			// That prefix is not removed automatically,
			// due to how tab completion works.
			final String current = ret.get(0);
			final String prefixStr = Txt.implode(prefix, " ");
			String result = prefixStr;
			if ( ! current.isEmpty()) result += " " + current;
			
			ret.set(0, result);
		}
		
		return ret;
	}
	
	// This things splits up the arguments at spaces.
	private static List<List<String>> getParts(List<String> list)
	{
		List<List<String>> ret = Lists.newArrayList();
		
		for (String str : list)
		{
			if (str == null) continue;
			if (str.isEmpty()) continue;
			ret.add(Arrays.asList(str.split("\\s+")));
		}
		
		return ret;
	}

	private static List<String> withoutPreAndSuffix(List<List<String>> suggestionParts, List<String> prefix)
	{
		List<String> ret = new ArrayList<String>(suggestionParts.size());
		boolean includesPrefix = false; // Sometimes a suggestion is equal to the prefix.
		for (List<String> suggestion : suggestionParts)
		{
			if (suggestion.equals(prefix) && !includesPrefix)
			{
				ret.add("");
				includesPrefix = true;
				continue;
			}
			// We remove the prefix because we only want that once.
			// But we can't keep things after the first part either
			// because of spaces and stuff.
			if (suggestion.size() <= prefix.size()) continue;
			ret.add(suggestion.get(prefix.size()));
		}
		
		return ret;
	}
	
	private static List<String> getPrefix(List<List<String>> suggestionParts)
	{
		List<String> prefix = null;
		
		for (List<String> suggestion : suggestionParts)
		{
			prefix = getOkay(prefix, suggestion);
		}
		
		return prefix;
	}
	
	// This method return a new array only including
	// the first parts that are equal.
	private static List<String> getOkay(List<String> original, List<String> compared)
	{
		if (original == null) return compared;
		
		final int size = Math.min(original.size(), compared.size());
		List<String> ret = new ArrayList<String>(size);
		
		for (int i = 0; i < size; i++)
		{
			if (compared.get(i) == null || original.get(i) == null) break;
			if ( ! compared.get(i).equals(original.get(i))) break;
			ret.add(original.get(i));
		}

		return ret;
	}
	
}
