package com.massivecraft.massivecore;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateIntegration;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Integration extends Engine
{
	// -------------------------------------------- //
	// NAME
	// -------------------------------------------- //
	
	private String name = null;
	public String getName() { return this.name; }
	public Integration setName(String name) { this.name = name; return this; }
	
	// -------------------------------------------- //
	// PREDICATE
	// -------------------------------------------- //
	
	private Predicate<Integration> predicate = PredicateIntegration.get();
	public Predicate<Integration> getPredicate() { return this.predicate; }
	public Integration setPredicate(Predicate<Integration> predicate) { this.predicate = predicate; return this; }
	
	private List<String> pluginNames = Collections.emptyList();
	public List<String> getPluginNames() { return this.pluginNames; }
	public Integration setPluginNames(Collection<String> pluginNames) { this.pluginNames = new MassiveList<>(pluginNames); return this; }
	public Integration setPluginNames(String... pluginNames) { return this.setPluginNames(Arrays.asList(pluginNames)); }
	public Integration setPluginName(String pluginName) { return this.setPluginNames(pluginName); }
	
	private List<String> classNames = Collections.emptyList();
	public List<String> getClassNames() { return this.classNames; }
	public Integration setClassNames(Collection<String> classNames) { this.classNames = new MassiveList<>(classNames); return this; }
	public Integration setClassNames(String... classNames) { return this.setClassNames(Arrays.asList(classNames)); }
	public Integration setClassName(String className) { return this.setClassNames(className); }
	
	// -------------------------------------------- //
	// INTEGRATION ACTIVE
	// -------------------------------------------- //
	
	// NOTE: We must make use of duplicate information to avoid triggering class loads.
	private boolean integrationActive = false;
	public boolean isIntegrationActive() { return this.integrationActive; }
	public void setIntegrationActive(Boolean integrationActive)
	{
		// Calc
		if (integrationActive == null)
		{
			integrationActive = this.getPredicate().apply(this);
		}
		
		// NoChange
		if (this.isIntegrationActive() == integrationActive) return;
			
		try
		{
			this.setIntegrationActiveEngines(integrationActive);
			this.setIntegrationActiveInner(integrationActive);
			
			this.integrationActive = integrationActive;
			
			String message = Txt.parse(integrationActive ? "<g>Integration Activated <h>%s" : "<b>Integration Deactivated <h>%s", this.getName());
			this.getPlugin().log(message);
		}
		catch (Throwable t)
		{
			String message = Txt.parse(integrationActive ? "<b>Integration Activation <h>%s<b> FAILED:" : "<b>Integration Deactivation <h>%s<b> FAILED:", this.getName());
			this.getPlugin().log(message);
			t.printStackTrace();
		}
	}
	public void setIntegrationActive()
	{
		this.setIntegrationActive(null);
	}
	
	// -------------------------------------------- //
	// INTEGRATION ACTIVE > ENGINES
	// -------------------------------------------- //
	
	public void setIntegrationActiveEngines(boolean active)
	{
		for (Engine engine : this.getEngines())
		{
			engine.setPluginSoft(this.getPlugin());
			engine.setActive(active);
		}
	}
	
	public List<Engine> getEngines()
	{
		Engine engine = this.getEngine();
		if (engine == null) return Collections.emptyList();
		return Collections.singletonList(engine);
	}
	
	public Engine getEngine()
	{
		return null;
	}
	
	// -------------------------------------------- //
	// INTEGRATION ACTIVE > INNER
	// -------------------------------------------- //
	
	public void setIntegrationActiveInner(boolean active)
	{
		
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Integration()
	{
		// TODO: Improve upon this one
		this.setName(this.getClass().getSimpleName());
		
		// TODO: Is this period fine?
		this.setPeriod(10L);
	}
	
	// -------------------------------------------- //
	// TICK > ACTIVE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if (active)
		{
			this.setIntegrationActive();
		}
		else
		{
			this.setIntegrationActive(false);
		}
	}
	
	// -------------------------------------------- //
	// TICK > RUN
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		this.setIntegrationActive();
	}
	
	// -------------------------------------------- //
	// TICK > EVENT LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event)
	{
		if (this.getPlugin().equals(event.getPlugin()))
		{
			this.setIntegrationActive(false);
		}
		else
		{
			this.setIntegrationActive();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event)
	{
		this.setIntegrationActive();
	}
	
}
