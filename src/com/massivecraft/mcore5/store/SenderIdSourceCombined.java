package com.massivecraft.mcore5.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class SenderIdSourceCombined implements SenderIdSource
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final List<SenderIdSource> sources;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public SenderIdSourceCombined(SenderIdSource... sources)
	{
		this.sources = new ArrayList<SenderIdSource>(Arrays.asList(sources));
	}
	
	public SenderIdSourceCombined(Collection<SenderIdSource> sources)
	{
		this.sources = new ArrayList<SenderIdSource>(sources);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getSenderIds()
	{
		TreeSet<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (SenderIdSource source : this.sources)
		{
			ret.addAll(source.getSenderIds());
		}
		return ret;
	}

}
