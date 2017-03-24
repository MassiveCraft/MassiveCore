package com.massivecraft.massivecore.predicate;

import com.massivecraft.massivecore.collections.MassiveList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class PredicateElementRegexes implements Predicate<StackTraceElement>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private List<Pattern> patterns = Collections.emptyList();
	public List<Pattern> getPatterns() { return this.patterns; }
	public void setPatterns(Collection<Pattern> patterns) { this.patterns = new MassiveList<>(patterns); }
	public void setPatterns(Iterable<String> regexes) { this.setPatterns(asPatterns(regexes));}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PredicateElementRegexes(String... regexes)
	{
		this.setPatterns(Arrays.asList(regexes));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(StackTraceElement element)
	{
		if (element == null) return false;
		String string = element.toString();
		for (Pattern pattern : this.getPatterns())
		{
			if (pattern.matcher(string).matches()) return true;
		}
		return false;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	protected List<Pattern> asPatterns(Iterable<String> regexes)
	{
		// Create
		List<Pattern> ret = new MassiveList<>();
		
		// Fill
		for (String regex : regexes)
		{
			Pattern pattern = Pattern.compile(regex);
			ret.add(pattern);
		}
		
		// Return
		return ret;
	}
	
}
