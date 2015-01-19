package com.massivecraft.massivecore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PredictateAnd<T> implements Predictate<T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	@SafeVarargs
	public static <T> PredictateAnd<T> get(Predictate<T>... predictates) { return new PredictateAnd<T>(predictates); }
	@SafeVarargs
	public PredictateAnd(Predictate<T>... predictates)
	{
		this(Arrays.asList(predictates));
	}
	
	public static <T> PredictateAnd<T> get(Collection<Predictate<T>> predictates) { return new PredictateAnd<T>(predictates); }
	public PredictateAnd(Collection<Predictate<T>> predictates)
	{
		this.predictates = Collections.unmodifiableList(new ArrayList<Predictate<T>>(predictates));
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final List<Predictate<T>> predictates;
	public List<Predictate<T>> getPredictates() { return this.predictates; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(T type)
	{
		for (Predictate<T> predictate : this.getPredictates())
		{
			if (!predictate.apply(type)) return false;
		}
		return true;
	}
	
}
