package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.type.TypePS;
import com.massivecraft.massivecore.command.type.TypeWorldId;
import com.massivecraft.massivecore.command.type.sender.TypeSenderId;
import com.massivecraft.massivecore.event.EventMassiveCoreDestination;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.teleport.Destination;
import com.massivecraft.massivecore.teleport.DestinationJump;
import com.massivecraft.massivecore.teleport.DestinationPlayer;
import com.massivecraft.massivecore.teleport.DestinationSimple;
import com.massivecraft.massivecore.teleport.DestinationThat;
import com.massivecraft.massivecore.teleport.DestinationThere;
import com.massivecraft.massivecore.teleport.DestinationTop;
import com.massivecraft.massivecore.teleport.DestinationUtil;
import com.massivecraft.massivecore.teleport.DestinationWorld;
import com.massivecraft.massivecore.util.IdUtil;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EngineMassiveCoreDestination extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreDestination i = new EngineMassiveCoreDestination();
	public static EngineMassiveCoreDestination get() { return i; }
	
	// -------------------------------------------- //
	// DESTINATION ARG
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void destinationPs(EventMassiveCoreDestination event)
	{
		try
		{
			PS ps = TypePS.get().read(event.getArg(), event.getSender());
			Destination destination = new DestinationSimple(ps);
			event.setDestination(destination);
		}
		catch (MassiveException exception)
		{
			event.setException(exception);
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void destinationArg(EventMassiveCoreDestination event)
	{
		final String arg = event.getArg().toLowerCase();
		final CommandSender sender = event.getSender();
		
		try
		{
			Destination destination = destinationArg(arg, sender);
			if (destination == null) return;
			event.setDestination(destination);
		}
		catch (MassiveException e)
		{
			event.setException(e);
		}
		catch (Exception e)
		{
			event.setException(new MassiveException().addMsg("<b>%s", e.getMessage()));
		}
		
		event.setCancelled(true);
	}
	
	public static final Set<String> ALIASES_TOP = new MassiveSet<>("top");
	public static final Set<String> ALIASES_THERE = new MassiveSet<>("there");
	public static final Set<String> ALIASES_THAT = new MassiveSet<>("that");
	public static final Set<String> ALIASES_JUMP = new MassiveSet<>("jump");
	public static final Set<String> ALIASES_WORLD = new MassiveSet<>("world", "w", "spawn", "wspawn", "worldspawn");
	public static final Set<String> ALIASES_PLAYER = new MassiveSet<>("player", "p", "here", "me", "self");
	
	public Destination destinationArg(String arg, CommandSender sender) throws MassiveException
	{
		// Prepare
		arg = arg.toLowerCase();
		
		List<String> parts = Arrays.asList(arg.split("[\\s\\,\\:]+", 2));
		String first = parts.get(0);
		String rest = null;
		if (parts.size() > 1) rest = parts.get(1);
		
		arg = arg.replace("\\s+", "");
		
		// TOP
		if (ALIASES_TOP.contains(arg))
		{
			Player player = DestinationUtil.getPlayer(sender);
			return new DestinationTop(player);
		}
		
		// THERE
		if (ALIASES_THERE.contains(arg))
		{
			Player player = DestinationUtil.getPlayer(sender);
			return new DestinationThere(player);
		}
		
		// THAT
		if (ALIASES_THAT.contains(arg))
		{
			Player player = DestinationUtil.getPlayer(sender);
			return new DestinationThat(player);
		}
		
		// JUMP
		if (ALIASES_JUMP.contains(arg))
		{
			Player player = DestinationUtil.getPlayer(sender);
			return new DestinationJump(player);
		}
		
		// World Explicit
		if (ALIASES_WORLD.contains(first))
		{
			String worldId;
			if (rest != null)
			{
				worldId = TypeWorldId.get().read(rest, sender);
			}
			else
			{
				Player player = DestinationUtil.getPlayer(sender);
				World world = player.getWorld();
				worldId = world.getName();
			}
			return new DestinationWorld(worldId);
		}
		
		// World Implicit
		try
		{
			String worldId = TypeWorldId.get().read(arg, sender);
			return new DestinationWorld(worldId);
		}
		catch (Exception e)
		{
			
		}
		
		// Player Explicit
		if (ALIASES_PLAYER.contains(first))
		{
			String playerId;
			if (rest != null)
			{
				playerId = TypeSenderId.get().read(rest, sender);
			}
			else
			{
				Player player = DestinationUtil.getPlayer(sender);
				playerId = IdUtil.getId(player);
			}
			return new DestinationPlayer(playerId);
		}
		
		// Player Implicit
		try
		{
			String playerId = TypeSenderId.get().read(arg, sender);
			return new DestinationPlayer(playerId);
		}
		catch (Exception e)
		{
			
		}
		
		// Null
		return null;
	}
	
}
