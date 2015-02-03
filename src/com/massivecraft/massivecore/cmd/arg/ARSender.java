package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;

public class ARSender extends ARSenderIdAbstract<CommandSender>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ARSender i = new ARSender();
	public static ARSender get() { return i; }
	private ARSender()
	{
		super(SenderIdSourceMixinAllSenderIds.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public CommandSender getResultForSenderId(String senderId)
	{
		//Null check is done in IdUtil
		return IdUtil.getSender(senderId);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (String name : IdUtil.getOnlineNames())
		{
			if ( ! Mixin.canSee(sender, name)) continue;
			ret.add(name);
		}
		
		return ret;
	}

}
