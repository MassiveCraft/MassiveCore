package com.massivecraft.mcore.money;

public class MoneyMixinDefault extends MoneyMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final MoneyMixinDefault i = new MoneyMixinDefault();
	public static MoneyMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// ENABLED AND DISABLED
	// -------------------------------------------- //
	
	@Override
	public boolean enabled(String universe)
	{
		return false;
	}
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	@Override
	public String format(String universe, double amount)
	{
		return String.valueOf(amount);
	}
	
	@Override
	public String singular(String universe)
	{
		return "singular";
	}
	
	@Override
	public String plural(String universe)
	{
		return "plural";
	}
	
	// -------------------------------------------- //
	// EXISTS AND CREATE
	// -------------------------------------------- //
	
	@Override
	public boolean exists(String universe, String accountId)
	{
		return false;
	}
	
	@Override
	public boolean create(String universe, String accountId)
	{
		return false;
	}
	
	// -------------------------------------------- //
	// GET AND SET
	// -------------------------------------------- //
	
	@Override
	public double get(String universe, String accountId)
	{
		return 0D;
	}
	
	@Override
	public boolean set(String universe, String accountId, double amount)
	{
		return false;
	}
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	@Override
	public boolean add(String universe, String accountId, double amount)
	{
		return false;
	}
	
	@Override
	public boolean subtract(String universe, String accountId, double amount)
	{
		return false;
	}
	
}