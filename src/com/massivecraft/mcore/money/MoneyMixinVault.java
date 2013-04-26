package com.massivecraft.mcore.money;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.massivecraft.mcore.util.MUtil;



import net.milkbowl.vault.economy.Economy;

public class MoneyMixinVault extends MoneyMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final MoneyMixinVault i = new MoneyMixinVault();
	public static MoneyMixinVault get() { return i; }
	
	// -------------------------------------------- //
	// ACTIVATE & DEACTIVATE
	// -------------------------------------------- //
	
	public void activate()
	{
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		this.economy = rsp.getProvider();
		Money.mixin(this);
	}
	
	public void deactivate()
	{
		Money.mixin(null);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Economy economy = null;
	
	// -------------------------------------------- //
	// ENABLED AND DISABLED
	// -------------------------------------------- //
	
	@Override
	public boolean enabled(String universe)
	{
		return this.economy.isEnabled();
	}
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	@Override
	public String format(String universe, double amount)
	{
		return this.economy.format(amount);
	}
	
	@Override
	public String singular(String universe)
	{
		return this.economy.currencyNameSingular();		
	}
	
	@Override
	public String plural(String universe)
	{
		return this.economy.currencyNamePlural();
	}
	
	// -------------------------------------------- //
	// EXISTS AND CREATE
	// -------------------------------------------- //
	
	@Override
	public boolean exists(String universe, String accountId)
	{
		return this.economy.hasAccount(accountId, universe);
	}
	
	@Override
	public boolean create(String universe, String accountId)
	{
		return this.ensureExists(universe, accountId);
	}
	
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	@Override
	public double get(String universe, String accountId)
	{
		this.ensureExists(universe, accountId);
		
		return this.economy.getBalance(accountId, universe);
	}
	
	@Override
	public boolean has(String universe, String accountId, double amount)
	{
		this.ensureExists(universe, accountId);
		
		return this.economy.has(accountId, universe, amount);
	}
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	@Override
	public boolean set(String universe, String accountId, double amount)
	{
		double current = get(universe, accountId);
		return add(universe, accountId, amount - current);
	}
	
	@Override
	public boolean add(String universe, String accountId, double amount)
	{
		if (amount < 0) return subtract(universe, accountId, -amount);
		
		this.ensureExists(universe, accountId);
		
		return economy.depositPlayer(accountId, universe, amount).transactionSuccess();
	}
	
	@Override
	public boolean subtract(String universe, String accountId, double amount)
	{
		if (amount < 0) return add(universe, accountId, -amount);
		
		this.ensureExists(universe, accountId);
		
		return economy.withdrawPlayer(accountId, universe, amount).transactionSuccess();
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public boolean ensureExists(String universe, String accountId)
	{
		if (this.economy.hasAccount(accountId, universe)) return true;
		
		if (!this.economy.createPlayerAccount(accountId, universe)) return false;
		
		if (MUtil.isValidPlayerName(accountId)) return true;
		
		double balance = this.economy.getBalance(accountId, universe);
		economy.withdrawPlayer(accountId, universe, balance);
		
		return true;
	}
	
}