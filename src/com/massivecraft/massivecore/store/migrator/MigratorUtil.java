package com.massivecraft.massivecore.store.migrator;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.annotations.SerializedName;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MigratorUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	public static final String VERSION_FIELD_NAME = "version";

	// -------------------------------------------- //
	// MIGRATOR REGISTRY
	// -------------------------------------------- //

	private static Map<Class<?>, Map<Integer, MigratorRoot>> migrators = new HashMap<>();

	public static boolean isActive(MigratorRoot migrator)
	{
		return getMigratorMap(migrator).get(migrator.getVersion()) == migrator;
	}

	// ADD
	public static void addMigrator(MigratorRoot migrator)
	{
		MigratorRoot old = getMigratorMap(migrator).put(migrator.getVersion(), migrator);

		// If there was an old one and it wasn't this one already: deactivate.
		if (old != null && old != migrator) old.setActive(false);
	}

	// REMOVE
	public static void removeMigrator(MigratorRoot migrator)
	{
		MigratorRoot current = getMigratorMap(migrator).get(migrator.getVersion());

		// If there wasn't a new one already: remove
		if (current == migrator) getMigratorMap(migrator).remove(migrator.getVersion());
	}

	// GET
	public static MigratorRoot getMigrator(Class<?> entityClass, int version)
	{
		Map<Integer, MigratorRoot> migratorMap = getMigratorMap(entityClass);

		MigratorRoot migrator = migratorMap.get(version);
		if (migrator == null)
		{
			throw new RuntimeException(String.format("No VersionMigrator found for %s version %d", entityClass.getName(), version));
		}

		return migrator;
	}

	// GET MAP
	private static Map<Integer, MigratorRoot> getMigratorMap(MigratorRoot migrator)
	{
		return getMigratorMap(migrator.getClazz());
	}

	private static Map<Integer, MigratorRoot> getMigratorMap(Class<?> entityClass)
	{
		Map<Integer, MigratorRoot> ret = migrators.get(entityClass);
		if (ret == null)
		{
			ret = new MassiveMap<>();
			migrators.put(entityClass, ret);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// TARGET VERSION
	// -------------------------------------------- //
	
	private static Map<Class<?>, Integer> targetVersions = new HashMap<>();
	
	// ADD
	public static void setTargetVersion(Class<?> clazz, int targetVersion)
	{
		targetVersions.put(clazz, targetVersion);
	}
	
	// GET
	public static int getTargetVersion(Class<?> entityClass)
	{
		if (!targetVersions.containsKey(entityClass))
		{
			targetVersions.put(entityClass, computeTargetVersion(entityClass));
		}
		return targetVersions.get(entityClass);
	}
	
	public static int computeTargetVersion(Class<?> clazz)
	{
		int version = 0;
		try
		{
			version = ReflectionUtil.getField(clazz, VERSION_FIELD_NAME, ReflectionUtil.newInstance(clazz));
		}
		catch (Exception ex)
		{
			// The field was not there
		}
		return version;
	}
	
	// -------------------------------------------- //
	// JSON REPRESENTATION
	// -------------------------------------------- //
	
	private static Map<Class<?>, Type> jsonRepresentation = new HashMap<>();
	
	// ADD
	public static void addJsonRepresentation(Class<?> clazz, Type representation)
	{
		jsonRepresentation.put(clazz, representation);
	}
	
	// GET
	public static Type getJsonRepresentation(Type actualType)
	{
		if (!jsonRepresentation.containsKey(actualType))
		{
			return actualType;
		}
		return jsonRepresentation.get(actualType);
	}
	
	
	// -------------------------------------------- //
	// MIGRATE
	// -------------------------------------------- //
	
	public static boolean migrate(Type realType, JsonElement jsonElement)
	{
		if (jsonElement == null) throw new NullPointerException("element");
		
		if (jsonElement.isJsonNull()) return false;
		if (jsonElement.isJsonPrimitive()) return false;
		
		Type jsonType = getJsonRepresentation(realType);
		
		if (jsonElement.isJsonObject())
		{
			if (jsonType != null && Map.class.isAssignableFrom(getClassType(jsonType)))
			{
				ParameterizedType parameterizedType = (ParameterizedType) jsonType;
				Type keyType = parameterizedType.getActualTypeArguments()[0];
				Type valueType = parameterizedType.getActualTypeArguments()[1];
				
				JsonObject object = jsonElement.getAsJsonObject();
				
				boolean migrated = false;
				for (Entry<String, JsonElement> entry : object.entrySet())
				{
					migrated = migrate(valueType, entry.getValue()) | migrated;
				}
				return migrated;
			}
			
			boolean migrated = false;
			JsonObject object = jsonElement.getAsJsonObject();
			Type classType = jsonType != null ? jsonType : realType;
			migrated = migrateClass(classType, object) | migrated;
			if (jsonType != null) migrated = migrateFields(jsonType, object) | migrated;
			return migrated;
			
		}
		if (jsonElement.isJsonArray())
		{
			Class<?> clazz = getClassType(jsonType);
			
			Type elementType = null;
			
			if (clazz.isArray())
			{
				elementType =  clazz.getComponentType();
			}
			else if (Collection.class.isAssignableFrom(clazz))
			{
				ParameterizedType parameterizedType = (ParameterizedType) jsonType;
				elementType = parameterizedType.getActualTypeArguments()[0];
			}
			
			boolean migrated = false;
			for (JsonElement element1 : jsonElement.getAsJsonArray())
			{
				migrated = migrate(elementType, element1) | migrated;
			}
			return migrated;
		}
		
		throw new RuntimeException();
	}

	public static boolean migrateClass(Type type, JsonObject entity)
	{
		if (type == null) throw new NullPointerException("entityClass");
		if (entity == null) throw new NullPointerException("entity");
		
		Class<?> entityClass = getClassType(type);
		
		int targetVersion = getTargetVersion(entityClass);
		int entityVersion = getVersion(entity);
		if (entityVersion == targetVersion) return false;
		
		validateMigratorsPresent(entityClass, entityVersion, targetVersion);

		for (; entityVersion < targetVersion; entityVersion++)
		{
			// When upgrading we need the migrator we are updating to.
			Migrator migrator = getMigrator(entityClass, entityVersion+1);
			migrator.migrate(entity);
		}

		return true;
	}
	
	public static boolean migrateFields(Type type, JsonObject object)
	{
		if (type == null) throw new NullPointerException("entityClass");
		if (object == null) throw new NullPointerException("object");
		
		Class<?> entityClass = getClassType(type);
		
		// We can't lookup the generic type of a map, so trying to convert the fields is useless
		// Furthermore maps are stored as jsopnobjects, and we can't treat them like other objects
		// because their "fields" are dynamically made and can't be looked up with reflection
		if (Map.class.isAssignableFrom(entityClass)) return false;
		
		boolean migrated = false;
		for (Entry<String, JsonElement> entry : object.entrySet())
		{
			String name = entry.getKey();
			JsonElement element = entry.getValue();

			// It might be defined in a superclass, so find the defining class
			Class<?> superClass = ReflectionUtil.getSuperclassDeclaringField(entityClass, true, name);
			
			// Try find field if it has a different serialisation name
			if (superClass == null)
			{
				Field field = tryFindField(entityClass, name);
				if (field != null)
				{
					name = field.getName();
					superClass = entityClass;
				}
			}
			Type elementType = ReflectionUtil.getField(superClass, name).getGenericType();
			migrated = migrate(elementType, element) | migrated;
		}
		
		return migrated;
	}
	
	private static Field tryFindField(Class<?> clazz, String name)
	{
		for (Field field : clazz.getDeclaredFields())
		{
			SerializedName serializedName = field.getAnnotation(SerializedName.class);
			if (serializedName == null) continue;
			
			if (serializedName.value().equals(name))
			{
				return field;
			}
		}
		return null;
	}
	
	// -------------------------------------------- //
	// MISSING MIGRATORS
	// -------------------------------------------- //

	public static void validateMigratorsPresent(Class<?> entityClass, int from, int to)
	{
		List<Integer> missingMigrators = MigratorUtil.getMissingMigratorVersions(entityClass, from, to);
		if (missingMigrators.isEmpty()) return;

		String versions = Txt.implodeCommaAndDot(missingMigrators);
		String name = entityClass.getName();
		throw new IllegalStateException(String.format("Could not find migrators for %s for versions: %s", name, versions));
	}

	public static List<Integer> getMissingMigratorVersions(Class<?> entityClass, int from, int to)
	{
		if (from == to) return Collections.emptyList();
		if (from > to) throw new IllegalArgumentException(String.format("from: %d to: %d", from, to));
		Map<Integer, MigratorRoot> migrators = getMigratorMap(entityClass);

		// We need not the from but we need the to.
		from++;
		to++;

		List<Integer> ret = new MassiveList<>();
		for (int i = from; i < to; i++)
		{
			if (migrators.containsKey(i)) continue;
			ret.add(i);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static int getVersion(JsonObject entity)
	{
		if (entity == null) throw new NullPointerException("entity");
		JsonElement element = entity.get(VERSION_FIELD_NAME);
		if (element == null) return 0;
		return element.getAsInt();
	}
	
	public static Class<?> getClassType(Type type)
	{
		if (type == null) return null;
		if (type instanceof Class<?>)
		{
			return (Class<?>) type;
		}
		else if (type instanceof ParameterizedType)
		{
			return (Class<?>) ((ParameterizedType) type).getRawType();
		}
		else
		{
			throw new RuntimeException(type.getTypeName());
		}
	}

}
