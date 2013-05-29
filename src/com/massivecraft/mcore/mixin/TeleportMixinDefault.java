package com.massivecraft.mcore.mixin;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.mcore.event.MCorePlayerPSTeleportEvent;
import com.massivecraft.mcore.ps.PS;
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
	
	public static void teleportPlayer(Player player, PS ps) throws TeleporterException
	{
		// Base the PS location on the entity location
		ps = ps.getEntity(true);
		ps = PS.valueOf(player.getLocation()).with(ps);
		
		// Bukkit Location
		Location location = null;
		try
		{
			location = ps.asBukkitLocation();
			
		}
		catch (Exception e)
		{
			throw new TeleporterException(Txt.parse("<b>Could not calculate the location: %s", e.getMessage()));
		}
		
		// eject passengers and unmount before transport
		player.eject();
		Entity vehicle = player.getVehicle();
		if (vehicle != null) vehicle.eject();
		
		// Do the teleport
		TeleportMixinCauseEngine.get().setMixinCausedTeleportIncoming(true);
		player.teleport(location);
		TeleportMixinCauseEngine.get().setMixinCausedTeleportIncoming(false);
		
		// Bukkit velocity
		Vector velocity = null;
		try
		{
			velocity = ps.asBukkitVelocity();
		}
		catch (Exception e)
		{
			return;
		}
		player.setVelocity(velocity);
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
			MCorePlayerPSTeleportEvent event = new MCorePlayerPSTeleportEvent(teleporteeId, Mixin.getSenderPs(teleporteeId), destinationPs);
			event.run();
			if (event.isCancelled()) return;
			if (event.getTo() == null) return;
			destinationPs = event.getTo();
			
			if (destinationDesc != null)
			{
				Mixin.msg(teleporteeId, "<i>Teleporting to <h>"+destinationDesc+"<i>.");
			}
			
			Player teleportee = SenderUtil.getPlayer(teleporteeId);
			if (teleportee != null)
			{
				teleportPlayer(teleportee, destinationPs);
			}
			else
			{
				Mixin.setSenderPs(teleporteeId, destinationPs);
			}
		}
	}
	
}