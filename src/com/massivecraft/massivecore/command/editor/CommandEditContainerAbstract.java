package com.massivecraft.massivecore.command.editor;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementEditorPropertyCreated;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.util.ContainerUtil;
import com.massivecraft.massivecore.util.Txt;

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
		elements = this.getValueType().getContainerElementsOrdered(elements);
		V after = this.getValueType().createNewInstance();
		ContainerUtil.addElements(after, elements);
		
		// Apply
		this.attemptSet(after);
	}
	
	@Override
	public String createCommandAlias()
	{
		// Split at uppercase letters
		String name = this.getClass().getSimpleName();
		name = name.substring("CommandEditContainer".length());
		final String[] words = name.split("(?=[A-Z])");
		String alias = Txt.implode(words, "");
		alias = Txt.lowerCaseFirst(alias);
		return alias;
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract void alter(List<Object> elements) throws MassiveException;
	
	// -------------------------------------------- //
	// PARAMETER
	// -------------------------------------------- //
	
	public boolean isCollection()
	{
		Type<V> type = this.getValueType();
		if (type.isContainerCollection()) return true;
		if (type.isContainerMap()) return false;
		throw new RuntimeException("Neither Collection nor Map.");
	}
	
	public void addParametersElement(boolean strict)
	{
		Type<Object> innerType = this.getValueInnerType();
		
		if (this.isCollection())
		{
			this.addParameter(innerType, true);
		}
		else
		{
			Type<Object> keyType = innerType.getInnerType(0);
			Type<Object> valueType = innerType.getInnerType(1);
			if (strict)
			{
				this.addParameter(keyType);
				this.addParameter(valueType);
			}
			else
			{
				this.addParameter(null, TypeNullable.get(keyType, "any", "all"), keyType.getTypeName(), "any");
				this.addParameter(null, TypeNullable.get(valueType, "any", "all"), valueType.getTypeName(), "any");
			}
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
			return new SimpleImmutableEntry<Object, Object>(key, value);
		}
	}

}
