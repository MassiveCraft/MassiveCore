package com.massivecraft.massivecore.integration.liability;

import com.massivecraft.massivecore.Integration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class IntegrationLiabilityAreaEffectCloud extends Integration
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IntegrationLiabilityAreaEffectCloud i = new IntegrationLiabilityAreaEffectCloud();
	public static IntegrationLiabilityAreaEffectCloud get() { return i; }
	
	public IntegrationLiabilityAreaEffectCloud()
	{
		this.setClassName("org.bukkit.entity.AreaEffectCloud");
	}
	
	// Don't need to specify an engine here since the only methods we want to protect are static
	
	// -------------------------------------------- //
	// LIABILITY CALCULATION
	// -------------------------------------------- //
	
	public Entity getLiableDamager(EntityDamageByEntityEvent event)
	{
		if (!this.isIntegrationActive()) return null;
		return LiabilityCalculatorAreaEffectCloud.liability(event);
	}
	
}
