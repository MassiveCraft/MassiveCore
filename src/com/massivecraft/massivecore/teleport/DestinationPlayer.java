package com.massivecraft.massivecore.teleport;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class DestinationPlayer extends DestinationAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected String playerId;
	
	public String getPlayerId() { return this.playerId; }
	public void setPlayerId(String playerId) { this.playerId = playerId; }
	
	public void setPlayer(Object playerObject) { this.playerId = IdUtil.getId(playerObject); }
	public CommandSender getSender() { return IdUtil.getSender(this.playerId); }
	public Player getPlayer() { return IdUtil.getPlayer(this.playerId); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DestinationPlayer(String playerId)
	{
		this.setPlayerId(playerId);
	}
	
	public DestinationPlayer(Object playerObject)
	{
		this.setPlayer(playerObject);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PS getPsInner()
	{
		return Mixin.getSenderPs(this.playerId);
	}
	
	@Override
	public String getDesc(Object watcherObject)
	{
		return this.getDesc(watcherObject, true);
	}
	
	public String getDesc(Object watcherObject, boolean prefix)
	{
		String ret = "";
		
		// Player Prefix
		if (prefix)
		{
			ret += "Player ";
		}
		
		// Display Name
		ret += Mixin.getDisplayName(this.getPlayerId(), watcherObject);
		
		// Offline Suffix
		if (Mixin.isOffline(this.getPlayerId()))
		{
			ret += Txt.parse(" <b>[Offline]");
		}
		
		return ret;
	}

}
