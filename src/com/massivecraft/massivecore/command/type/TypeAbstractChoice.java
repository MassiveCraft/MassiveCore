package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.collections.MassiveTreeSet;
import com.massivecraft.massivecore.command.type.container.AllAble;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class TypeAbstractChoice<T> extends TypeAbstract<T> implements AllAble<T>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static Mson SUGGEST_FORMAT = Mson.parse("<h>%s");
	public final static Mson SUGGEST_COMMMA = Mson.parse(" <silver>| ");
	public final static Mson SUGGEST_AND = Mson.parse(" <silver>| ");
	public final static Mson SUGGEST_DOT = Mson.parse("");
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected int listCountMax = 50;
	public int getListCountMax() { return this.listCountMax; }
	public TypeAbstractChoice<T> setListCountMax(int listCountMax) { this.listCountMax = listCountMax; return this; }
	
	protected String help = null;
	public String getHelp() { return this.help; }
	public TypeAbstractChoice<T> setHelp(String help) { this.help = help; return this; }
	
	protected boolean canSeeOverridden = calcCanSeeOverriden();
	public boolean isCanSeeOverridden() { return this.canSeeOverridden; }
	public void setCanSeeOverridden(boolean canSeeOverridden) { this.canSeeOverridden = canSeeOverridden; }
	public boolean calcCanSeeOverriden()
	{
		return ! TypeAbstractChoice.class.equals(ReflectionUtil.getSuperclassDeclaringMethod(this.getClass(), true, "canSee"));
	}
	
	// -------------------------------------------- //
	// FIELDS: CACHE
	// -------------------------------------------- //
	
	// All: You should either setAll or override getAll.
	protected Collection<T> all = null;
	public Collection<T> getAll() { return all; }
	public void setAll(Collection<T> all)
	{
		if (all != null) all = Collections.unmodifiableCollection(new MassiveList<>(all));
		this.all = all;
		
		if (all == null)
		{
			this.options = null;
			this.tabs = null;
		}
		else if ( ! this.isCanSeeOverridden())
		{
			// The "all" cache is set and canSee is not overriden.
			// This means we can cache options and tabs.
			this.options = this.createOptions(all);
			this.tabs = this.createTabs((CommandSender)null);
		}
	}
	@SafeVarargs
	public final void setAll(T... all)
	{
		this.setAll(Arrays.asList(all));
	}
	
	// Options
	protected Map<String, T> options = null;
	public Map<String, T> getOptions() { return options; }
	public void setOptions(Map<String, T> options) { this.options = options; }
	
	// Tabs
	protected Collection<String> tabs = null;
	public Collection<String> getTabs() { return this.tabs; }
	public void setTabs(Collection<String> tabs) { this.tabs = tabs; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeAbstractChoice(Class<?> clazz)
	{
		super(clazz);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: TYPE
	// -------------------------------------------- //
	
	protected static final String MESSAGE_MATCH_NOTHING = Txt.parse("<b>No %s matches \"<h>%s<b>\".");
	protected static final String MESSAGE_MATCH_AMBIGUOUS = Txt.parse("<b>%d %ss matches \"<h>%s<b>\".");
	protected static final String MESSAGE_AVAILABLE_EMPTY = Txt.parse("<i>Note: There is no %s available.");
	
	protected static final String MESSAGE_COLON_AMBIGUOUS = Txt.parse("<aqua>Ambigous<silver>: ");
	protected static final String MESSAGE_COLON_ALL = Txt.parse("<aqua>All<silver>: ");
	protected static final String MESSAGE_COLON_SIMILAR = Txt.parse("<aqua>Similar<silver>: ");
	
	protected static final String MESSAGE_SUGGESTIONS_EMPTY = Txt.parse("<i>No suggestions found.");
	protected static final String MESSAGE_SUGGESTIONS_MUCH = Txt.parse("<i>Over %d suggestions found (hiding output).");
	
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		// NPE Evade
		if (arg == null) return null;
		
		// Exact
		T exact = this.getExactMatch(arg);
		if (exact != null) return exact;
		
		// Get All
		Collection<T> all = this.getAll(sender);
		
		// Get Options
		Map<String, T> options = this.getOptions();
		if (options == null) options = this.createOptions(all);
		
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
		String message;
		if (matches.isEmpty())
		{
			message = String.format(MESSAGE_MATCH_NOTHING, this.getName(), arg);
			exception.addMessage(message);
			suggestLevenshtein = true;
		}
		// Ambiguous
		else
		{
			message = String.format(MESSAGE_MATCH_AMBIGUOUS, matches.size(), this.getName(), arg);
			exception.addMessage(message);
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
			message = String.format(MESSAGE_AVAILABLE_EMPTY, this.getName());
			exception.addMessage(message);
		}
		else
		{
			Collection<T> suggestions = null;
			Mson format = SUGGEST_FORMAT;
			Mson comma = SUGGEST_COMMMA;
			Mson and = SUGGEST_AND;
			Mson dot = SUGGEST_DOT;
			
			if (suggestAmbiguous)
			{
				suggestions = matches;
				message = MESSAGE_COLON_AMBIGUOUS;
			}
			else if (suggestAll)
			{
				suggestions = all;
				message = MESSAGE_COLON_ALL;
			}
			else if (suggestLevenshtein)
			{
				suggestions = this.getMatches(options, arg, true);
				message = MESSAGE_COLON_SIMILAR;
			}
			
			if (suggestions.isEmpty())
			{
				exception.addMessage(MESSAGE_SUGGESTIONS_EMPTY);
			}
			else if (suggestions.size() > this.getListCountMax())
			{
				message = String.format(MESSAGE_SUGGESTIONS_MUCH, this.getListCountMax());
				exception.addMessage(message);
			}
			else
			{
				List<Mson> visuals = new MassiveList<>();
				for (T value : suggestions)
				{
					visuals.add(this.getVisualMson(value, sender));
				}
				exception.addMessage(Mson.mson(message, Mson.implodeCommaAndDot(visuals, format, comma, and, dot)));
			}
		}
		
		// Help
		String help = this.getHelp();
		if (help != null) exception.addMessage(help);
		
		throw exception;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ALL ABLE
	// -------------------------------------------- //
	
	@Override
	public Collection<T> getAll(CommandSender sender)
	{
		// No Can See Override?
		if ( ! this.isCanSeeOverridden()) return this.getAll();
		
		// Create
		Set<T> ret = new MassiveSet<>();
		
		// Fill
		for (T value : this.getAll())
		{
			if ( ! this.canSee(value, sender)) continue;
			ret.add(value);
		}
		
		// Return
		return ret;
	}
	
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
	
	public List<T> getMatches(Map<String, T> options, String arg, boolean levenshtein)
	{
		// Create
		List<T> ret = new MassiveList<>();
		
		// Prepare
		arg = this.prepareOptionKey(arg);
		
		// Exact
		T exact = options.get(arg);
		if (exact != null) return Collections.singletonList(exact);
		
		// Fill
		for (Entry<String, T> entry : options.entrySet())
		{
			String key = entry.getKey();
			T value = entry.getValue();
			
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
	
	public Map<String, T> createOptions(Iterable<T> all)
	{
		// Create
		Map<String, T> ret = new MassiveMap<>();
		
		// Fill
		for (T value : all)
		{
			for (String key : this.createOptionKeys(value))
			{
				ret.put(key, value);
			}
		}
		
		// Return
		return ret;
	}
	
	// This method creates keys for a certain value.
	// They ARE comparable.
	public List<String> createOptionKeys(T value)
	{
		// Create
		List<String> ret = new MassiveList<>();
		
		// Fill
		String string;
		
		for (String name : this.getNames(value))
		{
			string = this.prepareOptionKey(name);
			if (string != null) ret.add(string);
		}
		
		for (String id : this.getIds(value))
		{
			string = this.prepareOptionKey(id);
			if (string != null) ret.add(string);
		}
		
		// Return
		return ret;
	}
	
	// The purpose of this method is to strip down a string to a comparable string key.
	protected static Pattern PATTERN_KEY_UNWANTED = Pattern.compile("[_\\-\\s]+");
	public String prepareOptionKey(String string)
	{
		if (string == null) return null;
		string = string.trim();
		string = string.toLowerCase();
		// SLOW: string = string.replaceAll("[_\\-\\s]+", "");
		string = PATTERN_KEY_UNWANTED.matcher(string).replaceAll("");
		return string;
	}

	// -------------------------------------------- //
	// TAB
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Collection<String> ret = this.getTabs();
		if (ret == null) ret = this.createTabs(sender);
		return ret;
	}
	
	public Set<String> createTabs(CommandSender sender)
	{
		// Create
		Set<String> ret = new MassiveSet<>();
		
		// Fill
		for (T value : this.getAll(sender))
		{
			ret.addAll(this.createTabs(value));
		}
		
		// Return
		return ret;
	}
	
	public Set<String> createTabs(T value)
	{
		// Create
		Set<String> ret = new MassiveTreeSet<String, ComparatorCaseInsensitive>(ComparatorCaseInsensitive.get());
		
		// Fill
		String string;
		
		for (String name : this.getNames(value))
		{
			string = this.prepareTab(name, true);
			if (string != null) ret.add(string);
			
			string = this.prepareTab(name, false);
			if (string != null) ret.add(string);
		}
		
		for (String id : this.getIds(value))
		{
			string = this.prepareTab(id, true);
			if (string != null) ret.add(string);
			
			string = this.prepareTab(id, false);
			if (string != null) ret.add(string);
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
