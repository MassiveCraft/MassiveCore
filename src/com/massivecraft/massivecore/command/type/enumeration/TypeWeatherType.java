package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.WeatherType;

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

}
