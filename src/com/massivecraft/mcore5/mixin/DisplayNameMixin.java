package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;

public interface DisplayNameMixin
{
	public String getDisplayName(String senderId);
	public void setDisplayName(String senderId, String displayName);
	public String getDisplayName(CommandSender sender);
	public void setDisplayName(CommandSender sender, String displayName);
}
