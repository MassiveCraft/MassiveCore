package com.massivecraft.massivecore.teleport;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.IdUtil;

public final class PSGetterPlayer extends PSGetterAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String senderId;
	public String getSenderId() { return this.senderId; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private PSGetterPlayer(String senderId)
	{
		this.senderId = senderId;
	}
	
	// -------------------------------------------- //
	// VALUE OF
	// -------------------------------------------- //
	
	public static PSGetterPlayer valueOf(CommandSender player)
	{
		return new PSGetterPlayer(IdUtil.getId(player));
	}
	
	public static PSGetterPlayer valueOf(SenderEntity<?> playerEntity)
	{
		return new PSGetterPlayer(playerEntity.getId());
	}
	
	public static PSGetterPlayer valueOf(String playerId)
	{
		return new PSGetterPlayer(playerId);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PS getPS()
	{
		return Mixin.getSenderPs(this.senderId);
	}

}
