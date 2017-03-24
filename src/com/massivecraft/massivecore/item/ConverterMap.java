package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.collections.MassiveMap;

import java.util.Map;
import java.util.Map.Entry;

public class ConverterMap<KX, VX, KY, VY> extends Converter<Map<KX, VX>, Map<KY, VY>>
{
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final Converter<KX, KY> converterKey;
	public Converter<KX, KY> getConverterKey() { return this.converterKey; }
	
	private final Converter<VX, VY> converterValue;
	public Converter<VX, VY> getConverterValue() { return this.converterValue; }
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	public ConverterMap(Converter<KX, KY> converterKey, Converter<VX, VY> converterValue)
	{
		this.converterKey = converterKey;
		this.converterValue = converterValue;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Map<KY, VY> convert(Map<KX, VX> mapx)
	{
		// Null
		if (mapx == null) return null; 
		
		// Create
		Map<KY, VY> mapy = new MassiveMap<>();
		
		// Fill
		for (Entry<KX, VX> entry : mapx.entrySet())
		{
			KY ky = null;
			KX kx = entry.getKey();
			try
			{
				ky = this.getConverterKey().convert(kx);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				continue;
			}
			
			VY vy = null;
			VX vx = entry.getValue();
			try
			{
				vy = this.getConverterValue().convert(vx);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				continue;
			}
			
			mapy.put(ky, vy);
		}
		
		// Return
		return mapy;
	}

}
