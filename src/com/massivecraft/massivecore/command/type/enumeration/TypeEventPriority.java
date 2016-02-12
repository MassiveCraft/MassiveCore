package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.event.EventPriority;

public class TypeEventPriority extends TypeEnum<EventPriority>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeEventPriority i = new TypeEventPriority();
	public static TypeEventPriority get() { return i; }
	public TypeEventPriority()
	{
		super(EventPriority.class);
	}

}
