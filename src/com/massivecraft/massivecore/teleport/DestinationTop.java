package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DestinationTop extends DestinationPlayer
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DestinationTop(String playerId)
	{
		super(playerId);
	}
	
	public DestinationTop(Object playerObject)
	{
		super(playerObject);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PS getPsInner()
	{
		Player player = this.getPlayer();
		if (player == null) return null;
		
		Location location = player.getLocation();
		location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
		
		return PS.valueOf(location);
	}
	
	@Override
	public String getDesc(Object watcherObject)
	{
		return "Top for " + super.getDesc(watcherObject, false);
	}

}
