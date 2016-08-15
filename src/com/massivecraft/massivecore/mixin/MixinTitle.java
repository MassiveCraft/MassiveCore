package com.massivecraft.massivecore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.nms.NmsPacket;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class MixinTitle extends MixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinTitle d = new MixinTitle();
	private static MixinTitle i = d;
	public static MixinTitle get() { return i; }
	public static void set(MixinTitle i) { MixinTitle.i = i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public boolean sendTitleMessage(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		// Get the player
		Player player = IdUtil.getPlayer(watcherObject);
		if (player == null) return false;
		
		// If we don't send any message (empty is ok) we might end up displaying old messages.
		if (titleSub == null) titleSub = "";
		if (titleMain == null) titleMain = "";

		titleSub = NmsPacket.toJson(titleSub);
		titleMain = NmsPacket.toJson(titleMain);
		
		return NmsPacket.sendTitle(player, ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
	public boolean sendTitleMsg(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		if (titleMain != null) titleMain = Txt.parse(titleMain);
		if (titleSub != null) titleSub = Txt.parse(titleSub);
		return this.sendTitleMessage(watcherObject, ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
	public boolean isTitlesAvailable()
	{
		return NmsPacket.get().isAvailable();
	}

}
