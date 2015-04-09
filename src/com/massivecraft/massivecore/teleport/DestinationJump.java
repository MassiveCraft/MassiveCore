package com.massivecraft.massivecore.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.ps.PS;

public class DestinationJump extends DestinationPlayer
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DestinationJump(String playerId)
	{
		super(playerId);
	}
	
	public DestinationJump(Object playerObject)
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
		
		Location location = DestinationUtil.getJumpLocation(player);
		
		return PS.valueOf(location);
	}
	
	@Override
	public String getDesc(Object watcherObject)
	{
		return "Jump for " + super.getDesc(watcherObject, false);
	}

}
