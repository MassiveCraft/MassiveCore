package com.massivecraft.mcore5;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.massivecraft.mcore5.util.Txt;

public class PSTeleporterDefault implements PSTeleporter
{
	@Override
	public void teleport(Entity entity, PS ps) throws PSTeleporterException
	{
		Location location = ps.calcLocation();		
		if (location == null) throw new PSTeleporterException(Txt.parse("<b>Could not calculate the location"));
		
		entity.teleport(location);
		
		Vector velocity = ps.getVelocity();
		if (velocity == null) return;
		
		entity.setVelocity(velocity);
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
