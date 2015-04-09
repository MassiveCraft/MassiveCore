package com.massivecraft.massivecore.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.ps.PS;

public class DestinationThat extends DestinationPlayer
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DestinationThat(String playerId)
	{
		super(playerId);
	}
	
	public DestinationThat(Object playerObject)
	{
		super(playerObject);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PS getPs()
	{
		Player player = this.getPlayer();
		if (player == null) return null;
		
		Location location = DestinationUtil.getThatLocation(player);
		
		return PS.valueOf(location);
	}
	
	@Override
	public String getDesc(Object watcherObject)
	{
		return "That for " + super.getDesc(watcherObject, false);
	}

}
