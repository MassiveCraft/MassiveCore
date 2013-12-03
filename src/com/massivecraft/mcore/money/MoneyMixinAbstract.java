package com.massivecraft.mcore.money;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class MoneyMixinAbstract implements MoneyMixin
{
	// -------------------------------------------- //
	// MODIFY
	// -------------------------------------------- //
	
	// MOVE
	
	/*public boolean move(double amount, String causeId, String fromId, String toId, Collection<String> categories)
	{
		// LOL this one shall be abstract still :3
	}*/
	
	public boolean move(double amount, String causeId, String fromId, String toId, String... categories)
	{
		return this.move(amount, causeId, fromId, toId, Arrays.asList(categories));
	}
	
	public boolean move(double amount, String causeId, String fromId, String toId)
	{
		return this.move(amount, causeId, fromId, toId, new ArrayList<String>());
	}
	
	// SPAWN
	
	public boolean spawn(double amount, String causeId, String toId, Collection<String> categories)
	{
		// Based on Move
		return this.move(amount, causeId, null, toId, categories);
	}
	
	public boolean spawn(double amount, String causeId, String toId, String... categories)
	{
		return this.spawn(amount, causeId, toId, Arrays.asList(categories));
	}
	
	public boolean spawn(double amount, String causeId, String toId)
	{
		return this.spawn(amount, causeId, toId, new ArrayList<String>());
	}
	
	// DESPAWN
	
	public boolean despawn(double amount, String causeId, String fromId, Collection<String> categories)
	{
		// Based on Move
		return this.move(amount, causeId, fromId, null, categories);
	}
	
	public boolean despawn(double amount, String causeId, String fromId, String... categories)
	{
		return this.despawn(amount, causeId, fromId, Arrays.asList(categories));
	}
	
	public boolean despawn(double amount, String causeId, String fromId)
	{
		return this.despawn(amount, causeId, fromId, new ArrayList<String>());
	}
	
	// SET
	
	public boolean set(double amount, String causeId, String accountId, Collection<String> categories)
	{
		// Based on Move
		return this.move(amount - this.get(accountId), causeId, null, accountId, categories);
	}
	
	public boolean set(double amount, String causeId, String accountId, String... categories)
	{
		return this.set(amount, causeId, accountId, Arrays.asList(categories));
	}
	
	public boolean set(double amount, String causeId, String accountId)
	{
		return this.set(amount, causeId, accountId, new ArrayList<String>());
	}
	
}