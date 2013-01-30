package com.massivecraft.mcore5.cmd.arg;

import org.bukkit.command.CommandSender;

public class ArgPredictateAnd<T> implements ArgPredictate<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final ArgPredictate<T> one;
	private final ArgPredictate<T> two;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ArgPredictateAnd(ArgPredictate<T> one, ArgPredictate<T>two)
	{
		this.one = one;
		this.two = two;
	}
	
	@Override
	public boolean apply(T type, String arg, CommandSender sender)
	{
		if (!this.one.apply(type, arg, sender)) return false;
		return this.two.apply(type, arg, sender);
	}

}
