package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;
import java.util.Map;

import com.massivecraft.massivecore.collections.MassiveTreeMap;
import com.massivecraft.massivecore.collections.MassiveTreeMapDef;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

public class MassiveTreeMapAdapter extends MassiveXAdapter<MassiveTreeMap<?, ?, ?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveTreeMapAdapter i = new MassiveTreeMapAdapter();
	public static MassiveTreeMapAdapter get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public MassiveTreeMap<?, ?, ?> create(Object parent, boolean def, JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		Object comparator = getComparator(typeOfT);
		if (def)
		{
			return new MassiveTreeMapDef(comparator, (Map)parent);
		}
		else
		{
			return new MassiveTreeMap(comparator, (Map)parent);
		}
	}
	
	// -------------------------------------------- //
	// GET COMPARATOR
	// -------------------------------------------- //
	
	public static Object getComparator(Type typeOfT)
	{
		return getNewArgumentInstance(typeOfT, 2);
	}
	
}
