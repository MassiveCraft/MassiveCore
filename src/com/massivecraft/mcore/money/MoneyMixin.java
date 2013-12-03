package com.massivecraft.mcore.money;

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
	public String singular();
	public String plural();
	
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
	
	public boolean move(double amount, String causeId, String fromId, String toId, Collection<String> categories);
	public boolean move(double amount, String causeId, String fromId, String toId, String... categories);
	public boolean move(double amount, String causeId, String fromId, String toId);
	
	public boolean spawn(double amount, String causeId, String toId, Collection<String> categories);
	public boolean spawn(double amount, String causeId, String toId, String... categories);
	public boolean spawn(double amount, String causeId, String toId);
	
	public boolean despawn(double amount, String causeId, String fromId, Collection<String> categories);
	public boolean despawn(double amount, String causeId, String fromId, String... categories);
	public boolean despawn(double amount, String causeId, String fromId);
	
	public boolean set(double amount, String causeId, String accountId, Collection<String> categories);
	public boolean set(double amount, String causeId, String accountId, String... categories);
	public boolean set(double amount, String causeId, String accountId);
	
}
