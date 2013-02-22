package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.SenderUtil;

public abstract class ListNameMixinAbstract implements ListNameMixin
{
	@Override
	public String getListName(CommandSender sender)
	{
		return this.getListName(SenderUtil.getSenderId(sender));
	}

	@Override
	public void setListName(CommandSender sender, String listName)
	{
		this.setListName(SenderUtil.getSenderId(sender), listName);
	}
}