package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
	public PS getPsInner()
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
