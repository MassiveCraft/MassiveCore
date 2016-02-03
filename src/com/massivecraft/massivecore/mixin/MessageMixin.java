package com.massivecraft.massivecore.mixin;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.predicate.Predicate;

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
	public boolean msgPredicate(Predicate<CommandSender> predicate, String msg);
	public boolean msgPredicate(Predicate<CommandSender> predicate, String msg, Object... args);
	public boolean msgPredicate(Predicate<CommandSender> predicate, Collection<String> msgs);
	
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
	public boolean messagePredicate(Predicate<CommandSender> predicate, Object message);
	public boolean messagePredicate(Predicate<CommandSender> predicate, Object... messages);
	public boolean messagePredicate(Predicate<CommandSender> predicate, Collection<?> messages);
	
	// One
	public boolean messageOne(Object sendeeObject, Object message);
	public boolean messageOne(Object sendeeObject, Object... messages);
	public boolean messageOne(Object sendeeObject, Collection<?> messages);
	
}
