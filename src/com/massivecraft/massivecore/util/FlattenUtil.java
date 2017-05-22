package com.massivecraft.massivecore.util;

import com.massivecraft.massivecore.collections.MassiveList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlattenUtil
{
	// Did you offer the client varargs?
	// Worried about getting an array of a single list instead of an array of the list's contents?
	// Throw it in here, we'll flatten it out for you and give you back a nice, nonnull, list.
	// NOTE: This should never return null
	// No matter what we put it, it should give us a List back
	public static List<Object> flatten(Object packed)
	{
		// Null Check
		if (packed == null) return Collections.singletonList(null);
		
		// Is it iterable?
		if (packed instanceof Iterable)
		{
			// Create
			List<Object> ret = new MassiveList<>();
			
			// Fill
			for (Object item : (Iterable<Object>)packed)
			{
				// Recurse
				ret.addAll(flatten(item));
			}
			
			// Return
			return ret;
		}
		
		// Is it an array?
		if (packed.getClass().isArray())
		{
			// Send it through again as a list
			List<Object> asList = Arrays.asList((Object[])packed);
			return flatten(asList);
		}
		
		// Doesn't seem to hold elements we care about, just return it in a list
		return Collections.singletonList(packed);
	}
	
}
