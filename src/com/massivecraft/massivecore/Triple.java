package com.massivecraft.massivecore;

import com.massivecraft.massivecore.util.MUtil;

import java.io.Serializable;

public class Triple<A, B, C> implements Cloneable, Serializable
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
	
	private final C third;
	public C getThird() { return this.third; };
	
	// -------------------------------------------- //
	// FIELDS: WITH
	// -------------------------------------------- //
	
	public Triple<A, B, C> withFirst(A first) { return valueOf(first, second, third); }
	
	public Triple<A, B, C> withSecond(B second) { return valueOf(first, second, third); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Triple()
	{
		this(null, null, null);
	}
	
	public Triple(A first, B second, C third)
	{
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	// -------------------------------------------- //
	// FACTORY: VALUE OF
	// -------------------------------------------- //
	
	public static <A, B, C> Triple<A, B, C> valueOf(A first, B second, C third)
	{
		return new Triple<A, B, C>(first, second, third);
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof Triple<?, ?, ?>)) return false;
		Triple<?, ?, ?> that = (Triple<?, ?, ?>)object;
		return MUtil.equals(
			this.getFirst(), that.getFirst(),
			this.getSecond(), that.getSecond(),
			this.getThird(), that.getThird()
		);
	}
	
	// -------------------------------------------- //
	// CLONE
	// -------------------------------------------- //
	
	@Override
	public Triple<A, B, C> clone()
	{
		return this;
	}
	
}
