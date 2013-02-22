package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.SenderUtil;

public abstract class DisplayNameMixinAbstract implements DisplayNameMixin
{
	@Override
	public String getDisplayName(CommandSender sender)
	{
		return this.getDisplayName(SenderUtil.getSenderId(sender));
	}

	@Override
	public void setDisplayName(CommandSender sender, String displayName)
	{
		this.setDisplayName(SenderUtil.getSenderId(sender), displayName);
	}
}