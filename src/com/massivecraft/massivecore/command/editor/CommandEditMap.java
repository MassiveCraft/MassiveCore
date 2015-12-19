package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.command.type.Type;

import java.util.Map;

public class CommandEditMap<O, V extends Map<?, ?>> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CommandEditMap(EditSettings<O> settings, Property<O, V> property, Type<?> mapValueType)
	{
		// Super
		super(settings, property, null);
		
		// Children
		this.addChild(new CommandEditShow<O, V>(settings, property));
		this.addChild(new CommandEditCreate<O, V>(settings, property));
		this.addChild(new CommandEditDelete<O, V>(settings, property));
		
		this.addChild(new CommandEditMapPut<O, V>(settings, property, mapValueType));
		this.addChild(new CommandEditMapRemove<O, V>(settings, property, mapValueType));
		this.addChild(new CommandEditMapClear<O, V>(settings, property, mapValueType));
	}
	
}
