package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.collections.MassiveTreeSet;
import com.massivecraft.massivecore.collections.MassiveTreeSetDef;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Collection;

public class AdapterMassiveTreeSet extends AdapterMassiveX<MassiveTreeSet<?, ?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final AdapterMassiveTreeSet i = new AdapterMassiveTreeSet();
	public static AdapterMassiveTreeSet get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public MassiveTreeSet<?, ?> create(Object parent, boolean def, JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		Object comparator = getComparator(typeOfT);
		if (def)
		{
			return new MassiveTreeSetDef(comparator, (Collection)parent);
		}
		else
		{
			return new MassiveTreeSet(comparator, (Collection)parent);
		}
	}
	
	// -------------------------------------------- //
	// GET COMPARATOR
	// -------------------------------------------- //
	
	public static Object getComparator(Type typeOfT)
	{
		return getNewArgumentInstance(typeOfT, 1);
	}
	
}
