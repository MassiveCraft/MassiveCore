package com.massivecraft.massivecore.command.type;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.CaseInsensitiveComparator;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.collections.MassiveTreeSet;
import com.massivecraft.massivecore.command.type.collection.AllAble;
import com.massivecraft.massivecore.util.Txt;

public abstract class TypeAbstractChoice<T> extends TypeAbstract<T> implements AllAble<T>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static String SUGGEST_FORMAT = Txt.parse("<h>%s");
	public final static String SUGGEST_COMMMA = Txt.parse(" <silver>| ");
	public final static String SUGGEST_AND = Txt.parse(" <silver>| ");
	public final static String SUGGEST_DOT = Txt.parse("");
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected int listCountMax = 50;
	public int getListCountMax() { return this.listCountMax; }
	public TypeAbstractChoice<T> setListCountMax(int listCountMax) { this.listCountMax = listCountMax; return this; }
	
	protected String help = null;
	public String getHelp() { return this.help; }
	public TypeAbstractChoice<T> setHelp(String help) { this.help = help; return this; }
	
	// -------------------------------------------- //
	// OVERRIDE: TYPE
	// -------------------------------------------- //
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		// NPE Evade
		if (arg == null) return null;
		
		// Get All
		Collection<T> all = this.getAll(sender);
		
		// Get Options
		Map<String, T> options = this.getOptions(all);
		
		// Get Matches
		List<T> matches = this.getMatches(options, arg, false);
		
		// Exact
		if (matches.size() == 1) return matches.get(0);
		
		// Exception
		MassiveException exception = new MassiveException();
		
		// Suggestions
		boolean suggestNone = false;
		boolean suggestAmbiguous = false;
		boolean suggestAll = false;
		boolean suggestLevenshtein = false;
		
		// Nothing Found
		if (matches.isEmpty())
		{
			exception.addMsg("<b>No %s matches \"<h>%s<b>\".", this.getTypeName(), arg);
			suggestLevenshtein = true;
		}
		// Ambiguous
		else
		{
			exception.addMsg("<b>%d %ss matches \"<h>%s<b>\".", matches.size(), this.getTypeName(), arg);
			suggestAmbiguous = true;
		}
		
		// Suggest
		if (all.isEmpty()) suggestNone = true;
		if (all.size() <= this.getListCountMax()) suggestAll = true;
		
		if ( ! this.canList(sender))
		{
			
		}
		else if (suggestNone)
		{
			exception.addMsg("<i>Note: There is no %s available.", this.getTypeName());
		}
		else
		{
			Collection<T> suggestions = null;
			String msg = null;
			String format = SUGGEST_FORMAT;
			String comma = SUGGEST_COMMMA;
			String and = SUGGEST_AND;
			String dot = SUGGEST_DOT;
			
			if (suggestAmbiguous)
			{
				suggestions = matches;
				msg = "<aqua>Ambigous<silver>: %s";
			}
			else if (suggestAll)
			{
				suggestions = all;
				msg = "<aqua>All<silver>: %s";
			}
			else if (suggestLevenshtein)
			{
				suggestions = this.getMatches(options, arg, true);;
				msg = "<aqua>Similar<silver>: %s";
			}
			
			if (suggestions.isEmpty())
			{
				exception.addMsg("<i>No suggestions found.");
			}
			else if (suggestions.size() > this.getListCountMax())
			{
				exception.addMsg("<i>Over %d suggestions found (hiding output).", this.getListCountMax());
			}
			else
			{
				List<String> visuals = new MassiveList<String>();
				for (T value : suggestions)
				{
					visuals.add(this.getVisual(value, sender));
				}
				exception.addMsg(msg, Txt.implodeCommaAndDot(visuals, format, comma, and, dot));
			}
		}
		
		// Help
		String help = this.getHelp();
		if (help != null) exception.addMsg(help);
		
		throw exception;
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	// -------------------------------------------- //
	// OVERRIDE: ALL ABLE
	// -------------------------------------------- //
	
	@Override
	public Collection<T> getAll(CommandSender sender)
	{
		// Create
		Set<T> ret = new MassiveSet<T>();
		
		// Fill
		for (T value : this.getAll())
		{
			if ( ! this.canSee(value, sender)) continue;
			ret.add(value);
		}
		
		// Return
		return ret;
	}
	
	public abstract Collection<T> getAll();
	
	public boolean canList(CommandSender sender)
	{
		return true;
	}
	
	public boolean canSee(T value, CommandSender sender)
	{
		return true;
	}	
	
	// -------------------------------------------- //
	// MATCHES
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public List<T> getMatches(Map<String, T> options, String arg, boolean levenshtein)
	{
		// Exact
		T exact = this.getExactMatch(arg);
		if (exact != null) return new MassiveList<T>(exact);
		
		// Create
		List<T> ret = new MassiveList<T>();
		
		// Prepare
		arg = this.prepareKey(arg);
		
		// Fill
		for (Entry<String, T> entry : options.entrySet())
		{
			String key = entry.getKey();
			T value = entry.getValue();
			
			if (arg.equals(key)) return new MassiveList<T>(value);
			
			if (levenshtein)
			{
				if ( ! isLevenshteinSimilar(arg, key)) continue;
			}
			else
			{
				if ( ! key.startsWith(arg)) continue;
			}
			
			ret.add(value);
		}
		
		// Return
		return ret;
	}
	
	// Can be overridden to make use of existing indexes.
	public T getExactMatch(String arg)
	{
		return null;
	}
	
	public boolean isLevenshteinSimilar(String arg, String string)
	{
		int max = this.getLevenshteinMax(arg);
		return StringUtils.getLevenshteinDistance(arg, string) <= max;
	}
	
	public int getLevenshteinMax(String arg)
	{
		if (arg == null) return 0; // For some apparent reason this is required.
		if (arg.length() <= 1) return 0; // When dealing with 1 character aliases, there is way too many options.
		if (arg.length() <= 7) return 1; // 1 is default.
		
		return 2;  // If it were 8 characters or more, we end up here. Because many characters allow for more typos.
	}
	
	// -------------------------------------------- //
	// OPTIONS
	// -------------------------------------------- //
	
	public Map<String, T> getOptions(Iterable<T> all)
	{
		// Create
		Map<String, T> ret = new MassiveMap<String, T>();
		
		// Fill
		for (T value : all)
		{
			for (String key : this.getKeys(value))
			{
				ret.put(key, value);
			}
		}
		
		// Return
		return ret;
	}
	
	// This method creates keys for a certain value.
	// They are not comparable.
	public List<String> getKeys(T value)
	{
		// Create
		List<String> ret = new MassiveList<String>();
		
		// Fill
		for (String name : this.getNames(value))
		{
			ret.add(this.prepareKey(name));
		}
		
		for (String id : this.getIds(value))
		{
			ret.add(this.prepareKey(id));
		}
		
		// Return
		return ret;
	}
	
	// The purpose of this method is to strip down a string to a comparable string key.
	public String prepareKey(String string)
	{
		if (string == null) return null;
		string = string.trim();
		string = string.toLowerCase();
		string = string.replaceAll("[_\\-\\s]+", "");
		return string;
	}

	// -------------------------------------------- //
	// TAB
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Create
		Set<String> ret = new MassiveSet<String>();
		
		// Fill
		for (T value : this.getAll(sender))
		{
			ret.addAll(this.getTabs(value));
		}
		
		// Return
		return ret;
	}
	
	public Set<String> getTabs(T value)
	{
		// Create
		Set<String> ret = new MassiveTreeSet<String, CaseInsensitiveComparator>(CaseInsensitiveComparator.get());
		
		// Fill
		for (String name : this.getNames(value))
		{
			ret.add(this.prepareTab(name, true));
			ret.add(this.prepareTab(name, false));
		}
		
		for (String id : this.getIds(value))
		{
			ret.add(this.prepareTab(id, true));
			ret.add(this.prepareTab(id, false));
		}
		
		// Return
		return ret;
	}
	
	public String prepareTab(String string, boolean spaces)
	{
		if (string == null) return null;
		string = string.trim();
		if ( ! spaces) string = string.replace(" ", "");
		return string;
	}

}
