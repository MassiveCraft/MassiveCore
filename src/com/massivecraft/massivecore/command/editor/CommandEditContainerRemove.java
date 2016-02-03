package com.massivecraft.massivecore.command.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.MUtil;

public class CommandEditContainerRemove<O, V> extends CommandEditContainerAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditContainerRemove(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
		
		// Aliases
		this.setAliases("remove");
		
		// Parameters
		this.addParametersElement(false);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(List<Object> elements) throws MassiveException
	{
		if (this.isCollection())
		{
			this.alterCollection(elements);
		}
		else
		{
			this.alterMap(elements);
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE > COLLECTION
	// -------------------------------------------- //
	
	public void alterCollection(List<Object> elements) throws MassiveException
	{
		// Args
		Object element = this.readElement();
		
		// Alter
		Iterator<Object> iterator = elements.iterator();
		while (iterator.hasNext())
		{
			Object other = iterator.hasNext();
			if ( ! this.getValueInnerType().equals(other, element, false)) continue;
			iterator.remove();
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE > MAP
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public void alterMap(List<Object> elements) throws MassiveException
	{
		// Args
		Object key = this.readArg();
		Object value = this.readArg();
		
		// Validate
		if (key == null && value == null) throw new MassiveException().addMsg("<b>Please supply key and/or value.");
		
		// Alter
		Iterator<Object> iterator = elements.iterator();
		while (iterator.hasNext())
		{
			Entry<Object, Object> other = (Entry<Object, Object>) iterator.next();
			
			if (key != null && ! MUtil.equals(key, other.getKey())) continue;
			if (value != null && ! MUtil.equals(value, other.getValue())) continue;
			
			iterator.remove();
		}
	}
	
}
