package com.massivecraft.mcore4.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.massivecraft.mcore4.util.MUtil;

/**
 * WCat stands for World Categorizer.
 * They provide a recursive matching system.
 */
public class WCat extends Entity<WCat, String>
{
	public final static transient String RETURN = "_return";
	public final static transient String RUN = "_run";
	public final static transient String _DEFAULT = "_default";
	public final static transient String DEFAULT = "default";
	public final static transient List<WCatRule> DEFAULT_RULES = MUtil.list(new WCatRule(RUN, _DEFAULT));
	public final static transient List<WCatRule> DEFAULT_DEFAULT_RULES = MUtil.list(new WCatRule(RETURN, DEFAULT));
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override public Coll<WCat, String> getColl() { return WCatColl.i; }
	@Override protected WCat getThis() { return this; }
	
	private final static transient WCat defaultInstance = new WCat();
	@Override public WCat getDefaultInstance(){ return defaultInstance; }
	@Override protected Class<WCat> getClazz() { return WCat.class; }
	
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected List<WCatRule> rules;
	public List<WCatRule> rules() { return this.rules; };
	public void rules(List<WCatRule> val) { this.rules = new ArrayList<WCatRule>(val); };
	public void rules(String... namesAndParams)
	{
		this.rules = new ArrayList<WCatRule>();
		Iterator<String> iter = Arrays.asList(namesAndParams).iterator();
		while (iter.hasNext())
		{
			String name = iter.next();
			String param = iter.next();
			this.rules.add(new WCatRule(name, param));
		}
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WCat()
	{
		this.rules(DEFAULT_RULES);
	}
	
	// -------------------------------------------- //
	// THAT SPECIAL LOGIC
	// -------------------------------------------- //
	
	public String categorize(String worldName)
	{
		for (WCatRule rule : this.rules())
		{
			String name = rule.name();
			String param = rule.param();
			if (name.equals(RETURN))
			{
				return param;
			}
			else if (name.equals(RUN))
			{
				WCat subcat = WCatColl.i.get(param);
				String subcatresult = subcat.categorize(worldName);
				if (subcatresult != null) return subcatresult;
			}
			else if (name.equalsIgnoreCase(worldName))
			{
				return param;
			}
		}
		return null;
	}
	
	
}
