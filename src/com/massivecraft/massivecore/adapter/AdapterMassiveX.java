package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.collections.Def;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;
import com.massivecraft.massivecore.xlib.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * This is the abstract adapter for all "Massive structures".
 * It makes sure Def instances "handle empty as null".
 * It makes sure we avoid infinite GSON recurse loops by recursing with supertype.
 */
public abstract class AdapterMassiveX<T> implements JsonDeserializer<T>, JsonSerializer<T>
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(T src, Type type, JsonSerializationContext context)
	{
		ParameterizedType ptype = (ParameterizedType) type;
		
		// Calculate def
		Class<?> clazz = getClazz(ptype);
		boolean def = Def.class.isAssignableFrom(clazz);
		
		// If this is a Def ...
		if (def)
		{
			// ... and the instance is null or contains no elements ...
			if (isEmpty(src))
			{
				// ... then serialize as a JsonNull!
				return JsonNull.INSTANCE;
			}
			// ... and it's non null and contains something ...
			else
			{
				// ... then serialize it as if it were the regular Java collection!
				// SUPER TYPE x2 EXAMPLE: MassiveListDef --> MassiveList --> ArrayList
				return context.serialize(src, getSuperType(getSuperType(ptype)));
			}
		}
		// If this a regular Massive structure and not a Def ...
		else
		{
			// ... then serialize it as if it were the regular java collection!
			// SUPER TYPE x1 EXAMPLE: MassiveList --> ArrayList
			return context.serialize(src, getSuperType(ptype));
		}
	}
	
	@Override
	public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		ParameterizedType ptype = (ParameterizedType) type;
		
		/*// TODO: Temporary Debug
		if (MUtil.getStackTraceString().contains("com.massivecraft.factions.entity.FactionColl.init"))
		{
			typeDebug(ptype);
			typeDebug(getSuperType(ptype));
			typeDebug(getSuperType(getSuperType(ptype)));
		}*/
		
		// Calculate def
		Class<?> clazz = getClazz(ptype);
		boolean def = Def.class.isAssignableFrom(clazz);
		
		// If this is a Def ...
		if (def)
		{
			// ... then deserialize it as if it were the regular Java collection!
			// SUPER TYPE x2 EXAMPLE: MassiveListDef --> MassiveList --> ArrayList
			Object parent = context.deserialize(json, getSuperType(getSuperType(ptype)));
			return create(parent, def, json, type, context);
		}
		// If this a regular Massive structure and not a Def ...
		else
		{
			// ... and the json is null or a JsonNull ...
			if (json == null || json instanceof JsonNull)
			{
				// ... then deserialize as a null!
				return null;
			}
			// ... and it's non null and contains something ...
			else
			{
				// ... then deserialize it as if it were the regular java collection!
				// SUPER TYPE x1 EXAMPLE: MassiveList --> ArrayList
				Object parent = context.deserialize(json, getSuperType(ptype));
				return create(parent, def, json, type, context);
			}
		}
	}
	
	/*
	public static void typeDebug(ParameterizedType ptype)
	{
		System.out.println("=== Type Debug Start ===");
		
		System.out.println(ptype.toString());
		
		ParameterizedType parameterizedType = (ParameterizedType) ptype;
		System.out.println("Actual Type Arguments: " + Txt.implode(parameterizedType.getActualTypeArguments(), ", "));
		
		System.out.println("=== Type Debug End ===");
	}*/
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T create(Object parent, boolean def, JsonElement json, Type typeOfT, JsonDeserializationContext context);
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static Class<?> getClazz(ParameterizedType ptype)
	{
		return (Class<?>)ptype.getRawType();
	}
	
	public static ParameterizedType getSuperType(ParameterizedType ptype)
	{
		// ------- SELF -------
		
		// Get args
		Type[] args = ptype.getActualTypeArguments();
		
		// Get clazz
		Class<?> clazz = (Class<?>)ptype.getRawType();
		
		// ------- SUPER -------
		
		// Get stype
		ParameterizedType sptype = (ParameterizedType) clazz.getGenericSuperclass();
		
		// Get sargs
		// NOTE: These will be broken! we can however look at the count!
		Type[] sargs = sptype.getActualTypeArguments();
		
		// Get sclazz
		Class<?> sclazz = (Class<?>)sptype.getRawType();
		
		// ------- CONSTRUCTED -------
		
		Type[] typeArguments = Arrays.copyOfRange(args, 0, sargs.length);
		
		return $Gson$Types.newParameterizedTypeWithOwner(null, sclazz, typeArguments);
	}
	
	public static Object getNewArgumentInstance(Type type, int index)
	{
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		Class<?> clazz = (Class<?>) actualTypeArguments[index];
		try
		{
			return clazz.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object object)
	{
		// A Map is not a Collection.
		// Thus we have to use isEmpty() declared in different interfaces. 
		if (object == null) return true;
		if (object instanceof Map) return ((Map)object).isEmpty();
		if (object instanceof Collection) return ((Collection)object).isEmpty();
		return false;
	}
	
}
