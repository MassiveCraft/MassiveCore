package com.massivecraft.mcore4.store;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.mcore4.store.idstrategy.IdStrategy;

public abstract class DriverAbstract<R> implements Driver<R>
{
	public DriverAbstract(String name)
	{
		this.name = name;
	}
	
	protected String name;
	@Override public String name() { return this.name; }

	protected Map<String, IdStrategy<?, ?>> idStrategies = new HashMap<String, IdStrategy<?, ?>>();
	@Override
	public boolean registerIdStrategy(IdStrategy<?, ?> idStrategy)
	{
		if (idStrategies.containsKey(idStrategy.getName())) return false;
		idStrategies.put(idStrategy.getName(), idStrategy);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <L> IdStrategy<L, ?> getIdStrategy(String idStrategyName)
	{
		IdStrategy<?, ?> idStrategy = idStrategies.get(idStrategyName);
		return (IdStrategy<L, ?>) idStrategy;
	}
}
