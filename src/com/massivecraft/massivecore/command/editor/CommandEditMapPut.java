package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.Type;

import java.util.Map;

public class CommandEditMapPut<O, V extends Map<?,?>> extends CommandEditMapAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CommandEditMapPut(EditSettings<O> settings, Property<O, V> property, Type<?> mapValueType)
	{
		// Super	
		super(settings, property, mapValueType);
		
		// Parameters
		this.addParameter(this.getMapKeyType(), this.getMapKeyType().getTypeName());
		this.addParameter(this.getMapValueType(), this.getMapValueType().getTypeName());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Map<Object, Object> alter(Map<Object, Object> map) throws MassiveException
	{
		// Args
		Object key = this.readArg();
		Object value = this.readArg();
		
		// Alter
		map.put(key, value);

		// Return
		return map;
	}	
	
}
