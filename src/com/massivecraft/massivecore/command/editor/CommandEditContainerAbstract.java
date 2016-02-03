package com.massivecraft.massivecore.command.editor;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementEditorPropertyCreated;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.util.ContainerUtil;

public abstract class CommandEditContainerAbstract<O, V> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditContainerAbstract(EditSettings<O> settings, Property<O, V> property)
	{
		// Super
		super(settings, property, true);
		
		// Aliases
		String alias = this.createCommandAlias();
		this.setAliases(alias);
		
		// Desc
		this.setDesc(alias + " " + this.getPropertyName());
		
		// Requirements
		this.addRequirements(RequirementEditorPropertyCreated.get(true));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Type
		
		// Create
		V container = this.getProperty().getRaw(this.getObject());
		List<Object> elements = this.getValueType().getContainerElementsOrdered(container);
		
		// Alter
		try
		{
			this.alter(elements);
		}
		catch (MassiveException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new MassiveException().addMsg("<b>%s", e.getMessage());
		}
		
		// After
		V after = this.getValueType().createNewInstance();
		ContainerUtil.addElements(after, elements);
		
		// Order
		elements = this.getValueType().getContainerElementsOrdered(after);
		ContainerUtil.setElements(after, elements);
		
		// Apply
		this.attemptSet(after);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract void alter(List<Object> elements) throws MassiveException;
	
	// -------------------------------------------- //
	// MAP
	// -------------------------------------------- //
	
	public boolean isCollection()
	{
		Type<V> type = this.getValueType();
		if (type.isContainerCollection())
		{
			return true;
		}
		else if (type.isContainerMap())
		{
			return false;
		}
		else
		{
			throw new RuntimeException("Neither Collection nor Map.");
		}
	}
	
	public void addParametersElement(boolean strict)
	{
		Type<V> type = this.getValueType();
		Type<Object> innerType = type.getInnerType();
		
		if (type.isContainerCollection())
		{
			this.addParameter(innerType, innerType.getTypeName(), true);
		}
		else if (type.isContainerMap())
		{
			Type<Object> keyType = innerType.getInnerType(0);
			Type<Object> valueType = innerType.getInnerType(1);
			if (strict)
			{
				this.addParameter(keyType, keyType.getTypeName());
				this.addParameter(valueType, valueType.getTypeName());
			}
			else
			{
				this.addParameter(null, TypeNullable.get(keyType, "any", "all"), keyType.getTypeName(), "any");
				this.addParameter(null, TypeNullable.get(valueType, "any", "all"), valueType.getTypeName(), "any");
			}
		}
		else
		{
			throw new RuntimeException("Neither Collection nor Map.");
		}
	}
	
	public Object readElement() throws MassiveException
	{
		if (this.isCollection())
		{
			return this.readArg();
		}
		else
		{
			Object key = this.readArg();
			Object value = this.readArg();
			return new SimpleEntry<Object, Object>(key, value);
		}
	}

}
