package com.massivecraft.mcore.mixin;

import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;

public interface ActuallMixin
{
	public boolean isActuallJoin(PlayerJoinEvent event);
	public boolean isActuallLeave(MCorePlayerLeaveEvent event);
}
