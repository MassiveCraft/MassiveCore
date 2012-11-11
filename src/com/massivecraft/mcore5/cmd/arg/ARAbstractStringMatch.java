package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.mcore5.util.Txt;

public abstract class ARAbstractStringMatch implements ArgReader<String>
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public ArgResult<String> read(String str, MCommand mcommand)
	{
		ArgResult<String> result = new ArgResult<String>();
		
		// Find all matches		
		Set<String> matches = new HashSet<String>();
		String perfectMatch = null;
		
		outerloop:
		for (Collection<String> altColl : this.altColls())
		{
			for (String alt : altColl)
			{
				Integer matchDistance = this.matches(str, alt);
				if (matchDistance == null) continue;
				if (matchDistance == 0)
				{
					perfectMatch = alt;
					break outerloop;
				}
				else
				{
					matches.add(alt);
				}
			}
		}
		
		// Set result and errors
		if (perfectMatch != null)
		{
			result.setResult(perfectMatch);
		}
		else if (matches.size() == 1)
		{
			result.setResult(matches.iterator().next());
		}
		else if (matches.size() > 1)
		{
			result.getErrors().add("<b>"+Txt.upperCaseFirst(this.typename())+" matching \"<h>"+str+"<b>\" is ambigious.");
			result.getErrors().add("<b>Did you mean "+Txt.implodeCommaAndDot(matches, "<h>%s", "<b>, ", " <b>or ", "<b>?"));
		}
		else if (matches.size() == 0)
		{
			result.getErrors().add("<b>No "+this.typename()+" matching \"<h>"+str+"<b>\".");
		}
		
		return result;
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	// return null if no match
	// return 0 if perfect match
	// return >0 to declare distance from perfect match
	public abstract Integer matches(String arg, String alt);
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected String typename;
	public String typename() { return this.typename; }
	
	protected Collection<Collection<String>> altColls;
	public Collection<Collection<String>> altColls() { return this.altColls; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARAbstractStringMatch(String typename, Collection<Collection<String>> altColls)
	{
		this.typename = typename;
		this.altColls = altColls;
	}
}
