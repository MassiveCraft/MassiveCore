package com.massivecraft.massivecore.command.type.container;

import java.util.Collection;

import org.bukkit.command.CommandSender;

public interface AllAble<T>
{
	public Collection<T> getAll(CommandSender sender);
}
