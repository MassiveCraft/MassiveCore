package com.massivecraft.mcore4.cmd.arg;

import com.massivecraft.mcore4.cmd.MCommand;
import com.massivecraft.mcore4.store.PlayerColl;
import com.massivecraft.mcore4.store.PlayerEntity;

public class ARPlayerEntity<T extends PlayerEntity<T>> implements ArgReader<T>
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public ArgResult<T> read(String str, MCommand mcommand)
	{
		ArgResult<T> result = new ArgResult<T>();
		ArgResult<String> innerResult = this.stringReader().read(str, mcommand);
		
		if (innerResult.hasResult())
		{
			String playerName = innerResult.getResult();
			T entity = this.playerColl().get(playerName);
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
	
	protected PlayerColl<T> playerColl;
	public PlayerColl<T> playerColl() { return this.playerColl; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARPlayerEntity(ArgReader<String> stringReader, PlayerColl<T> playerColl)
	{
		this.stringReader = stringReader;
		this.playerColl = playerColl;
	}

}
