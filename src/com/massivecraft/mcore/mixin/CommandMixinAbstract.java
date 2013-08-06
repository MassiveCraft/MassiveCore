package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.util.SenderUtil;

public abstract class CommandMixinAbstract implements CommandMixin
{
	@Override
	public boolean dispatchCommand(CommandSender sender, String commandLine)
	{
		return this.dispatchCommand(SenderUtil.getSenderId(sender), SenderUtil.getSenderId(sender), commandLine);
	}
	
	@Override
	public boolean dispatchCommand(SenderEntity<?> sender, String commandLine)
	{
		return this.dispatchCommand(sender.getId(), sender.getId(), commandLine);
	}
	
	@Override
	public boolean dispatchCommand(String senderId, String commandLine)
	{
		return this.dispatchCommand(senderId, senderId, commandLine);
	}
}
