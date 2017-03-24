package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.command.type.sender.TypeSender;
import org.bukkit.command.CommandSender;

public class PropertyUsed<V> extends Property<CommandSender, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PropertyUsed(EditSettings<V> settings, V used)
	{
		super(TypeSender.get(), settings.getObjectType(), "used " + settings.getObjectType().getName());
		this.addRequirements(settings.getUsedRequirements());
		this.used = used;
	}
	
	public PropertyUsed(EditSettings<V> settings)
	{
		this(settings, null);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	// We provide a default implementation with "the used" stored internally.
	// This makes sense for a few cases.
	// Such as when we edit the same instance all the time, such as a configuration.
	// Most of the time these methods will however be overridden.
	
	private V used = null;
	
	@Override
	public V getRaw(CommandSender sender)
	{
		return this.used;
	}
	
	@Override
	public CommandSender setRaw(CommandSender sender, V used)
	{
		this.used = used;
		return sender;
	}

}
