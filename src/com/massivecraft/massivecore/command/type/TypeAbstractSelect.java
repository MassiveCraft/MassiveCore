package com.massivecraft.massivecore.command.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.AllAble;
import com.massivecraft.massivecore.util.Txt;

public abstract class TypeAbstractSelect<T> extends TypeAbstract<T> implements AllAble<T>
{
	// -------------------------------------------- //
	// CONSTANT
	// -------------------------------------------- //
	
	public static final int LIST_COUNT_MAX = 50;
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T select(String str, CommandSender sender) throws MassiveException;
	
	public boolean canList(CommandSender sender) { return true; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		T result = this.select(arg, sender);
		if (result != null) return result;
		
		MassiveException exception = createExceptionForInvalidArg(arg, sender);
		throw exception;
	}
	
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> names = new ArrayList<String>();
		for (T value : this.getAll(sender))
		{
			String name = this.getVisual(value, sender);
			names.add(name);
		}
		return names;
	}
	
	public MassiveException createExceptionForInvalidArg(String arg, CommandSender sender)
	{
		MassiveException ret = new MassiveException();
		ret.addMsg("<b>No %s matches \"<h>%s<b>\".", this.getTypeName(), arg);
		
		if (this.canList(sender))
		{			
			Collection<String> names = this.altNames(sender);
			
			// Try Levenshtein
			List<String> matches = this.getMatchingAltNames(arg, sender, this.getMaxLevenshteinDistanceForArg(arg));
			
			if (names.isEmpty())
			{
				ret.addMsg("<i>Note: There is no %s available.", this.getTypeName());
			}
			else if ( ! matches.isEmpty() && matches.size() <= LIST_COUNT_MAX)
			{
				String format = Txt.parse("<h>%s");
				String comma = Txt.parse("<i>, ");
				String and = Txt.parse(" <i>or ");
				String dot = Txt.parse("<i>?");
				ret.addMsg("<i>Did you mean %s", Txt.implodeCommaAndDot(matches, format, comma, and, dot));
			}
			else if (names.size() > LIST_COUNT_MAX)
			{
				ret.addMsg("<i>More than %d alternatives available.", LIST_COUNT_MAX);
			}
			else
			{
				String format = Txt.parse("<h>%s");
				String comma = Txt.parse("<i>, ");
				String and = Txt.parse(" <i>or ");
				String dot = Txt.parse("<i>.");
				ret.addMsg("<i>Use %s", Txt.implodeCommaAndDot(names, format, comma, and, dot));
			}
		}
		return ret;
	}
	
	public List<String> getMatchingAltNames(String arg, CommandSender sender, int maxLevenshteinDistance)
	{
		if (arg == null) return Collections.emptyList(); // For some apparent reason this is required.
		arg = arg.toLowerCase();
		
		// Try Levenshtein
		List<String> matches = new ArrayList<String>();
		
		for (String alias : this.altNames(sender))
		{
			String aliaslc = alias.toLowerCase();
			int distance = StringUtils.getLevenshteinDistance(arg, aliaslc);
			if (distance > maxLevenshteinDistance) continue;
			matches.add(alias);
		}
		return matches;
	}
	
	public int getMaxLevenshteinDistanceForArg(String arg)
	{
		if (arg == null) return 0; // For some apparent reason this is required.
		if (arg.length() <= 1) return 0; // When dealing with 1 character aliases, there is way too many options.
		if (arg.length() <= 7) return 1; // 1 is default.
		
		return 2;  // If it were 8 characters or more, we end up here. Because many characters allow for more typos.
	}
	
}
