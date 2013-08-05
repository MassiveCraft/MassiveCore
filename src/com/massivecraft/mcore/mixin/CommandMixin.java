package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.store.SenderEntity;

public interface CommandMixin
{
	public boolean dispatchCommand(CommandSender sender, String commandLine);
	public boolean dispatchCommand(SenderEntity<?> sender, String commandLine);
	public boolean dispatchCommand(String sender, String commandLine);
}
