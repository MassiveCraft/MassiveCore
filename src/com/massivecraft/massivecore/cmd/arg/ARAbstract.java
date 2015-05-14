package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.command.CommandSender;

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
		ret = prepareForSpaces(ret, arg);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// PRIVATE: TAB COMPLETE CALCULATIONS
	// -------------------------------------------- //
	
	public static List<String> prepareForSpaces(List<String> suggestions, String arg)
	{
		cleanSuggestions(suggestions);
		
		// This will get the common prefix for all passed in suggestions.
		// This will allow us to tab complete somethings with spaces
		// if we know they all start with the same value,
		// so we don't have to replace all of it.
		final String prefix = getPrefix(suggestions);
		
		// This is all the suggetions without the common prefix.
		List<String> ret = withoutPreAndSuffix(suggestions, prefix);
		// If it isn't empty and there is a prefix...
		if ( ! ret.isEmpty() && ! prefix.isEmpty())
		{
			// ...then we want the first one to have the prefix.
			// That prefix is not removed automatically,
			// due to how tab completion works.
			final String current = ret.get(0);
			String result = prefix;
			if ( ! current.isEmpty())
			{
				if (result.charAt(result.length()-1) != ' ') result += ' ';
				result += current;
			}
			
			int unwantedPrefixLength =  arg.lastIndexOf(' ');
			if (unwantedPrefixLength != -1)
			{
				unwantedPrefixLength++;
				result = result.substring(unwantedPrefixLength);
			}
			ret.set(0, result);
		}
		

		
		return ret;
	}
	
	private static void cleanSuggestions(List<String> suggestions)
	{
		for (ListIterator<String> it = suggestions.listIterator(); it.hasNext();)
		{
			String suggestion = it.next();
			if (suggestion == null) it.remove();
			else if (suggestion.isEmpty()) it.remove();
			else it.set(suggestion.toLowerCase());
		}
	}
	
	private static List<String> withoutPreAndSuffix(List<String> suggestions, String prefix)
	{
		LinkedHashSet<String> ret = new LinkedHashSet<String>(suggestions.size());
		boolean includesPrefix = false; // Sometimes a suggestion is equal to the prefix.
		for (String suggestion : suggestions)
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
			if (suggestion.length() <= prefix.length()) continue;
			int lastSpace = prefix.indexOf(' ', prefix.length());
			int lastIndex = lastSpace != -1 ? lastSpace : suggestion.length();
			ret.add(suggestion.substring(prefix.length(), lastIndex));
		}
		
		return new ArrayList<String>(ret);
	}
	
	private static String getPrefix(List<String> suggestions)
	{
		String prefix = null;
		
		for (String suggestion : suggestions)
		{
			prefix = getOkay(prefix, suggestion);
		}
		
		if (prefix == null) return "";
		int lastSpace = prefix.lastIndexOf(" ");
		if (lastSpace == -1) return "";
		
		return prefix.substring(0, lastSpace+1);
	}
	
	// This method return a new string only including
	// the first characters that are equal.
	private static String getOkay(String original, String compared)
	{
		if (original == null) return compared;
		final int size = Math.min(original.length(), compared.length());
		StringBuilder ret = new StringBuilder();
		
		for (int i = 0; i < size; i++)
		{
			if (Character.toLowerCase(compared.charAt(i)) != Character.toLowerCase(original.charAt(i))) break;
			ret.append(compared.charAt(i));
		}
		
		if (ret.length() == 0) return "";
		
		int lastSpace = ret.lastIndexOf(" ");
		if (lastSpace == -1) return "";
		
		return ret.toString();
	}
	
}
