	package com.massivecraft.massivecore.command.editor;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.util.PermUtil;

public class EditSettingsSingleton<O> extends EditSettings<O>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String permission;
	public String getPermission() { return this.permission; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <O> EditSettingsSingleton<O> get(O object, Type<O> typeObject, String permission)
	{
		return new EditSettingsSingleton<O>(object, typeObject, permission);
	}
	public EditSettingsSingleton(final O object, Type<O> typeObject, String permission)
	{
		super(typeObject);
		this.permission = permission;
		PropertyUsed<O> usedProperty = new PropertyUsed<O>(this) {
			
			@Override
			public O getRaw(CommandSender sender)
			{
				return object;
			}
			
			@Override
			public void setRaw(CommandSender sender, O used)
			{

			}
			
		};
		
		this.setUsedProperty(usedProperty);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Permission getPropertyPermission(Property<O, ?> property)
	{
		return PermUtil.get(false, permission);
	}
	
}
