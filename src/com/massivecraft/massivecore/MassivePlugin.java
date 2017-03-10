package com.massivecraft.massivecore;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateAnd;
import com.massivecraft.massivecore.predicate.PredicateIsClassSingleton;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.migrator.MigratorRoot;
import com.massivecraft.massivecore.test.Test;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MassivePlugin extends JavaPlugin implements Listener, Named
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Gson
	protected Gson gson = null;
	public Gson getGson() { return this.gson; }
	public void setGson(Gson gson) { this.gson = gson; }
	
	protected boolean versionSynchronized = true;
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
		
		// ... and it's not MassiveCore itself ...
		if (this.getClass().equals(MassiveCore.class)) return;
		
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
		this.activateOne(objects);
	}

	// We must get one active at a time because initialisation of some
	// can only happen after others have been initialised.
	public void activateOne(Object object)
	{
		boolean debug = MassiveCoreMConf.get() != null && MassiveCoreMConf.get().debugActives;

		// Try collection
		if (object instanceof Iterable)
		{
			Iterable<?> elements = (Iterable) object;
			for (Object element : elements)
			{
				this.activateOne(element);
			}
			return;
		}

		// Try array
		if (object instanceof Object[])
		{
			Object[] array = (Object[]) object;
			activateOne(Arrays.asList(array));
			return;
		}

		Active active = asActive(object);
		if (active == null) return;

		if (active.isActive())
		{
			if (debug) log(Txt.parse("<h>%s <b>is already active.", active.getClass().getSimpleName()));
		}

		active.setActive(this);
		if (debug) log(Txt.parse("<i>Activating <h>%s<i>.", active.getClass().getSimpleName()));

	}

	private static Active asActive(Object object)
	{
		// Active already
		if (object instanceof Active)
		{
			return (Active) object;
		}

		// Try string as class name
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

		// Try class
		if (object instanceof Class<?>)
		{
			Class<?> clazz = (Class<?>)object;
			if ( ! Active.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Not Active Class: " + (clazz == null ? "NULL" : clazz));

			Object instance = ReflectionUtil.getSingletonInstance(clazz);
			if ( ! (instance instanceof Active)) throw new IllegalArgumentException("Not Active Instance: " + (instance == null ? "NULL" : instance) + " for object: " + (object == null ? "NULL" : object));

			Active active = (Active)instance;
			return active;
		}

		// No success
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
		// And activate them
		this.activate(getClassesActive());
	}

	public List<Class<?>> getClassesActive()
	{
		List<Class<?>> ret = new MassiveList<>();

		// Fill with all kinds of Actives
		ret.addAll(this.getClassesActiveMigrators());
		ret.addAll(this.getClassesActiveColls());
		ret.addAll(this.getClassesActiveNms());
		ret.addAll(this.getClassesActiveCommands());
		ret.addAll(this.getClassesActiveEngines());
		ret.addAll(this.getClassesActiveIntegrations());
		ret.addAll(this.getClassesActiveTasks());
		ret.addAll(this.getClassesActiveMixins());
		ret.addAll(this.getClassesActiveTests());

		return ret;
	}

	public List<Class<?>> getClassesActiveColls()
	{
		return getClassesActive("entity", Coll.class);
	}

	public List<Class<?>> getClassesActiveNms()
	{
		return getClassesActive("nms", Mixin.class, new Predicate<Class<?>>()
			{
				@Override
				public boolean apply(Class<?> clazz)
				{
					try
					{
						ReflectionUtil.getField(clazz, "d");
						return true;
					}
					catch (Exception ex)
					{
						return false;
					}
				}
			}
		);
	}

	public List<Class<?>> getClassesActiveCommands()
	{
		return getClassesActive("cmd", MassiveCommand.class);
	}

	public List<Class<?>> getClassesActiveEngines()
	{
		return getClassesActive(Engine.class);
	}

	public List<Class<?>> getClassesActiveIntegrations()
	{
		return getClassesActive(Integration.class);
	}

	public List<Class<?>> getClassesActiveTasks()
	{
		return getClassesActive("task", ModuloRepeatTask.class);
	}

	public List<Class<?>> getClassesActiveMixins()
	{
		return getClassesActive(Mixin.class);
	}

	public List<Class<?>> getClassesActiveTests()
	{
		return getClassesActive(Test.class);
	}

	public List<Class<?>> getClassesActiveMigrators()
	{
		return getClassesActive("entity.migrator", MigratorRoot.class);
	}

	public List<Class<?>> getClassesActive(Class<? extends Active> superClass, Predicate<Class<?>>... predicates)
	{
		return getClassesActive(superClass.getSimpleName().toLowerCase(), superClass);
	}

	@SuppressWarnings("unchecked")
	public List<Class<?>> getClassesActive(String packageName, final Class<?> superClass, Predicate<Class<?>>... predicates)
	{
		if (!Active.class.isAssignableFrom(superClass)) throw new IllegalArgumentException(superClass.getName() + " is not insatnce of Active.");
		
		packageName = packageName == null ? "" : "." + packageName;
		packageName = this.getClass().getPackage().getName() + packageName;
		
		Predicate predicateCombined = PredicateAnd.get(predicates);
		Predicate<Class<?>> predicateNotAbstract = new Predicate<Class<?>>()
		{
			@Override
			public boolean apply(Class<?> type)
			{
				return !Modifier.isAbstract(type.getModifiers());
			}
		};
		Predicate<Class<?>> predicateSubclass = new Predicate<Class<?>>()
		{
			@Override
			public boolean apply(Class<?> type)
			{
				return superClass.isAssignableFrom(type);
			}
		};
		Predicate<Class<?>> predicateSingleton = PredicateIsClassSingleton.get();

		return ReflectionUtil.getPackageClasses(packageName, this.getClassLoader(), true, predicateCombined, predicateNotAbstract, predicateSubclass, predicateSingleton);
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
