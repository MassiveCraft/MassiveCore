package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinPlayed;
import com.massivecraft.massivecore.mixin.MixinSenderPs;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		return MixinSenderPs.get().getSenderPs(this.playerId);
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
		ret += MixinDisplayName.get().getDisplayName(this.getPlayerId(), watcherObject);
		
		// Offline Suffix
		if (MixinPlayed.get().isOffline(this.getPlayerId()))
		{
			ret += Txt.parse(" <b>[Offline]");
		}
		
		return ret;
	}

}
