package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.collections.MassiveSet;

import java.util.Set;

public class ConverterSet<EX, EY> extends Converter<Iterable<EX>, Set<EY>>
{
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final Converter<EX, EY> converterElement;
	public Converter<EX, EY> getConverterElement() { return this.converterElement; }
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	public ConverterSet(Converter<EX, EY> converterElement)
	{
		this.converterElement = converterElement;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Set<EY> convert(Iterable<EX> exs)
	{
		// Null
		if (exs == null) return null;
		
		// Create
		Set<EY> eys = new MassiveSet<>();
		
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
		return eys;
	}

}
