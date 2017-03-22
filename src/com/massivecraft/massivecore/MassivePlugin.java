package com.massivecraft.massivecore;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.comparator.ComparatorActivePriority;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.nms.Nms;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateAnd;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.migration.VersionMigrator;
import com.massivecraft.massivecore.test.Test;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

public abstract class MassivePlugin extends JavaPlugin implements Listener, Named
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Gson
	protected Gson gson = null;
	public Gson getGson() { return this.gson; }
	public void setGson(Gson gson) { this.gson = gson; }
	
	protected boolean versionSynchronized = false;
	public boolean isVersionSynchronized() { return this.versionSynchronized; }
	public void setVersionSynchronized(boolean versionSynchronized) { this.versionSynchronized = versionSynchronized; }
	
	// -------------------------------------------- //
	// LOAD
	// -------------------------------------------- //
	
	@Override
	public void onLoad()
	{
		this.onLoadPre();
		this.onLoadInner();
		this.onLoadPost();
	}
	
	public void onLoadPre()
	{
		this.logPrefixColored = Txt.parse("<teal>[<aqua>%s %s<teal>] <i>", this.getDescription().getName(), this.getDescription().getVersion());
		this.logPrefixPlain = ChatColor.stripColor(this.logPrefixColored);
	}
	
	public void onLoadInner()
	{
		
	}
	
	public void onLoadPost()
	{
		
	}
	
	// -------------------------------------------- //
	// ENABLE
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! this.onEnablePre()) return;
		this.onEnableInner();
		this.onEnablePost();
	}
	
	private long enableTime;
	public long getEnableTime() { return this.enableTime; }
	
	public boolean onEnablePre()
	{
		this.enableTime = System.currentTimeMillis();
		
		log("=== ENABLE START ===");
		
		// Version Synchronization
		this.checkVersionSynchronization();
		
		// Create Gson
		Gson gson = this.getGsonBuilder().create();
		this.setGson(gson);
		
		// Listener
		Bukkit.getPluginManager().registerEvents(this, this);

		return true;
	}
	
	public void checkVersionSynchronization()
	{
		// If this plugin is version synchronized ...
		if ( ! this.isVersionSynchronized()) return;
		
		// ... and checking is enabled ...
		if ( ! MassiveCoreMConf.get().versionSynchronizationEnabled) return;
		
		// ... get the version numbers ...
		String thisVersion = this.getDescription().getVersion();
		String massiveVersion = MassiveCore.get().getDescription().getVersion();
		
		// ... and if the version numbers are different ...
		if (thisVersion.equals(massiveVersion)) return;
		
		// ... log a warning message ...
		String thisName = this.getDescription().getName();
		String massiveName = MassiveCore.get().getDescription().getName();
		
		log(Txt.parse("<b>WARNING: You are using <pink>" + thisName + " <aqua>" + thisVersion + " <b>and <pink>" + massiveName + " <aqua>" + massiveVersion + "<b>!"));
		log(Txt.parse("<b>WARNING: They must be the exact same version to work properly!"));
		log(Txt.parse("<b>WARNING: Remember to always update all plugins at the same time!"));
		log(Txt.parse("<b>WARNING: You should stop your server and properly update."));
		
		// ... and pause for 10 seconds.
		try
		{
			Thread.sleep(10000L);
		}
		catch (InterruptedException ignored)
		{
			
		}
	}
	
	public void onEnableInner()
	{
		
	}
	
	public void onEnablePost()
	{
		// Metrics
		if (MassiveCoreMConf.get().mcstatsEnabled)
		{
			try
			{
				MetricsLite metrics = new MetricsLite(this);
				metrics.start();
			}
			catch (IOException e)
			{
				String message = Txt.parse("<b>Metrics Initialization Failed :'(");
				log(message);
			}
		}
		
		long ms = System.currentTimeMillis() - this.enableTime;
		log(Txt.parse("=== ENABLE <g>COMPLETE <i>(Took <h>" + ms + "ms<i>) ==="));
	}
	
	// -------------------------------------------- //
	// DISABLE
	// -------------------------------------------- //
	
	@Override
	public void onDisable()
	{
		// Commands
		this.deactivate(MassiveCommand.getAllInstances());
		
		// Engines
		this.deactivate(Engine.getAllInstances());
		
		// Collections
		this.deactivate(Coll.getInstances());
		
		log("Disabled");
	}
	
	// -------------------------------------------- //
	// GSON
	// -------------------------------------------- //
	
	public GsonBuilder getGsonBuilder()
	{
		return MassiveCore.getMassiveCoreGsonBuilder();
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public void suicide()
	{
		this.log(Txt.parse("<b>Now I suicide!"));
		Bukkit.getPluginManager().disablePlugin(this);
	}

	public void activate(Object... objects)
	{
		activate(Arrays.asList(objects), true);
	}
	
	public void activate(Collection<?> objects, boolean strictThrow)
	{
		for (Object object : objects)
		{
			Active active = asActive(object);
			if (active == null) continue;
			int priority = object instanceof Class ? ComparatorActivePriority.getPriority((Class) object) : -1;

			if (active.isActive())
			{
				if(strictThrow) throw new IllegalArgumentException(active.getClass().getName() + " is already active");
				else continue;
			}
			//if (this.getName().equals("MassiveCore") || this.getName().equals("Factions"))
			{
				log(Txt.parse("<i>Activating <h>%s", active.getClass().getSimpleName()));
			}
			active.setActive(this);
		}
	}

	private static Active asActive(Object object)
	{
		if (object instanceof Active)
		{
			return (Active)object;
		}

		if (object instanceof String)
		{
			String string = (String)object;
			try
			{
				object = Class.forName(string);
			}
			catch (NoClassDefFoundError | ClassNotFoundException e)
			{
				// Silently skip and move on
				return null;
			}
		}

		if (object instanceof Class<?>)
		{
			Class<?> clazz = (Class<?>)object;
			if ( ! Active.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Not Active Class: " + (clazz == null ? "NULL" : clazz));

			Object instance = ReflectionUtil.getSingletonInstance(clazz);
			if ( ! (instance instanceof Active)) throw new IllegalArgumentException("Not Active Instance: " + (instance == null ? "NULL" : instance) + " for object: " + (object == null ? "NULL" : object));

			Active active = (Active)instance;
			return active;
		}

		throw new IllegalArgumentException("Neither Active nor Class: " + object);
	}
	
	private void deactivate(Collection<? extends Active> actives)
	{
		// Fail Fast
		if (actives == null) throw new NullPointerException("actives");
		
		// Clone to Avoid CME
		List<Active> all = new MassiveList<>(actives);
		
		// Reverse to Disable Reversely
		Collections.reverse(all);
		
		// Loop
		for (Active active : all)
		{
			// Check
			if ( ! this.equals(active.getActivePlugin())) continue;
			
			// Deactivate
			active.setActive(false);
		}
	}

	// -------------------------------------------- //
	// ACTIVATE AUTO
	// -------------------------------------------- //

	public void activateAuto()
	{
		// Create
		List<Class<?>> classes = new MassiveList<>();

		// Fill with all kinds of Actives
		classes.addAll(this.getColls());
		classes.addAll(this.getNms());
		classes.addAll(this.getCommands());
		classes.addAll(this.getEngines());
		classes.addAll(this.getIntegrations());
		classes.addAll(this.getTasks());
		classes.addAll(this.getMixins());
		classes.addAll(this.getTests());
		classes.addAll(this.getVersionMigrators());

		// Sort them so they are activated in the right order
		Collections.sort(classes, ComparatorActivePriority.get());

		// And activate them
		this.activate(classes, false);
	}

	public List<Class<?>> getColls()
	{
		return getClasses("entity", Coll.class);
	}

	public List<Class<?>> getNms()
	{
		return getClasses(Nms.class, new Predicate<Class<?>>()
		{
			@Override public boolean apply(Class<?> clazz)
			{
				try
				{
					ReflectionUtil.getField(clazz, "d");
					return true;
				}
				catch (Throwable t)
				{
					return false;
				}
			}
		});
	}

	public List<Class<?>> getCommands()
	{
		return getClasses("cmd", MassiveCommand.class, new Predicate<Class<?>>()
		{
			@Override public boolean apply(Class<?> clazz)
			{
				try
				{
					ReflectionUtil.getSingletonInstance(clazz);
					return true;
				}
				catch (Exception ex)
				{
					return false;
				}
			}
		});
	}

	public List<Class<?>> getEngines()
	{
		return getClasses(Engine.class);
	}

	public List<Class<?>> getIntegrations()
	{
		return getClasses(Integration.class);
	}

	public List<Class<?>> getTasks()
	{
		return getClasses("task", ModuloRepeatTask.class);
	}

	public List<Class<?>> getMixins()
	{
		return getClasses(Mixin.class);
	}

	public List<Class<?>> getTests()
	{
		return getClasses(Test.class);
	}

	public List<Class<?>> getVersionMigrators()
	{
		return getClasses("entity.migration", VersionMigrator.class);
	}

	@SuppressWarnings("unchecked")
	public List<Class<?>> getClasses(Class<? extends Active> superClass, Predicate<Class<?>>... predicates)
	{
		return getClasses(superClass.getSimpleName().toLowerCase(), superClass, predicates);
	}

	@SuppressWarnings("unchecked")
	public List<Class<?>> getClasses(String packageName, Class<?> superClass, Predicate<Class<?>>... predicates)
	{
		// Create ret
		List<Class<?>> ret = new MassiveList<>();

		ClassLoader classLoader = this.getClassLoader();
		try
		{
			// Get info
			packageName = packageName == null ? "" : "." + packageName;
			String activePackage = this.getClass().getPackage().getName() + packageName;
			ClassPath classPath = ClassPath.from(classLoader);
			Predicate<Class<?>> predicateCombined = PredicateAnd.get(predicates);

			for (ClassInfo classInfo : classPath.getTopLevelClassesRecursive(activePackage))
			{
				// Get name of class
				String className = classInfo.getName();

				// Avoid versions created at runtime
				// Apparently it found a "EngineMassiveCoreCollTick 3" which we don't want
				if (className.contains(" ")) continue;

				// Try and load it
				Class<?> clazz;
				try
				{
					 clazz = classInfo.load();
				}
				catch (NoClassDefFoundError ex)
				{
					// Probably an integration Engine which we should not have loaded.
					// Just skip it
					continue;
				}

				// The class cannot be abstracy
				if (Modifier.isAbstract(clazz.getModifiers())) continue;

				// And it must be an instance of what we expect
				if (!superClass.isAssignableFrom(clazz)) continue;

				// And it must not be ignored
				if (clazz.getAnnotation(ActiveIgnore.class) != null) continue;
				if (!predicateCombined.apply(clazz)) continue;

				ret.add(clazz);
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}

		return ret;
	}
	
	// -------------------------------------------- //
	// LOGGING
	// -------------------------------------------- //

	private String logPrefixColored = null;
	private String logPrefixPlain = null;
	public void log(Object... msg)
	{
		log(Level.INFO, msg);
	}
	public void log(Level level, Object... msg)
	{
		String imploded = Txt.implode(msg, " ");
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		if (level == Level.INFO && sender != null)
		{
			Bukkit.getConsoleSender().sendMessage(this.logPrefixColored + imploded);
		}
		else
		{
			Logger.getLogger("Minecraft").log(level, this.logPrefixPlain + imploded);
		}
	}
}
