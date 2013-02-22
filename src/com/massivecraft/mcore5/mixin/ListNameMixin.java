package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;

public interface ListNameMixin
{
	public String getListName(String senderId);
	public void setListName(String senderId, String listName);
	public String getListName(CommandSender sender);
	public void setListName(CommandSender sender, String listName);
}
