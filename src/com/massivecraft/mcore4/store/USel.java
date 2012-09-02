package com.massivecraft.mcore4.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.massivecraft.mcore4.util.MUtil;

/**
 * USel stands for "Universe Selector".
 * The task of a USel is to select a "universe" based on a world.
 * There will be one USel per "context".
 */
public class USel extends Entity<USel, String>
{
	public final static transient String RETURN = "_return";
	public final static transient String RUN = "_run";
	public final static transient String _DEFAULT = "_default";
	public final static transient String DEFAULT = "default";
	public final static transient List<USelRule> DEFAULT_RULES = MUtil.list(new USelRule(RUN, _DEFAULT));
	public final static transient List<USelRule> DEFAULT_DEFAULT_RULES = MUtil.list(new USelRule(RETURN, DEFAULT));
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override protected USel getThis() { return this; }
	
	private final static transient USel defaultInstance = new USel();
	@Override public USel getDefaultInstance(){ return defaultInstance; }
	@Override protected Class<USel> getClazz() { return USel.class; }
	
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected List<USelRule> rules;
	public List<USelRule> rules() { return this.rules; };
	public void rules(List<USelRule> val) { this.rules = new ArrayList<USelRule>(val); };
	public void rules(String... namesAndParams)
	{
		this.rules = new ArrayList<USelRule>();
		Iterator<String> iter = Arrays.asList(namesAndParams).iterator();
		while (iter.hasNext())
		{
			String name = iter.next();
			String param = iter.next();
			this.rules.add(new USelRule(name, param));
		}
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public USel()
	{
		this.rules(DEFAULT_RULES);
	}
	
	// -------------------------------------------- //
	// THAT SPECIAL LOGIC
	// -------------------------------------------- //
	
	public String select(String worldName)
	{
		for (USelRule rule : this.rules())
		{
			String name = rule.name();
			String param = rule.param();
			if (name.equals(RETURN))
			{
				return param;
			}
			else if (name.equals(RUN))
			{
				USel subSelector = USelColl.i.get(param);
				String subSelectorResult = subSelector.select(worldName);
				if (subSelectorResult != null) return subSelectorResult;
			}
			else if (name.equalsIgnoreCase(worldName))
			{
				return param;
			}
		}
		return null;
	}
	
	
}
