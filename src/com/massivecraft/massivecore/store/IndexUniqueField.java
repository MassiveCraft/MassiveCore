package com.massivecraft.massivecore.store;

import java.util.HashMap;
import java.util.Map;

public class IndexUniqueField<F, O>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Map<F, O> f2o;
	private Map<O, F> o2f;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public IndexUniqueField(Map<F, O> map)
	{
		this.f2o = map;
		this.o2f = new HashMap<>();
	}
	
	// -------------------------------------------- //
	// STUFF
	// -------------------------------------------- //
	
	public void update(O object, F field)
	{
		if (object == null) return;
		if (field == null) return;
		
		this.f2o.put(field, object);
		this.o2f.put(object, field);
	}
	
	public O removeField(F field)
	{
		if (field == null) return null;
		
		O object = this.f2o.remove(field);
		if (object != null) this.o2f.remove(object);
		return object;
	}
	
	public F removeObject(O object)
	{
		if (object == null) return null;
		
		F field = this.o2f.remove(object);
		if (field != null) this.f2o.remove(object);
		return field;
	}
	
	public O getObject(F field)
	{
		if (field == null) return null;
		
		return this.f2o.get(field);
	}
	
	public F getField(O object)
	{
		if (object == null) return null;
		
		return this.o2f.get(object);
	}
	
}
