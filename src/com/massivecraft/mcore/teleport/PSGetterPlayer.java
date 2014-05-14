package com.massivecraft.mcore.teleport;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.util.IdUtil;

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
