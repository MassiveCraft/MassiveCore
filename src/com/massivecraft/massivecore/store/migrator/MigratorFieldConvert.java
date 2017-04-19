package com.massivecraft.massivecore.store.migrator;

import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;

public abstract class MigratorFieldConvert implements Migrator
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private final String fieldName;
	public String getFieldName() { return fieldName; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public MigratorFieldConvert(String fieldName)
	{
		if (fieldName == null) throw new NullPointerException("fieldName");
		this.fieldName = fieldName;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void migrate(JsonObject entity)
	{
		JsonElement value = entity.get(this.getFieldName());
		if (value == null) value = JsonNull.INSTANCE;
		value = getElement(migrateInner(value));
		entity.add(this.getFieldName(), value);
	}

	private static JsonElement getElement(Object object)
	{
		if (object instanceof JsonElement) return (JsonElement) object;
		else if (object instanceof String) return new JsonPrimitive((String) object);
		else if (object instanceof Boolean) return new JsonPrimitive((Boolean) object);
		else if (object instanceof Character) return new JsonPrimitive((Character) object);
		else if (object instanceof Number) return new JsonPrimitive((Number) object);
		
		throw new IllegalArgumentException("Unvalid JsonElement: " + object);
	}

	// Must return JsonElement or a primitive
	public abstract Object migrateInner(JsonElement element);

}
