package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;

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
		// Null check is done in IdUtil
		return IdUtil.getPlayer(senderId);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Super Ret
		Collection<String> ret = super.getTabList(sender, arg);
		
		// Filter Ret
		Iterator<String> iter = ret.iterator();
		while (iter.hasNext())
		{
			String name = iter.next();
			if ( ! MUtil.isValidPlayerName(name))
			{
				iter.remove();
			}
		}
		
		// Return Ret
		return ret;
	}

}
