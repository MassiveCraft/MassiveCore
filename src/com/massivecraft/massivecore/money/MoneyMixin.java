package com.massivecraft.massivecore.money;

import java.util.Collection;

public interface MoneyMixin
{
	// -------------------------------------------- //
	// ENABLED
	// -------------------------------------------- //
	
	boolean enabled();
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	String format(double amount);
	String format(double amount, boolean includeUnit);
	String singular();
	String plural();
	
	// -------------------------------------------- //
	// FRACTIONAL DIGITS
	// -------------------------------------------- //
	
	int fractionalDigits();
	double prepare(double amount);
	
	// -------------------------------------------- //
	// EXISTANCE
	// -------------------------------------------- //
	
	boolean exists(String accountId);
	boolean create(String accountId);
	
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	double get(String accountId);
	boolean has(String accountId, double amount);
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	boolean move(String fromId, String toId, String byId, double amount, Collection<String> categories, Object message);
	boolean move(String fromId, String toId, String byId, double amount, String category, Object message);
	boolean move(String fromId, String toId, String byId, double amount, Collection<String> categories);
	boolean move(String fromId, String toId, String byId, double amount, String category);
	boolean move(String fromId, String toId, String byId, double amount);
	
	boolean spawn(String toId, String byId, double amount, Collection<String> categories, Object message);
	boolean spawn(String toId, String byId, double amount, String category, Object message);
	boolean spawn(String toId, String byId, double amount, Collection<String> categories);
	boolean spawn(String toId, String byId, double amount, String category);
	boolean spawn(String toId, String byId, double amount);
	
	boolean despawn(String fromId, String byId, double amount, Collection<String> categories, Object message);
	boolean despawn(String fromId, String byId, double amount, String category, Object message);
	boolean despawn(String fromId, String byId, double amount, Collection<String> categories);
	boolean despawn(String fromId, String byId, double amount, String category);
	boolean despawn(String fromId, String byId, double amount);
	
	boolean set(String accountId, String byId, double amount, Collection<String> categories, Object message);
	boolean set(String accountId, String byId, double amount, String category, Object message);
	boolean set(String accountId, String byId, double amount, Collection<String> categories);
	boolean set(String accountId, String byId, double amount, String category);
	boolean set(String accountId, String byId, double amount);
	
}
