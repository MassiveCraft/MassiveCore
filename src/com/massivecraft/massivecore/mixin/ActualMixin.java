package com.massivecraft.massivecore.mixin;

import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;

public interface ActualMixin
{
	public boolean isActualJoin(PlayerJoinEvent event);
	public boolean isActualLeave(EventMassiveCorePlayerLeave event);
}
