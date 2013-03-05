package com.massivecraft.mcore.mixin;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.mcore.PS;
import com.massivecraft.mcore.event.MCorePlayerPSTeleportEvent;
import com.massivecraft.mcore.util.SenderUtil;
import com.massivecraft.mcore.util.Txt;

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
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, int delaySeconds) throws TeleporterException
	{
		if (!SenderUtil.isPlayerId(teleporteeId)) throw new TeleporterException(Txt.parse("<white>%s <b>is not a player.", Mixin.getDisplayName(teleporteeId)));
		
		if (delaySeconds > 0)
		{
			// With delay
			if (destinationDesc != null)
			{
				Mixin.msg(teleporteeId, "<i>Teleporting to <h>"+destinationDesc+" <i>in <h>"+delaySeconds+"s <i>unless you move.");
			}
			else
			{
				Mixin.msg(teleporteeId, "<i>Teleporting in <h>"+delaySeconds+"s <i>unless you move.");
			}
			
			new ScheduledTeleport(teleporteeId, destinationPs, destinationDesc, delaySeconds).schedule();
		}
		else
		{
			// Without delay AKA "now"/"at once"
			
			// Run event
			MCorePlayerPSTeleportEvent event = new MCorePlayerPSTeleportEvent(teleporteeId, Mixin.getSenderPs(teleporteeId), destinationPs.clone());
			event.run();
			if (event.isCancelled()) return;
			if (event.getTo() == null) return;
			destinationPs = event.getTo().clone();
			
			if (destinationDesc != null)
			{
				Mixin.msg(teleporteeId, "<i>Teleporting to <h>"+destinationDesc+"<i>.");
			}
			
			Player teleportee = SenderUtil.getPlayer(teleporteeId);
			if (teleportee != null)
			{
				teleportEntity(teleportee, destinationPs);
			}
			else
			{
				Mixin.setSenderPs(teleporteeId, destinationPs.clone());
			}
		}
	}
	
}