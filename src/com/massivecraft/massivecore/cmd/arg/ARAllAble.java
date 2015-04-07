package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

public interface ARAllAble<T> extends AR<T>
{
	public Collection<T> getAll(CommandSender sender);
}
