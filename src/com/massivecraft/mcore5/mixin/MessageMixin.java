package com.massivecraft.mcore5.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;

public interface MessageMixin
{
	public boolean message(CommandSender sender, String message);
	public boolean message(CommandSender sender, Collection<String> messages);
	public boolean message(CommandSender sender, String... messages);
	
	public boolean message(String senderId, String message);
	public boolean message(String senderId, Collection<String> messages);
	public boolean message(String senderId, String... messages);
	
	public boolean msg(CommandSender sender, String msg);
	public boolean msg(CommandSender sender, String msg, Object... args);
	public boolean msg(CommandSender sender, Collection<String> msgs);
	
	public boolean msg(String senderId, String msg);
	public boolean msg(String senderId, String msg, Object... args);
	public boolean msg(String senderId, Collection<String> msgs);
}
