package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Map;

public class AdapterMassiveMap extends AdapterMassiveX<MassiveMap<?, ?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final AdapterMassiveMap i = new AdapterMassiveMap();
	public static AdapterMassiveMap get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public MassiveMap<?,?> create(Object parent, boolean def, JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		if (def)
		{
			return new MassiveMapDef((Map)parent);
		}
		else
		{
			return new MassiveMap((Map)parent);
		}
	}
	
}
