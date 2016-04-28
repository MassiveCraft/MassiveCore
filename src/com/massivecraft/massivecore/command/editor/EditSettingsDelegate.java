package com.massivecraft.massivecore.command.editor;

import org.bukkit.command.CommandSender;

public class EditSettingsDelegate<O, V> extends EditSettings<V>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public EditSettingsDelegate(final EditSettings<O> outerSettings, final Property<O, V> property)
	{
		super(property.getValueType());
		
		PropertyUsed<V> usedProperty = new PropertyUsed<V>(this) {
			
			@Override
			public V getRaw(CommandSender sender)
			{
				return property.getRaw(outerSettings.getUsed(sender));
			}
			
			@Override
			public CommandSender setRaw(CommandSender sender, V used)
			{
				property.setRaw(outerSettings.getUsed(sender), used);
				return sender;
			}
			
		};
		this.setUsedProperty(usedProperty);
		
		this.addUsedRequirements(outerSettings.getPropertyRequirements());
		this.addUsedRequirements(property.getRequirements());
	}
	
}
