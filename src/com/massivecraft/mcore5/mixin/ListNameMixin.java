package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;

public interface ListNameMixin
{
	public String get(String senderId);
	public void set(String senderId, String listName);
	public String get(CommandSender sender);
	public void set(CommandSender sender, String listName);
}
