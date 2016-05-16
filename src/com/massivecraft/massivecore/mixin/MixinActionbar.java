package com.massivecraft.massivecore.mixin;

import org.bukkit.entity.Player;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.nms.NmsPacket;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class MixinActionbar extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinActionbar d = new MixinActionbar();
	private static MixinActionbar i = d;
	public static MixinActionbar get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public boolean sendActionbarMessage(Object watcherObject, String message)
	{
		// Get the player
		Player player = IdUtil.getPlayer(watcherObject);
		if(player == null) return false;

		message = NmsPacket.toJson(message);

		return NmsPacket.sendActionbar(player, message);
	}

	public boolean sendActionbarMsg(Object watcherObject, String message)
	{
		return this.sendActionbarMessage(watcherObject, Txt.parse(message));
	}

	public boolean sendActionbarMson(Object watcherObject, Mson mson)
	{
		// Get the player
		Player player = IdUtil.getPlayer(watcherObject);
		if(player == null) return false;

		// Convert to raw
		String message = mson.toRaw();

		return NmsPacket.sendActionbar(player, message);
	}

	public boolean isActionbarAvailable()
	{
		return NmsPacket.get().isAvailable();
	}

}
