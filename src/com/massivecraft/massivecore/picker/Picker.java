package com.massivecraft.massivecore.picker;

import java.util.Arrays;
import java.util.List;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.ReflectionUtil;

// TODO: Rename to Mixin... because that's what we are trying to replace.
@SuppressWarnings("unchecked")
public class Picker extends Engine
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	// The default class contains the static fields.
	// It should never be abstract and should always be compatible.
	// A default class instance is set as the default value. This avoids null.
	// It is detected by the required static field d. 
	
	private final Class<?> defaultClass = ReflectionUtil.getSuperclassDeclaringField(this.getClass(), true, "d");
	public Class<?> getDefaultClass() { return this.defaultClass; }
	
	public Picker getDefault() { return ReflectionUtil.getField(this.getDefaultClass(), "d", null); }
	public boolean isDefault() { return this == this.getDefault(); }
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	// Access the active static instance using instance methods.
	// This looks a bit strange but will save us a lot of repetitive source code down the road.
	// These are not meant to be used for selection or activation.
	// The standard active interface methods are used for that.
	
	public Picker getInstance() { return ReflectionUtil.getField(this.getDefaultClass(), "i", null); }
	public void setInstance(Picker i) { ReflectionUtil.setField(this.getDefaultClass(), "i", null, i); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// This is the base name.
	// It should describe the function supplied.
	// It should be set in the base class constructor.
	private String baseName = this.getDefaultClass().getClass().getSimpleName();
	public String getBaseName() { return this.baseName; }
	public void setBaseName(String baseName) { this.baseName = baseName; }
	
	// This is the instance specific name.
	// It should describe the circumstances for compatibility.
	// It could for example contain a version number.
	private String name = this.getClass().getSimpleName();
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	// This is the list of alternatives to choose from.
	// The first compatible alternative will be chosen for activation.
	// The list should thus start with the most provoking and detailed alternative.
	// If the list is empty we simply offer ourselves.
	private List<Class<?>> alternatives = new MassiveList<>();
	public List<Class<?>> getAlternatives() { return this.alternatives; }
	public <T extends Picker> T setAlternatives(List<Class<?>> alternatives) { this.alternatives = alternatives; return (T) this; }
	public <T extends Picker> T setAlternatives(Class<?>... alternatives) { return this.setAlternatives(Arrays.asList(alternatives));  }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Picker()
	{
		// Provoke!
		try
		{
			this.provoke();
		}
		catch (Throwable t)
		{
			throw ReflectionUtil.asRuntimeException(t);
		}
	}
	
	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	// Provoke your exceptions and errors here.
	// If a throwable is thrown here the instance is not compatible with the circumstances.
	
	public Object provoke() throws Throwable
	{
		return null;
	}
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	// We need to modify the active logic sightly.
	// 
	// We make use of the standard engine isActive.
	// That one looks at the engine registry and does not care about the instance.
	// That behavior tends to work out well for us in this case.
	//
	// The reason for this is that the default will be set as the instance from the start.
	// That does however not mean that it's active.
	// It means it will be used to set the active which may or may not be itself.
	
	@Override
	public void setActive(boolean active)
	{
		// NoChange
		if (this.isActive() == active) return;
		
		// Before
		Picker before = this.getInstance();
		
		// After
		Picker after;
		if (active)
		{
			// This
			after = this;
			
			// Alternatives
			for (Class<?> alternative : this.getAlternatives())
			{
				try
				{
					Picker alternativeInstance = ReflectionUtil.getSingletonInstance(alternative);
					after = alternativeInstance;
					break;
				}
				catch (Throwable t)
				{
					// Not Compatible
				}
			}
		}
		else
		{
			// Default
			after = this.getDefault();
		}
		
		// Deactivate Before
		if (before != this) before.setActive(false);
		
		// Set Instance
		this.setInstance(after);
		
		// Activate After
		if (after != this) after.setActive(true);
		
		// Super
		if (after == this) super.setActive(active);
	}
	
	
}
