package com.massivecraft.massivecore.store.migrator;

import com.massivecraft.massivecore.Active;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

import java.util.List;
import java.util.regex.Matcher;

public class MigratorRoot implements Migrator, Active
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private List<Migrator> innerMigrators = new MassiveList<>();
	public List<Migrator> getInnerMigrators()  { return innerMigrators; }
	public void setInnerMigrators(List<Migrator> innerMigrators) { this.innerMigrators = new MassiveList<>(innerMigrators); }
	public void addInnerMigrator(Migrator innerMigrator)
	{
		this.innerMigrators.add(innerMigrator);
	}

	private final Class<?> clazz;
	public Class<?> getClazz() { return clazz; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public MigratorRoot(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	// -------------------------------------------- //
	// OVERRIDE: ACTIVE
	// -------------------------------------------- //

	// Boolean
	@Override
	public boolean isActive()
	{
		return MigratorUtil.isActive(this);
	}

	@Override
	public void setActive(boolean active)
	{
		if (active)
		{
			MigratorUtil.addMigrator(this);
		}
		else
		{
			MigratorUtil.removeMigrator(this);
		}
	}

	// Plugin
	private MassivePlugin activePlugin = null;

	@Override
	public MassivePlugin setActivePlugin(MassivePlugin activePlugin)
	{
		MassivePlugin ret = this.activePlugin;
		this.activePlugin = activePlugin;
		return ret;
	}

	@Override
	public MassivePlugin getActivePlugin()
	{
		return this.activePlugin;
	}

	@Override
	public void setActive(MassivePlugin plugin)
	{
		this.setActivePlugin(plugin);
		this.setActive(plugin != null);
	}

	// -------------------------------------------- //
	// VERSION
	// -------------------------------------------- //

	public int getVersion()
	{
		String name = this.getClass().getSimpleName();
		Matcher matcher = Txt.PATTERN_NUMBER.matcher(name);
		if (!matcher.find()) throw new UnsupportedOperationException(String.format("%s doesn't have a version number.", name));
		String number = matcher.group();
		return Integer.parseInt(number);
	}

	// -------------------------------------------- //
	// MIGRATE
	// -------------------------------------------- //

	@Override
	public void migrate(JsonObject entity)
	{
		if (entity == null) throw new NullPointerException("entity");

		// Get current and expected entity version ...
		int entityVersionCurrent = MigratorUtil.getVersion(entity);
		int entityVersionExpected = this.getVersion() - 1;

		// ... make sure they match ...
		if (entityVersionCurrent != entityVersionExpected) throw new IllegalArgumentException(String.format("Entiy version: %d Expected: %d", entityVersionCurrent, entityVersionExpected));

		// ... do the migration.
		this.migrateInner(entity);
		this.migrateVersion(entity);
	}

	private void migrateVersion(JsonObject entity)
	{
		entity.addProperty(MigratorUtil.VERSION_FIELD_NAME, this.getVersion());
	}

	public void migrateInner(JsonObject entity)
	{
		// Look over all inner migrators.
		for (Migrator migrator : this.getInnerMigrators())
		{
			migrator.migrate(entity);
		}
	}

}
