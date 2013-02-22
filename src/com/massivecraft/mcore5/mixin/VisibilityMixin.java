package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;

public interface VisibilityMixin
{
	public boolean isVisible(String watcherId, String watcheeId);
	public boolean isVisible(CommandSender watcher, String watcheeId);
	public boolean isVisible(String watcherId, CommandSender watchee);
	public boolean isVisible(CommandSender watcher, CommandSender watchee);
	
	public void setVisible(String watcherId, String watcheeId, boolean visible);
	public void setVisible(CommandSender watcher, String watcheeId, boolean visible);
	public void setVisible(String watcherId, CommandSender watchee, boolean visible);
	public void setVisible(CommandSender watcher, CommandSender watchee, boolean visible);
}
