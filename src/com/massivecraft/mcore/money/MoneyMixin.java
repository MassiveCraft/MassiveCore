package com.massivecraft.mcore.money;

public interface MoneyMixin
{
	// -------------------------------------------- //
	// ENABLED
	// -------------------------------------------- //
	
	public boolean enabled(String universe);
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	public String format(String universe, double amount);
	public String singular(String universe);
	public String plural(String universe);
	
	// -------------------------------------------- //
	// EXISTS AND CREATE
	// -------------------------------------------- //
	
	public boolean exists(String universe, String accountId);
	public boolean create(String universe, String accountId);
	
	// -------------------------------------------- //
	// GET AND SET
	// -------------------------------------------- //
	
	public double get(String universe, String accountId);
	public boolean set(String universe, String accountId, double amount);
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	public boolean add(String universe, String accountId, double amount);
	public boolean subtract(String universe, String accountId, double amount);
	
}
