package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;
import java.util.Map;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

public class MassiveMapAdapter extends MassiveXAdapter<MassiveMap<?, ?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveMapAdapter i = new MassiveMapAdapter();
	public static MassiveMapAdapter get() { return i; }

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
