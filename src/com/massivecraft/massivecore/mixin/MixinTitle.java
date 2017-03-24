package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.nms.NmsChat;
import org.bukkit.entity.Player;

public class MixinTitle extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinTitle d = new MixinTitle();
	private static MixinTitle i = d;
	public static MixinTitle get() { return i; }
	
	// -------------------------------------------- //
	// AVAILABLE
	// -------------------------------------------- //
	
	@Override
	public boolean isAvailable()
	{
		return NmsChat.get().isAvailable();
	}
	
	// -------------------------------------------- //
	// SEND
	// -------------------------------------------- //
	
	public void sendTitleRaw(Player player, int ticksIn, int ticksStay, int ticksOut, String rawMain, String rawSub)
	{
		NmsChat.get().sendTitleRaw(player, ticksIn, ticksStay, ticksOut, rawMain, rawSub);
	}
	
	public void sendTitleMson(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, Mson msonMain, Mson msonSub)
	{
		NmsChat.get().sendTitleMson(watcherObject, ticksIn, ticksStay, ticksOut, msonMain, msonSub);
	}
	
	public void sendTitleMessage(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String messageMain, String messageSub)
	{
		NmsChat.get().sendTitleMessage(watcherObject, ticksIn, ticksStay, ticksOut, messageMain, messageSub);
	}
	
	public void sendTitleMsg(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String msgMain, String msgSub)
	{
		NmsChat.get().sendTitleMsg(watcherObject, ticksIn, ticksStay, ticksOut, msgMain, msgSub);
	}

}
