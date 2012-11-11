package com.massivecraft.mcore5.cmd.arg;

import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.mcore5.store.Coll;
import com.massivecraft.mcore5.store.Entity;

public class ARStringEntity<E extends Entity<E, String>> implements ArgReader<E>
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public ArgResult<E> read(String str, MCommand mcommand)
	{
		ArgResult<E> result = new ArgResult<E>();
		ArgResult<String> innerResult = this.stringReader().read(str, mcommand);
		
		if (innerResult.hasResult())
		{
			String playerName = innerResult.getResult();
			E entity = this.coll().get(playerName);
			result.setResult(entity);
		}
		else
		{
			result.setErrors(innerResult.getErrors());
		}
		
		return result;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected ArgReader<String> stringReader;
	public ArgReader<String> stringReader() { return this.stringReader; }
	
	protected Coll<E, String> coll;
	public Coll<E, String> coll() { return this.coll; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARStringEntity(ArgReader<String> stringReader, Coll<E, String> coll)
	{
		this.stringReader = stringReader;
		this.coll = coll;
	}

}
