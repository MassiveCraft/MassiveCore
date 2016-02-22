package com.massivecraft.massivecore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.nms.NmsPacket;
import com.massivecraft.massivecore.util.IdUtil;

public class ActionbarMixinDefault extends ActionbarMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ActionbarMixinDefault i = new ActionbarMixinDefault();
	public static ActionbarMixinDefault get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean sendActionbarMessage(Object watcherObject, String message)
	{
		// Get the player
		Player player = IdUtil.getPlayer(watcherObject);
		if(player == null) return false;

		message = NmsPacket.toJson(message);

		return NmsPacket.sendActionbar(player, message);
	}

	@Override
	public boolean sendActionbarMson(Object watcherObject, Mson mson)
	{
		// Get the player
		Player player = IdUtil.getPlayer(watcherObject);
		if(player == null) return false;

		// Convert to raw
		String message = mson.toRaw();

		return NmsPacket.sendActionbar(player, message);
	}

	@Override
	public boolean isActionbarAvailable()
	{
		return NmsPacket.get().isAvailable();
	}

}
