package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.collections.MassiveSetDef;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Collection;

public class AdapterMassiveSet extends AdapterMassiveX<MassiveSet<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final AdapterMassiveSet i = new AdapterMassiveSet();
	public static AdapterMassiveSet get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public MassiveSet<?> create(Object parent, boolean def, JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		if (def)
		{
			return new MassiveSetDef((Collection)parent);
		}
		else
		{
			return new MassiveSet((Collection)parent);
		}
	}
	
}
