package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
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
		super(SenderIdSourceMixinAllSenderIds.get(), SenderPresence.LOCAL, SenderType.PLAYER);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Player getResultForSenderId(String senderId)
	{
		// Null check is done in IdUtil
		return IdUtil.getPlayer(senderId);
	}

}
