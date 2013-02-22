package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;

public interface KickMixin
{
	public boolean kick(CommandSender sender);
	public boolean kick(String senderId);
	
	public boolean kick(CommandSender sender, String message);
	public boolean kick(String senderId, String message);
}
