package com.massivecraft.mcore5.mixin;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.mcore5.PS;
import com.massivecraft.mcore5.util.SenderUtil;
import com.massivecraft.mcore5.util.Txt;

public class TeleportMixinDefault extends TeleportMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TeleportMixinDefault i = new TeleportMixinDefault();
	public static TeleportMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// CORE LOGIC
	// -------------------------------------------- //
	
	public static void teleportEntity(Entity entity, PS ps) throws TeleporterException
	{
		// Use a local clone of the ps to avoid altering original
		ps = ps.clone();
		
		// Ensure the ps has a world name
		if (ps.getWorldName() == null)
		{
			ps.setWorldName(entity.getWorld().getName());
		}
		
		Location location = ps.calcLocation();		
		if (location == null) throw new TeleporterException(Txt.parse("<b>Could not calculate the location."));
		
		entity.teleport(location);
		
		Vector velocity = ps.getVelocity();
		if (velocity == null) return;
		
		entity.setVelocity(velocity);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, int delaySeconds) throws TeleporterException
	{
		this.sendPreTeleportMessage(teleportee, destinationDesc, delaySeconds);
		if (delaySeconds > 0)
		{
			new ScheduledTeleport(teleportee, destinationPs, destinationDesc, delaySeconds).schedule();
		}
		else
		{
			teleportEntity(teleportee, destinationPs);
		}
	}
	
	@Override
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, int delaySeconds) throws TeleporterException
	{
		validateTeleporteeId(teleporteeId);
		Player teleportee = SenderUtil.getPlayer(teleporteeId);
		this.teleport(teleportee, destinationPs, destinationDesc, delaySeconds);
	}
	
}