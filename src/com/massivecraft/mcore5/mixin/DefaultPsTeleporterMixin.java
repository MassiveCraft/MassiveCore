package com.massivecraft.mcore5.mixin;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.massivecraft.mcore5.PS;
import com.massivecraft.mcore5.util.Txt;

public class DefaultPsTeleporterMixin implements PsTeleporterMixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DefaultPsTeleporterMixin i = new DefaultPsTeleporterMixin();
	public static DefaultPsTeleporterMixin get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void teleport(Entity entity, PS ps) throws PsTeleporterException
	{
		Location location = ps.calcLocation();		
		if (location == null) throw new PsTeleporterException(Txt.parse("<b>Could not calculate the location"));
		
		entity.teleport(location);
		
		Vector velocity = ps.getVelocity();
		if (velocity == null) return;
		
		entity.setVelocity(velocity);
	}

}