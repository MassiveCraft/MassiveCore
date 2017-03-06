package com.massivecraft.massivecore.store.migration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class VersionMigrationUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	public static final String VERSION_FIELD_NAME = "version";

	// -------------------------------------------- //
	// REGISTRY
	// -------------------------------------------- //

	private static Map<Class<? extends Entity<?>>, Map<Integer, VersionMigratorRoot>> migrators = new HashMap<>();

	public static boolean isActive(VersionMigratorRoot migrator)
	{
		return getMigratorMap(migrator).get(migrator.getVersion()) == migrator;
	}

	// ADD
	public static void addMigrator(VersionMigratorRoot migrator)
	{
		VersionMigratorRoot old = getMigratorMap(migrator).put(migrator.getVersion(), migrator);

		// If there was an old one and it wasn't this one already: deactivate.
		if (old != null && old != migrator) old.setActive(false);
	}

	// REMOVE
	public static void removeMigrator(VersionMigratorRoot migrator)
	{
		VersionMigratorRoot current = getMigratorMap(migrator).get(migrator.getVersion());

		// If there wasn't a new one already: remove
		if (current == migrator) getMigratorMap(migrator).remove(migrator.getVersion());
	}

	// GET
	public static VersionMigratorRoot getMigrator(Class<? extends Entity<?>> entityClass, int version)
	{
		Map<Integer, VersionMigratorRoot> migratorMap = getMigratorMap(entityClass);

		VersionMigratorRoot migrator = migratorMap.get(version);
		if (migrator == null)
		{
			throw new RuntimeException(String.format("No VersionMigrator found for %s version %d", entityClass.getName(), version));
		}

		return migrator;
	}

	// GET MAP
	private static Map<Integer, VersionMigratorRoot> getMigratorMap(VersionMigratorRoot migrator)
	{
		return getMigratorMap(migrator.getEntityClass());
	}

	private static Map<Integer, VersionMigratorRoot> getMigratorMap(Class<? extends Entity<?>> entityClass)
	{
		Map<Integer, VersionMigratorRoot> ret = migrators.get(entityClass);
		if (ret == null)
		{
			ret = new MassiveMap<>();
			migrators.put(entityClass, ret);
		}
		return ret;
	}

	// -------------------------------------------- //
	// MIGRATE
	// -------------------------------------------- //

	public static boolean migrate(Class<? extends Entity<?>> entityClass, JsonObject entity, int targetVersion)
	{
		if (entityClass == null) throw new NullPointerException("entityClass");
		if (entity == null) throw new NullPointerException("entity");

		int entityVersion = getVersion(entity);
		if (entityVersion == targetVersion) return false;

		validateMigratorsPresent(entityClass, entityVersion, targetVersion);

		for (; entityVersion < targetVersion; entityVersion++)
		{
			// When upgrading we need the migrator we are updating to.
			// When downgrading we need the migrator we are downgrading from.
			// This is done to preserve the same logic within the same class.
			// That is why when updating we don't use entityVersion and when downgrading we do.
			VersionMigrator migrator = getMigrator(entityClass, entityVersion+1);
			migrator.migrate(entity);
		}

		return true;
	}

	// -------------------------------------------- //
	// MISSING MIGRATORS
	// -------------------------------------------- //

	public static void validateMigratorsPresent(Class<? extends Entity<?>> entityClass, int from, int to)
	{
		List<Integer> missingMigrators = VersionMigrationUtil.getMissingMigratorVersions(entityClass, from, to);
		if (missingMigrators.isEmpty()) return;

		String versions = Txt.implodeCommaAndDot(missingMigrators);
		String name = entityClass.getName();
		throw new IllegalStateException(String.format("Could not find migrators for %s for versions: %s", name, versions));
	}

	public static List<Integer> getMissingMigratorVersions(Class<? extends Entity<?>> entityClass, int from, int to)
	{
		if (from == to) return Collections.emptyList();
		if (from > to) throw new IllegalArgumentException(String.format("from: %d to: %d", from, to));
		Map<Integer, VersionMigratorRoot> migrators = getMigratorMap(entityClass);

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

}
