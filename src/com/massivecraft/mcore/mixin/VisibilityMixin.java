package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;

public interface VisibilityMixin
{
	public boolean canSee(String watcherId, String watcheeId);
	public boolean canSee(CommandSender watcher, String watcheeId);
	public boolean canSee(String watcherId, CommandSender watchee);
	public boolean canSee(CommandSender watcher, CommandSender watchee);
}
