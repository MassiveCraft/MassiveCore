package com.massivecraft.mcore4;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class PSTeleporterDefault implements PSTeleporter
{
	@Override
	public void teleport(Entity entity, PS ps)
	{
		Location location = ps.locationCalc();
		if (location != null) entity.teleport(location);
		
		Vector velocity = ps.velocity();
		if (velocity != null) entity.setVelocity(velocity);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static PSTeleporterDefault instance = new PSTeleporterDefault();
	public static PSTeleporterDefault get()
	{
		return instance;
	}
}
