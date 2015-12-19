package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementEditorPropertyCreated;
import com.massivecraft.massivecore.command.type.Type;

import java.util.Map;

public abstract class CommandEditMapAbstract<O, V extends Map<?, ?>> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CommandEditMapAbstract(EditSettings<O> settings, Property<O, V> property, Type<?> mapValueType)
	{
		// Super
		super(settings, property, true);
		this.setMapValueType(mapValueType);

		// Aliases
		String alias = this.createCommandAlias();
		this.setAliases(alias);
		
		// Desc
		this.setDesc(alias + " " + this.getPropertyName());
		
		// Requirements
		this.addRequirements(RequirementEditorPropertyCreated.get(true));
	}

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	Type<?> mapValueType = null;
	public void setMapValueType(Type<?> mapValueType) { this.mapValueType = mapValueType; }

	// -------------------------------------------- //
	// SHORTCUTS > PROPERTY > TYPE
	// -------------------------------------------- //

	// Only to be used with map type properties.
	public Type<Object> getMapKeyType()
	{
		return this.getProperty().getValueType().getInnerType();
	}

	@SuppressWarnings("unchecked")
	public Type<Object> getMapValueType()
	{
		return (Type<Object>) mapValueType;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public void perform() throws MassiveException
	{
		// Create
		Map<Object, Object> after = this.getShallowCopy();
		
		// Alter
		try
		{
			after = this.alter(after);
		}
		catch (MassiveException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new MassiveException().addMsg("<b>%s", e.getMessage());
		}

		// Apply
		this.attemptSet((V) after);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Map<Object, Object> alter(Map<Object, Object> map) throws MassiveException;

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	@SuppressWarnings("unchecked")
	public Map<Object, Object> getShallowCopy()
	{
		// Create
		V ret = this.getProperty().getRaw(this.getObject());
		if (ret == null) return null;

		// Fill
		Map<Object, Object> copy = (Map<Object, Object>) this.getProperty().getValueType().createNewInstance();
		for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) ret).entrySet())
		{
			copy.put(entry.getKey(), entry.getValue());
		}

		// Return
		return copy;
	}
	
}
