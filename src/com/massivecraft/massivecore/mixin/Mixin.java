package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Mixin extends Engine
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
	
	private Mixin defaultInstance = null;
	public Mixin getDefault()
	{
		if (this.defaultInstance == null) this.defaultInstance = ReflectionUtil.getField(this.getDefaultClass(), "d", null);
		return this.defaultInstance;
	}
	public boolean isDefault() { return this == this.getDefault(); }
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	// Access the active static instance using instance methods.
	// This looks a bit strange but will save us a lot of repetitive source code down the road.
	// These are not meant to be used for selection or activation.
	// The standard active interface methods are used for that.
	
	public Mixin getInstance() { return ReflectionUtil.getField(this.getDefaultClass(), "i", null); }
	public void setInstance(Mixin i) { ReflectionUtil.setField(this.getDefaultClass(), "i", null, i); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// This is the base name.
	// It should describe the function supplied.
	// It should be set in the base class constructor.
	private String baseName = this.getDefaultClass().getSimpleName();
	public String getBaseName() { return this.baseName; }
	public void setBaseName(String baseName) { this.baseName = baseName; }
	
	// This is the instance specific name.
	// It should describe the circumstances for compatibility.
	// It could for example contain a version number.
	private String name = this.createName();
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	// Is the Default counted as the Mixin being available?
	// And based off that information are we available?
	// NOTE: This field must be set in the default constructor!
	private Boolean availableDefault = null;
	public boolean isAvailableDefault() { return (this.availableDefault != null ? this.availableDefault : this.getDefault().getAlternatives().isEmpty()); }
	public void setAvailableDefault(boolean availableDefault) { this.availableDefault = availableDefault; }
	public boolean isAvailable() { return this.isAvailableDefault() || this.getInstance() != this.getDefault(); }
	public void require() { if (this.isAvailable()) return; throw new IllegalStateException(this.getBaseName() + " is Required"); }
	
	// This is the list of alternatives to choose from.
	// The first compatible alternative will be chosen for activation.
	// The list should thus start with the most provoking and detailed alternative.
	// If the list is empty we simply offer ourselves.
	private List<Class<?>> alternatives = new MassiveList<>();
	public List<Class<?>> getAlternatives() { return this.alternatives; }
	@SuppressWarnings("unchecked")
	public <T extends Mixin> T setAlternatives(List<Class<?>> alternatives) { this.alternatives = alternatives; return (T) this; }
	public <T extends Mixin> T setAlternatives(Class<?>... alternatives) { return this.setAlternatives(Arrays.asList(alternatives)); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Mixin()
	{
		// Setup
		try
		{
			this.provoke();
			this.setup();
		}
		catch (Throwable t)
		{
			//t.printStackTrace();
			throw ReflectionUtil.asRuntimeException(t);
		}
	}
	
	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	// This method is run from the constructor.
	// It may throw upon incompatibility.
	// --> Returns an object for easier provoking.
	
	public Object provoke() throws Throwable
	{
		return null;
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	// This method is run from the constructor.
	// It may throw upon incompatibility.
	// --> Does not return anything.
	
	public void setup() throws Throwable
	{
		
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
		boolean verbose = MassiveCoreMConf.get() != null && MassiveCoreMConf.get().debugEnabled;
		this.setActiveVerboose(active, verbose);
	}
	
	public void setActiveVerboose(boolean active, boolean verbose)
	{
		// NoChange
		if (this.isActive() == active) return;
		
		// Before and After
		Mixin before = this.getInstance();
		Mixin after = (active ? ReflectionUtil.getSingletonInstanceFirstCombatible(this.getAlternatives(), this) : this.getDefault());
		before.setPluginSoft(this.getPlugin());
		after.setPluginSoft(this.getPlugin());
		
		// Deactivate Before
		if (before != this) before.setActiveVerboose(false, false);
		
		// Set Instance
		this.setInstance(after);
		
		// Activate After
		if (after != this) after.setActiveVerboose(true, verbose);
		
		// This
		if (after != this) return;
		
		// Inform
		if (verbose)
		{
			String message = Txt.parse("<i>Mixin <h>%s<i> set to <h>%s", this.getBaseName(), this.getName());
			after.getPlugin().log(message);
		}
		
		// Super
		super.setActive(active);
	}
	
	// -------------------------------------------- //
	// NOT IMPLEMENTED
	// -------------------------------------------- //
	
	public RuntimeException notImplemented()
	{
		return new UnsupportedOperationException("not implemented");
	}
	
	// -------------------------------------------- //
	// CREATE NAME
	// -------------------------------------------- //
	
	private static final String FORGE = "Forge";
	
	private static final Map<String, String> NAME_MAP = new MassiveMap<>(
		"", "Default",
		"Fallback", "Fallback (Generic and Weaker)",
		
		"17R4", "Minecraft 1.7.10 [1_7_R4]",
		"17R4P", "Minecraft 1.7.10+ [1_7_R4+]",
		
		"18R1", "Minecraft 1.8.0 --> 1.8.2 [1_8_R1]",
		"18R1P", "Minecraft 1.8.0+ [1_8_R1+]",
		
		"18R2", "Minecraft 1.8.3 [1_8_R2]",
		"18R2P", "Minecraft 1.8.3+ [1_8_R2+]",
		
		"18R3", "Minecraft 1.8.4 --> 1.8.9 [1_8_R3]",
		"18R3P", "Minecraft 1.8.4+ [1_8_R3+]",
		
		"19R1", "Minecraft 1.9.0 --> 1.9.3 [1_9_R1]",
		"19R1P", "Minecraft 1.9.0+ [1_9_R1+]",
		
		"19R2", "Minecraft 1.9.4 [1_9_R2]",
		"19R2P", "Minecraft 1.9.4+ [1_9_R2+]",
		
		"110R1", "Minecraft 1.10.0 --> 1.10.2 [1_10_R1]",
		"110R1P", "Minecraft 1.10.0+ [1_10_R1+]",
		
		"111R1", "Minecraft 1.11.0 --> 1.11.2 [1_11_R1]",
		"111R1P", "Minecraft 1.11.0+ [1_11_R1+]",
		
		"112R1", "Minecraft 1.12.0 --> ? [1_12_R1]",
		"112R1P", "Minecraft 1.12.0+ [1_12_R1+]"
	);
	
	public String createName()
	{
		// Create
		String ret = this.getClass().getSimpleName();
		
		// Tweak
		String baseName = this.getBaseName();
		if (ret.startsWith(baseName)) ret = ret.substring(baseName.length());
		
		// Forge Pre
		boolean forge = ret.endsWith(FORGE);
		if (forge) ret = ret.substring(0, ret.length() - FORGE.length());
		
		// Name Map
		String name = NAME_MAP.get(ret);
		if (name != null) ret = name;
		
		// Forge Post
		if (forge) ret = FORGE + " " + ret;
		
		// Return
		return ret;
	}
	
}
