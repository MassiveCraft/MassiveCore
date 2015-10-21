package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;

import org.bukkit.command.CommandSender;

public interface TypeAllAble<T> extends Type<T>
{
	public Collection<T> getAll(CommandSender sender);
}
