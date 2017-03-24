package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.nms.NmsChat;
import org.bukkit.entity.Player;

public class MixinActionbar extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinActionbar d = new MixinActionbar();
	private static MixinActionbar i = d;
	public static MixinActionbar get() { return i; }
	
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
	
	public void sendActionbarRaw(Player player, String raw)
	{
		NmsChat.get().sendActionbarRaw(player, raw);
	}
	
	public void sendActionbarMson(Object watcherObject, Mson mson)
	{
		NmsChat.get().sendActionbarMson(watcherObject, mson);
	}
	
	public void sendActionbarMessage(Object watcherObject, String message)
	{
		NmsChat.get().sendActionbarMessage(watcherObject, message);
	}

	public void sendActionbarMsg(Object watcherObject, String msg)
	{
		NmsChat.get().sendActionbarMsg(watcherObject, msg);
	}

}
