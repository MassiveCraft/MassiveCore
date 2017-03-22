package com.massivecraft.massivecore.nms;

import java.util.Map;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import com.google.common.base.Function;
import com.massivecraft.massivecore.mixin.Mixin;

public class NmsEntityDamageEvent extends Nms
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsEntityDamageEvent d = new NmsEntityDamageEvent().setAlternatives(
		NmsEntityDamageEvent17R4P.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsEntityDamageEvent i = d;
	public static NmsEntityDamageEvent get() { return i; }
	
	// -------------------------------------------- //
	// ACCESS > ORIGINALS
	// -------------------------------------------- //
	
	public Map<DamageModifier, Double> getOriginals(EntityDamageEvent event)
	{
		throw this.notImplemented();
	}
	
	public void setOriginals(EntityDamageEvent event, Map<DamageModifier, Double> originals)
	{
		throw this.notImplemented();
	}
	
	// -------------------------------------------- //
	// ACCESS > MODIFIERS
	// -------------------------------------------- //
	
	public Map<DamageModifier, Double> getModifiers(EntityDamageEvent event)
	{
		throw this.notImplemented();
	}
	
	public void setModifiers(EntityDamageEvent event, Map<DamageModifier, Double> modifiers)
	{
		throw this.notImplemented();
	}
	
	// -------------------------------------------- //
	// ACCESS > FUNCTIONS
	// -------------------------------------------- //
	
	public Map<DamageModifier, Function<Double, Double>> getFunctions(EntityDamageEvent event)
	{
		throw this.notImplemented();
	}
	
	public void setModifierFunctions(EntityDamageEvent event, Map<DamageModifier, Function<Double, Double>> functions)
	{
		throw this.notImplemented();
	}
	
}
