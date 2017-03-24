package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.collections.MassiveList;

import java.util.List;

public class ConverterList<EX, EY> extends Converter<Iterable<EX>, List<EY>>
{
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final Converter<EX, EY> converterElement;
	public Converter<EX, EY> getConverterElement() { return this.converterElement; }
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	public ConverterList(Converter<EX, EY> converterElement)
	{
		this.converterElement = converterElement;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<EY> convert(Iterable<EX> exs)
	{
		// Null
		if (exs == null) return null;
		
		// Create
		List<EY> eys = new MassiveList<>();
		
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
