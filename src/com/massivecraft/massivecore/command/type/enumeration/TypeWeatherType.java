package com.massivecraft.massivecore.command.type.enumeration;

import java.util.Set;

import org.bukkit.WeatherType;

import com.massivecraft.massivecore.collections.MassiveSet;

public class TypeWeatherType extends TypeEnum<WeatherType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeWeatherType i = new TypeWeatherType();
	public static TypeWeatherType get() { return i; }
	public TypeWeatherType()
	{
		super(WeatherType.class);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Set<String> getNamesInner(WeatherType value)
	{
		Set<String> ret = new MassiveSet<String>(super.getNamesInner(value));
		
		if (value == WeatherType.DOWNFALL)
		{
			ret.add("Rain");
		}
		
		return ret;
	}

}
