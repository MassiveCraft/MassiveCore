package com.massivecraft.massivecore.money;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.massivecraft.massivecore.util.MUtil;

import net.milkbowl.vault.economy.Economy;

@SuppressWarnings("deprecation")
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
		if (Money.mixin() != null) return;
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return;
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
	public boolean enabled()
	{
		return this.economy.isEnabled();
	}
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	@Override
	public String format(double amount, boolean includeUnit)
	{
		if (includeUnit)
		{
			return this.economy.format(amount);
		}
		else
		{
			int fractionalDigits = this.fractionalDigits();
			if (fractionalDigits < 0)
			{
				return String.valueOf(amount);
			}
			else if (fractionalDigits == 0)
			{
				return String.valueOf((int)Math.round(amount));
			}
			else
			{
				return String.format("%." + fractionalDigits + "f", amount);
			}
		}
	}
	
	@Override
	public String singular()
	{
		return this.economy.currencyNameSingular();		
	}
	
	@Override
	public String plural()
	{
		return this.economy.currencyNamePlural();
	}
	
	// -------------------------------------------- //
	// FRACTIONAL DIGITS
	// -------------------------------------------- //
	
	@Override
	public int fractionalDigits()
	{
		return this.economy.fractionalDigits();
	}
	
	// -------------------------------------------- //
	// EXISTS AND CREATE
	// -------------------------------------------- //
	
	@Override
	public boolean exists(String accountId)
	{
		return this.economy.hasAccount(accountId);
	}
	
	@Override
	public boolean create(String accountId)
	{
		return this.ensureExists(accountId);
	}
	
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	@Override
	public double get(String accountId)
	{
		this.ensureExists(accountId);
		return this.economy.getBalance(accountId);
	}
	
	@Override
	public boolean has(String accountId, double amount)
	{
		this.ensureExists(accountId);
		return this.economy.has(accountId, amount);
	}
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	@Override
	public boolean move(String fromId, String toId, String byId, double amount, Collection<String> categories, String message)
	{
		// Ensure positive direction
		if (amount < 0)
		{
			amount *= -1;
			String temp = fromId;
			fromId = toId;
			toId = temp;
		}
		
		// Ensure the accounts exist
		if (fromId != null) this.ensureExists(fromId);
		if (toId != null) this.ensureExists(toId);
		
		// Subtract From
		if (fromId != null)
		{
			if (!economy.withdrawPlayer(fromId, amount).transactionSuccess())
			{
				return false;
			}
		}
		
		// Add To
		if (toId != null)
		{
			if (!economy.depositPlayer(toId, amount).transactionSuccess())
			{
				if (fromId != null)
				{
					// Undo the withdraw
					economy.depositPlayer(fromId, amount);
				}
				return false;
			}
		}
		
		return true;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public boolean ensureExists(String accountId)
	{
		if (this.economy.hasAccount(accountId)) return true;
		
		if (!this.economy.createPlayerAccount(accountId)) return false;
		
		if (MUtil.isValidPlayerName(accountId)) return true;
		
		double balance = this.economy.getBalance(accountId);
		economy.withdrawPlayer(accountId, balance);
		
		return true;
	}

}
