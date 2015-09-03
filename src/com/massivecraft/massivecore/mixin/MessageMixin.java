package com.massivecraft.massivecore.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Predictate;

public interface MessageMixin
{
	// -------------------------------------------- //
	// MSG
	// -------------------------------------------- //
	// All implemented in abstract!
	
	// All
	public boolean msgAll(String msg);
	public boolean msgAll(String msg, Object... args);
	public boolean msgAll(Collection<String> msgs);
	
	// Predicate
	public boolean msgPredictate(Predictate<CommandSender> predictate, String msg);
	public boolean msgPredictate(Predictate<CommandSender> predictate, String msg, Object... args);
	public boolean msgPredictate(Predictate<CommandSender> predictate, Collection<String> msgs);
	
	// One
	public boolean msgOne(Object sendeeObject, String msg);
	public boolean msgOne(Object sendeeObject, String msg, Object... args);
	public boolean msgOne(Object sendeeObject, Collection<String> msgs);
	
	// -------------------------------------------- //
	// MESSAGE
	// -------------------------------------------- //
	
	// All
	public boolean messageAll(Object message);
	public boolean messageAll(Object... messages);
	public boolean messageAll(Collection<?> messages);
	
	// Predicate
	public boolean messagePredictate(Predictate<CommandSender> predictate, Object message);
	public boolean messagePredictate(Predictate<CommandSender> predictate, Object... messages);
	public boolean messagePredictate(Predictate<CommandSender> predictate, Collection<?> messages);
	
	// One
	public boolean messageOne(Object sendeeObject, Object message);
	public boolean messageOne(Object sendeeObject, Object... messages);
	public boolean messageOne(Object sendeeObject, Collection<?> messages);
	
}
