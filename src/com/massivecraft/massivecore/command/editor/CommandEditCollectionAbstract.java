package com.massivecraft.massivecore.command.editor;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.requirement.RequirementEditorPropertyCreated;

public abstract class CommandEditCollectionAbstract<O, V extends Collection<?>> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditCollectionAbstract(EditSettings<O> settings, Property<O, V> property)
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void perform() throws MassiveException
	{
		// Create
		List<Object> after = this.getShallowCopy();
		
		// Alter
		try
		{
			this.alter(after);
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
		Collection<Object> toSet = null;
		if (after != null)
		{
			toSet = (Collection<Object>) this.getProperty().getValueType().createNewInstance();
			toSet.addAll(after);
		}
		
		this.attemptSet((V) toSet);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract void alter(List<Object> list) throws MassiveException;
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public List<Object> getShallowCopy()
	{
		V ret = this.getProperty().getRaw(this.getObject());
		if (ret == null) return null;
		return new MassiveList<Object>(ret);
	}
	
}
