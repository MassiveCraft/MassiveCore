package com.massivecraft.mcore.mixin;

import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;

public interface ActualMixin
{
	public boolean isActualJoin(PlayerJoinEvent event);
	public boolean isActualLeave(MCorePlayerLeaveEvent event);
}
