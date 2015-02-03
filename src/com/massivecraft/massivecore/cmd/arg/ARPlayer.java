package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.mixin.Mixin;
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
		// Null check is done in IdUtil :)
		return IdUtil.getPlayer(senderId);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (String name : IdUtil.getOnlineNames())
		{
			if ( ! MUtil.isValidPlayerName(name)) continue;
			if ( ! Mixin.canSee(sender, name)) continue;
			ret.add(name);
		}
		
		return ret;
	}

}
