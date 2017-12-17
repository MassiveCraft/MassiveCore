package com.massivecraft.massivecore.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class ConverterListImmutable<EX, EY> extends Converter<Iterable<EX>, ImmutableList<EY>>
{
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final Converter<EX, EY> converterElement;
	public Converter<EX, EY> getConverterElement() { return this.converterElement; }
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	public ConverterListImmutable(Converter<EX, EY> converterElement)
	{
		this.converterElement = converterElement;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ImmutableList<EY> convert(Iterable<EX> exs)
	{
		// Null
		if (exs == null) return null;
		
		// Create
		Builder<EY> eys = ImmutableList.builder();
		
		// Fill
		for (EX ex : exs)
		{
			EY ey = null;
			try
			{
				ey = this.getConverterElement().convert(ex);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				continue;
			}
			
			eys.add(ey);
		}
		
		// Return
		return eys.build();
	}
	
}
