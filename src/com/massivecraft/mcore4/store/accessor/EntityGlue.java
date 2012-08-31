package com.massivecraft.mcore4.store.accessor;

import java.util.Collection;

public interface EntityGlue
{
	public void copy(Object from, Object to, String property, boolean transparent);
	public void copy(Object from, Object to, String property);
	
	public void copy(Object from, Object to, Collection<String> properties, boolean transparent);
	public void copy(Object from, Object to, Collection<String> properties);
	
	public void copy(Object from, Object to, boolean transparent);
	public void copy(Object from, Object to);
	
	public Collection<String> getPropertyNames();
}