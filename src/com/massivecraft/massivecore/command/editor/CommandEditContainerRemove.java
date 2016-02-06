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
		for (Iterator<?> it = elements.iterator(); it.hasNext();)
		{
			Object other = it.next();
			if ( ! this.getValueInnerType().equals(other, element)) continue;
			it.remove();
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
		for (Iterator<?> it = elements.iterator(); it.hasNext();)
		{
			Entry<Object, Object> other = (Entry<Object, Object>) it.next();
			
			if (key != null && ! MUtil.equals(key, other.getKey())) continue;
			if (value != null && ! MUtil.equals(value, other.getValue())) continue;
			
			it.remove();
		}
	}
	
}
