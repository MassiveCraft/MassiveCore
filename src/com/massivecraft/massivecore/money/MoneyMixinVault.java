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
		Money.mixin(this);
	}
	
	public void deactivate()
	{
		Money.mixin(null);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public Economy getEconomy()
	{
		RegisteredServiceProvider<Economy> registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (registeredServiceProvider == null) return null;
		return registeredServiceProvider.getProvider();
	}
	
	// -------------------------------------------- //
	// ENABLED AND DISABLED
	// -------------------------------------------- //
	
	@Override
	public boolean enabled()
	{
		Economy economy = this.getEconomy();
		if (economy == null) return false;
		return economy.isEnabled();
	}
	
	// -------------------------------------------- //
	// FORMAT AND NAME
	// -------------------------------------------- //
	
	@Override
	public String format(double amount, boolean includeUnit)
	{
		if (includeUnit)
		{
			return this.getEconomy().format(amount);
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
		return this.getEconomy().currencyNameSingular();		
	}
	
	@Override
	public String plural()
	{
		return this.getEconomy().currencyNamePlural();
	}
	
	// -------------------------------------------- //
	// FRACTIONAL DIGITS
	// -------------------------------------------- //
	
	@Override
	public int fractionalDigits()
	{
		return this.getEconomy().fractionalDigits();
	}
	
	// -------------------------------------------- //
	// EXISTS AND CREATE
	// -------------------------------------------- //
	
	@Override
	public boolean exists(String accountId)
	{
		return this.getEconomy().hasAccount(accountId);
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
		return this.getEconomy().getBalance(accountId);
	}
	
	@Override
	public boolean has(String accountId, double amount)
	{
		this.ensureExists(accountId);
		return this.getEconomy().has(accountId, amount);
	}
	
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	@Override
	public boolean move(String fromId, String toId, String byId, double amount, Collection<String> categories, String message)
	{
		Economy economy = this.getEconomy();
		
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
		Economy economy = this.getEconomy();
		
		if (economy.hasAccount(accountId)) return true;
		
		if (!economy.createPlayerAccount(accountId)) return false;
		
		if (MUtil.isValidPlayerName(accountId)) return true;
		
		double balance = economy.getBalance(accountId);
		economy.withdrawPlayer(accountId, balance);
		
		return true;
	}

}
