package com.massivecraft.mcore.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.Predictate;

public interface MessageMixin
{
	// -------------------------------------------- //
	// RAW MESSAGE
	// -------------------------------------------- //
	
	// All
	public boolean message(String message);
	public boolean message(String... messages);
	public boolean message(Collection<String> messages);
	
	// Predictate
	public boolean message(Predictate<CommandSender> predictate, String message);
	public boolean message(Predictate<CommandSender> predictate, String... messages);
	public boolean message(Predictate<CommandSender> predictate, Collection<String> messages);
	
	// One
	public boolean message(CommandSender sendee, String message);
	public boolean message(CommandSender sendee, String... messages);
	public boolean message(CommandSender sendee, Collection<String> messages);
	
	// One by id
	public boolean message(String sendeeId, String message);
	public boolean message(String sendeeId, String... messages);
	public boolean message(String sendeeId, Collection<String> messages);
	
	// -------------------------------------------- //
	// PARSE MESSAGE
	// -------------------------------------------- //
	
	// All
	public boolean msg(String msg);
	public boolean msg(String msg, Object... args);
	public boolean msg(Collection<String> msgs);
	
	// Predictate
	public boolean msg(Predictate<CommandSender> predictate, String msg);
	public boolean msg(Predictate<CommandSender> predictate, String msg, Object... args);
	public boolean msg(Predictate<CommandSender> predictate, Collection<String> msgs);
	
	// One
	public boolean msg(CommandSender sendee, String msg);
	public boolean msg(CommandSender sendee, String msg, Object... args);
	public boolean msg(CommandSender sendee, Collection<String> msgs);
	
	// One by id
	public boolean msg(String sendeeId, String msg);
	public boolean msg(String sendeeId, String msg, Object... args);
	public boolean msg(String sendeeId, Collection<String> msgs);
}
