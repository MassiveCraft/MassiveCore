package com.massivecraft.mcore.money;

import com.massivecraft.mcore.util.MUtil;

public class Money
{
	// -------------------------------------------- //
	// MIXIN
	// -------------------------------------------- //
	
	private static MoneyMixin mixin = null;
	public static MoneyMixin mixin() { return mixin; };
	public static void mixin(MoneyMixin newMixin) { mixin = newMixin; }
	
	// -------------------------------------------- //
	// EXTRACT
	// -------------------------------------------- //
	
	public static String universe(Object universe)
	{
		String ret = MUtil.extract(String.class, "moneyUniverse", universe);
		if (ret == null) throw new IllegalArgumentException("extraction of universe from object failed");
		return ret;
	}
	
	public static String accountId(Object accountId)
	{
		String ret = MUtil.extract(String.class, "accountId", accountId);
		if (ret == null) throw new IllegalArgumentException("extraction of accountId from object failed");
		return ret;
	}
	
	// -------------------------------------------- //
	// ENABLED
	// -------------------------------------------- //
	
	public static boolean enabled(Object universe)
	{
		if (mixin == null) return false;
		return mixin.enabled(universe(universe));
	}
	
	public static boolean disabled(Object universe)
	{
		return !enabled(universe);
	}
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	public static String format(Object universe, double amount)
	{
		if (disabled(universe)) return String.valueOf(amount);
		return mixin.format(universe(universe), amount);
	}
	
	public static String singular(Object universe)
	{
		if (disabled(universe)) return "singular";
		return mixin.singular(universe(universe));
	}
	
	public static String plural(Object universe)
	{
		if (disabled(universe)) return "plural";
		return mixin.plural(universe(universe));
	}
	
	// -------------------------------------------- //
	// EXISTS AND CREATE
	// -------------------------------------------- //
	
	public static boolean exists(Object universe, Object accountId)
	{
		if (disabled(universe)) return false;
		return mixin.exists(universe(universe), accountId(accountId));
	}
	
	public static boolean create(Object universe, Object accountId)
	{
		if (disabled(universe)) return false;
		return mixin.create(universe(universe), accountId(accountId));
	}
	
	// -------------------------------------------- //
	// GET AND SET
	// -------------------------------------------- //
	
	public static double get(Object universe, Object accountId)
	{
		if (disabled(universe)) return 0D;
		return mixin.get(universe(universe), accountId(accountId));
	}
	
	public static boolean set(Object universe, Object accountId, double amount)
	{
		if (disabled(universe)) return false;
		return mixin.set(universe(universe), accountId(accountId), amount);
	}
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	public static boolean add(Object universe, Object accountId, double amount)
	{
		if (disabled(universe)) return false;
		return mixin.add(universe(universe), accountId(accountId), amount);
	}
	
	public static boolean subtract(Object universe, Object accountId, double amount)
	{
		if (disabled(universe)) return false;
		return mixin.subtract(universe(universe), accountId(accountId), amount);
	}
	
}
