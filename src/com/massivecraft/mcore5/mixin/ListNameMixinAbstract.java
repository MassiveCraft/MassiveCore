package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.SenderUtil;

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