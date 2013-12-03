package com.massivecraft.mcore.money;

import java.util.Collection;

import com.massivecraft.mcore.util.MUtil;

public class Money
{
	// -------------------------------------------- //
	// MIXIN
	// -------------------------------------------- //
	
	private static MoneyMixin mixin = null;
	public static MoneyMixin mixin() { return mixin; };
	public static void mixin(MoneyMixin mixin) { Money.mixin = mixin; }
	
	// -------------------------------------------- //
	// EXTRACT
	// -------------------------------------------- //
	
	public static String accountId(Object account)
	{
		String ret = MUtil.extract(String.class, "accountId", account);
		if (ret == null) throw new IllegalArgumentException("extraction of accountId from object failed");
		return ret;
	}
	
	// -------------------------------------------- //
	// ENABLED
	// -------------------------------------------- //
	
	public static boolean enabled()
	{
		if (mixin == null) return false;
		return mixin.enabled();
	}
	
	public static boolean disabled()
	{
		return !enabled();
	}
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	public static String format(double amount)
	{
		if (disabled()) return String.valueOf(amount);
		return mixin.format(amount);
	}
	
	public static String singular()
	{
		if (disabled()) return "singular";
		return mixin.singular();
	}
	
	public static String plural()
	{
		if (disabled()) return "plural";
		return mixin.plural();
	}
	
	// -------------------------------------------- //
	// EXISTANCE
	// -------------------------------------------- //
	
	public static boolean exists(Object account)
	{
		if (disabled()) return false;
		return mixin.exists(accountId(account));
	}
	
	public static boolean create(Object account)
	{
		if (disabled()) return false;
		return mixin.create(accountId(account));
	}
	
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	public static double get(Object account)
	{
		if (disabled()) return 0D;
		return mixin.get(accountId(account));
	}
	
	public static boolean has(Object account, double amount)
	{
		if (disabled()) return false;
		return mixin.has(accountId(account), amount);
	}
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	// MOVE
	
	public static boolean move(double amount, Object cause, Object from, Object to, Collection<String> categories)
	{
		if (disabled()) return false;
		return mixin.move(amount, accountId(cause), accountId(from), accountId(to), categories);
	}
	
	public static boolean move(double amount, Object cause, Object from, Object to, String... categories)
	{
		if (disabled()) return false;
		return mixin.move(amount, accountId(cause), accountId(from), accountId(to), categories);
	}
	
	public static boolean move(double amount, Object cause, Object from, Object to)
	{
		if (disabled()) return false;
		return mixin.move(amount, accountId(cause), accountId(from), accountId(to));
	}
	
	// SPAWN
	
	public static boolean spawn(double amount, Object cause, Object to, Collection<String> categories)
	{
		if (disabled()) return false;
		return mixin.spawn(amount, accountId(cause), accountId(to), categories);
	}
	
	public static boolean spawn(double amount, Object cause, Object to, String... categories)
	{
		if (disabled()) return false;
		return mixin.spawn(amount, accountId(cause), accountId(to), categories);
	}
	
	public static boolean spawn(double amount, Object cause, Object toId)
	{
		if (disabled()) return false;
		return mixin.spawn(amount, accountId(cause), accountId(toId));
	}
	
	// DESPAWN
	
	public static boolean despawn(double amount, Object cause, Object from, Collection<String> categories)
	{
		if (disabled()) return false;
		return mixin.despawn(amount, accountId(cause), accountId(from), categories);
	}
	
	public static boolean despawn(double amount, Object cause, Object from, String... categories)
	{
		if (disabled()) return false;
		return mixin.despawn(amount, accountId(cause), accountId(from), categories);
	}
	
	public static boolean despawn(double amount, Object cause, Object from)
	{
		if (disabled()) return false;
		return mixin.despawn(amount, accountId(cause), accountId(from));
	}
	
	// SET
	
	public static boolean set(double amount, Object cause, Object account, Collection<String> categories)
	{
		if (disabled()) return false;
		return mixin.set(amount, accountId(cause), accountId(account), categories);
	}
	
	public static boolean set(double amount, Object cause, Object account, String... categories)
	{
		if (disabled()) return false;
		return mixin.set(amount, accountId(cause), accountId(account), categories);
	}
	
	public static boolean set(double amount, Object cause, Object account)
	{
		if (disabled()) return false;
		return mixin.set(amount, accountId(cause), accountId(account));
	}
	
}
