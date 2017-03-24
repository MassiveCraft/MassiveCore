package com.massivecraft.massivecore.nms;

import com.google.common.base.Function;
import com.massivecraft.massivecore.util.ReflectionUtil;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import java.lang.reflect.Field;
import java.util.Map;

public class NmsEntityDamageEvent17R4P extends NmsEntityDamageEvent
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsEntityDamageEvent17R4P i = new NmsEntityDamageEvent17R4P();
	public static NmsEntityDamageEvent17R4P get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// org.bukkit.event.entity.EntityDamageEvent
	private Class<?> classEntityDamageEvent;
	
	// org.bukkit.event.entity.EntityDamageEvent#originals
	private Field fieldEntityDamageEventOriginals;
	
	// org.bukkit.event.entity.EntityDamageEvent#modifiers
	private Field fieldEntityDamageEventModifiers;
	
	// org.bukkit.event.entity.EntityDamageEvent#modifierFunctions
	private Field fieldEntityDamageEventFunctions;
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		this.classEntityDamageEvent = EntityDamageEvent.class;
		this.fieldEntityDamageEventOriginals = ReflectionUtil.getField(this.classEntityDamageEvent, "originals");
		this.fieldEntityDamageEventModifiers = ReflectionUtil.getField(this.classEntityDamageEvent, "modifiers");
		this.fieldEntityDamageEventFunctions = ReflectionUtil.getField(this.classEntityDamageEvent, "modifierFunctions");
	}
	
	// -------------------------------------------- //
	// ACCESS > ORIGINALS
	// -------------------------------------------- //
	
	@Override
	public Map<DamageModifier, Double> getOriginals(EntityDamageEvent event)
	{
		return ReflectionUtil.getField(this.fieldEntityDamageEventOriginals, event);
	}
	
	@Override
	public void setOriginals(EntityDamageEvent event, Map<DamageModifier, Double> originals)
	{
		ReflectionUtil.setField(this.fieldEntityDamageEventOriginals, event, originals);
	}
	
	// -------------------------------------------- //
	// ACCESS > MODIFIERS
	// -------------------------------------------- //
	
	@Override
	public Map<DamageModifier, Double> getModifiers(EntityDamageEvent event)
	{
		return ReflectionUtil.getField(this.fieldEntityDamageEventModifiers, event);
	}
	
	@Override
	public void setModifiers(EntityDamageEvent event, Map<DamageModifier, Double> modifiers)
	{
		ReflectionUtil.setField(this.fieldEntityDamageEventModifiers, event, modifiers);
	}
	
	// -------------------------------------------- //
	// ACCESS > FUNCTIONS
	// -------------------------------------------- //
	
	@Override
	public Map<DamageModifier, Function<Double, Double>> getFunctions(EntityDamageEvent event)
	{
		return ReflectionUtil.getField(this.fieldEntityDamageEventFunctions, event);
	}
	
	@Override
	public void setModifierFunctions(EntityDamageEvent event, Map<DamageModifier, Function<Double, Double>> functions)
	{
		ReflectionUtil.setField(this.fieldEntityDamageEventFunctions, event, functions);
	}
	
}
