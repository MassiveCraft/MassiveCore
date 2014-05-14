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
	public boolean messageAll(String message);
	public boolean messageAll(String... messages);
	public boolean messageAll(Collection<String> messages);
	
	// Predictate
	public boolean messagePredictate(Predictate<CommandSender> predictate, String message);
	public boolean messagePredictate(Predictate<CommandSender> predictate, String... messages);
	public boolean messagePredictate(Predictate<CommandSender> predictate, Collection<String> messages);
	
	// One
	public boolean messageOne(Object sendeeObject, String message);
	public boolean messageOne(Object sendeeObject, String... messages);
	public boolean messageOne(Object sendeeObject, Collection<String> messages);
	
	// -------------------------------------------- //
	// PARSE MESSAGE
	// -------------------------------------------- //
	
	// All
	public boolean msgAll(String msg);
	public boolean msgAll(String msg, Object... args);
	public boolean msgAll(Collection<String> msgs);
	
	// Predictate
	public boolean msgPredictate(Predictate<CommandSender> predictate, String msg);
	public boolean msgPredictate(Predictate<CommandSender> predictate, String msg, Object... args);
	public boolean msgPredictate(Predictate<CommandSender> predictate, Collection<String> msgs);
	
	// One
	public boolean msgOne(Object sendeeObject, String msg);
	public boolean msgOne(Object sendeeObject, String msg, Object... args);
	public boolean msgOne(Object sendeeObject, Collection<String> msgs);

}
