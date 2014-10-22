package com.massivecraft.massivecore.money;

import java.util.Collection;

public interface MoneyMixin
{
	// -------------------------------------------- //
	// ENABLED
	// -------------------------------------------- //
	
	public boolean enabled();
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	public String format(double amount);
	public String format(double amount, boolean includeUnit);
	public String singular();
	public String plural();
	
	// -------------------------------------------- //
	// FRACTIONAL DIGITS
	// -------------------------------------------- //
	
	public int fractionalDigits();
	public double prepare(double amount);
	
	// -------------------------------------------- //
	// EXISTANCE
	// -------------------------------------------- //
	
	public boolean exists(String accountId);
	public boolean create(String accountId);
	
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	public double get(String accountId);
	public boolean has(String accountId, double amount);
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	public boolean move(String fromId, String toId, String byId, double amount, Collection<String> categories, String message);
	public boolean move(String fromId, String toId, String byId, double amount, String category, String message);
	public boolean move(String fromId, String toId, String byId, double amount, Collection<String> categories);
	public boolean move(String fromId, String toId, String byId, double amount, String category);
	public boolean move(String fromId, String toId, String byId, double amount);
	
	public boolean spawn(String toId, String byId, double amount, Collection<String> categories, String message);
	public boolean spawn(String toId, String byId, double amount, String category, String message);
	public boolean spawn(String toId, String byId, double amount, Collection<String> categories);
	public boolean spawn(String toId, String byId, double amount, String category);
	public boolean spawn(String toId, String byId, double amount);
	
	public boolean despawn(String fromId, String byId, double amount, Collection<String> categories, String message);
	public boolean despawn(String fromId, String byId, double amount, String category, String message);
	public boolean despawn(String fromId, String byId, double amount, Collection<String> categories);
	public boolean despawn(String fromId, String byId, double amount, String category);
	public boolean despawn(String fromId, String byId, double amount);
	
	public boolean set(String accountId, String byId, double amount, Collection<String> categories, String message);
	public boolean set(String accountId, String byId, double amount, String category, String message);
	public boolean set(String accountId, String byId, double amount, Collection<String> categories);
	public boolean set(String accountId, String byId, double amount, String category);
	public boolean set(String accountId, String byId, double amount);
	
}
