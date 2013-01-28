package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;

public interface DisplayNameMixin
{
	public String get(String senderId);
	public void set(String senderId, String displayName);
	public String get(CommandSender sender);
	public void set(CommandSender sender, String displayName);
}
