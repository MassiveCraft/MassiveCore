package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.MUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	public void alter(V container) throws MassiveException
	{
		if (this.isCollection())
		{
			this.alterCollection((Collection<?>) container);
		}
		else
		{
			this.alterMap((Map<?, ?>)container);
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE > COLLECTION
	// -------------------------------------------- //
	
	public void alterCollection(Collection<?> elements) throws MassiveException
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
	public void alterMap(Map<?, ?> elements) throws MassiveException
	{
		// Args
		Object element = this.readElement();
		Entry<?, ?> entry = (Entry<?, ?>) element;
		Object key = entry.getKey();
		Object value = entry.getValue();
		
		// Validate
		if (key == null && value == null) throw new MassiveException().addMsg("<b>Please supply key and/or value.");
		
		// Alter
		for (Iterator<?> it = elements.entrySet().iterator(); it.hasNext();)
		{
			Entry<Object, Object> other = (Entry<Object, Object>) it.next();
			
			if (key != null && ! MUtil.equals(key, other.getKey())) continue;
			if (value != null && ! MUtil.equals(value, other.getValue())) continue;
			
			it.remove();
		}
	}
	
}
