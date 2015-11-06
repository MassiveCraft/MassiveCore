package com.massivecraft.massivecore.command.type.sender;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;

public class TypePlayer extends TypeSenderIdAbstract<Player>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final TypePlayer i = new TypePlayer();
	public static TypePlayer get() { return i; }
	private TypePlayer()
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
