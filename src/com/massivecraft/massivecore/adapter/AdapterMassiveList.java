package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveListDef;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Collection;

public class AdapterMassiveList extends AdapterMassiveX<MassiveList<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final AdapterMassiveList i = new AdapterMassiveList();
	public static AdapterMassiveList get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public MassiveList<?> create(Object parent, boolean def, JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		if (def)
		{
			return new MassiveListDef((Collection)parent);
		}
		else
		{
			return new MassiveList((Collection)parent);
		}
	}
	
}
