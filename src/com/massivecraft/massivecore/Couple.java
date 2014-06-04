package com.massivecraft.massivecore;

import java.io.Serializable;
import java.util.Map.Entry;

import com.massivecraft.massivecore.util.MUtil;

public class Couple<A, B> implements Entry<A, B>, Cloneable, Serializable
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final transient long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	private final A first;
	public A getFirst() { return this.first; };
	
	private final B second;
	public B getSecond() { return this.second; };
	
	// -------------------------------------------- //
	// FIELDS: WITH
	// -------------------------------------------- //
	
	public Couple<A, B> withFirst(A first) { return valueOf(first, second); }
	
	public Couple<A, B> withSecond(B second) { return valueOf(first, second); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Couple()
	{
		this(null, null);
	}
	
	public Couple(A first, B second)
	{
		this.first = first;
		this.second = second;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ENTRY
	// -------------------------------------------- //
	
	@Override
	public A getKey()
	{
		return this.first;
	}

	@Override
	public B getValue()
	{
		return this.second;
	}

	@Override
	public B setValue(B arg0)
	{
		throw new IllegalStateException("This entry is a couple which is immutable.");
	}
	
	// -------------------------------------------- //
	// FACTORY: VALUE OF
	// -------------------------------------------- //
	
	public static <A, B> Couple<A, B> valueOf(A first, B second)
	{
		return new Couple<A, B>(first, second);
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object derpObject)
	{
		if (derpObject == null) return false;
		if (!(derpObject instanceof Couple<?, ?>)) return false;
		Couple<?, ?> derp = (Couple<?, ?>)derpObject;
		return MUtil.equals(this.getFirst(), derp.getFirst()) && MUtil.equals(this.getSecond(), derp.getSecond());
	}
	
	// -------------------------------------------- //
	// CLONE
	// -------------------------------------------- //
	
	@Override
	public Couple<A, B> clone()
	{
		return this;
	}
	
}
