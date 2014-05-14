package com.massivecraft.mcore.mixin;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.mcore.event.MCorePlayerPSTeleportEvent;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.teleport.PSGetter;
import com.massivecraft.mcore.teleport.ScheduledTeleport;
import com.massivecraft.mcore.util.IdUtil;
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
		EngineTeleportMixinCause.get().setMixinCausedTeleportIncoming(true);
		player.teleport(location);
		EngineTeleportMixinCause.get().setMixinCausedTeleportIncoming(false);
		
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
	public void teleport(Object teleporteeObject, PSGetter toGetter, String desc, int delaySeconds) throws TeleporterException
	{
		String teleporteeId = IdUtil.getId(teleporteeObject);
		if (!IdUtil.isPlayerId(teleporteeId)) throw new TeleporterException(Txt.parse("<white>%s <b>is not a player.", Mixin.getDisplayName(teleporteeId)));
		
		if (delaySeconds > 0)
		{
			// With delay
			if (desc != null)
			{
				Mixin.msgOne(teleporteeId, "<i>Teleporting to <h>"+desc+" <i>in <h>"+delaySeconds+"s <i>unless you move.");
			}
			else
			{
				Mixin.msgOne(teleporteeId, "<i>Teleporting in <h>"+delaySeconds+"s <i>unless you move.");
			}
			
			new ScheduledTeleport(teleporteeId, toGetter, desc, delaySeconds).schedule();
		}
		else
		{
			// Without delay AKA "now"/"at once"
			
			// Resolve the getter
			PS to = toGetter.getPS();
			
			// Run event
			MCorePlayerPSTeleportEvent event = new MCorePlayerPSTeleportEvent(teleporteeId, Mixin.getSenderPs(teleporteeId), to, desc);
			event.run();
			if (event.isCancelled()) return;
			if (event.getTo() == null) return;
			to = event.getTo();
			desc = event.getDesc();
			
			if (desc != null)
			{
				Mixin.msgOne(teleporteeId, "<i>Teleporting to <h>"+desc+"<i>.");
			}
			
			Player teleportee = IdUtil.getPlayer(teleporteeId);
			if (teleportee != null)
			{
				teleportPlayer(teleportee, to);
			}
			else
			{
				Mixin.setSenderPs(teleporteeId, to);
			}
		}
	}
	
}