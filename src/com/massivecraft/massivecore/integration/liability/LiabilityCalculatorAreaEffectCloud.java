package com.massivecraft.massivecore.integration.liability;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class LiabilityCalculatorAreaEffectCloud
{
	// -------------------------------------------- //
	// LIABILITY CALCULATION
	// -------------------------------------------- //
	
	public static Entity liability(EntityDamageByEntityEvent event)
	{
		Entity liable = event.getDamager();
		if (!(liable instanceof AreaEffectCloud)) return null;
		
		AreaEffectCloud cloud = (AreaEffectCloud) liable;
		ProjectileSource source = cloud.getSource();
		return source instanceof Entity ? (Entity) source : null;
	}
	
}
