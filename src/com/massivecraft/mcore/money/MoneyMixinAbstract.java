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
	
	// this is the abstract one
	/*
	public boolean move(String fromId, String toId, String byId, double amount, Collection<String> categories)
	{
		// TODO Auto-generated method stub
		return false;
	}*/
	
	public boolean move(String fromId, String toId, String byId, double amount, String... categories)
	{
		return this.move(fromId, toId, byId, amount, Arrays.asList(categories));
	}
	
	public boolean move(String fromId, String toId, String byId, double amount)
	{
		return this.move(fromId, toId, byId, amount, new ArrayList<String>());
	}
	
	// SPAWN
	
	public boolean spawn(String toId, String byId, double amount, Collection<String> categories)
	{
		// Based on Move
		return this.move(null, toId, byId, amount, categories);
	}
	
	public boolean spawn(String toId, String byId, double amount, String... categories)
	{
		return this.spawn(toId, byId, amount, Arrays.asList(categories));
	}
	
	public boolean spawn(String toId, String byId, double amount)
	{
		return this.spawn(toId, byId, amount, new ArrayList<String>());
	}
	
	// DESPAWN
	
	public boolean despawn(String fromId, String byId, double amount, Collection<String> categories)
	{
		// Based on Move
		return this.move(fromId, null, byId, amount, categories);
	}
	
	public boolean despawn(String fromId, String byId, double amount, String... categories)
	{
		return this.despawn(fromId, byId, amount, Arrays.asList(categories));
	}
	
	public boolean despawn(String fromId, String byId, double amount)
	{
		return this.despawn(fromId, byId, amount, new ArrayList<String>());
	}
	
	// SET
	
	public boolean set(String accountId, String byId, double amount, Collection<String> categories)
	{
		// Based on Move
		return this.move(null, accountId, byId, amount - this.get(accountId), categories);
	}
	
	public boolean set(String accountId, String byId, double amount, String... categories)
	{
		return this.set(accountId, byId, amount, Arrays.asList(categories));
	}
	
	public boolean set(String accountId, String byId, double amount)
	{
		return this.set(accountId, byId, amount, new ArrayList<String>());
	}
	
}