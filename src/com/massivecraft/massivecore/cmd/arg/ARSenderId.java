package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;

public class ARSenderId extends ARSenderIdAbstract<String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private ARSenderId(SenderIdSource source, boolean online)
	{
		super(source, online);
	}
	
	private ARSenderId(SenderIdSource source)
	{
		super(source);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static final ARSenderId i = new ARSenderId(SenderIdSourceMixinAllSenderIds.get());
	public static ARSenderId get() { return i; }
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public static ARSenderId get(SenderIdSource source, boolean online) { return new ARSenderId(source, online); }
	public static ARSenderId get(SenderIdSource source) { return new ARSenderId(source); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getResultForSenderId(String senderId)
	{
		return senderId;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (String id : IdUtil.getOnlineIds())
		{
			if ( ! Mixin.canSee(sender, id)) continue;
			ret.add(id);
		}
		
		return ret;
	}

}
