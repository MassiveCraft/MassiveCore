package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;

public class ARPlayer extends ARSenderIdAbstract<Player>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ARPlayer i = new ARPlayer();
	public static ARPlayer get() { return i; }
	private ARPlayer()
	{
		super(SenderIdSourceMixinAllSenderIds.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Player getResultForSenderId(String senderId)
	{
		if (senderId == null) return null;
		return IdUtil.getPlayer(senderId);
	}

}
