package com.massivecraft.mcore2.persist;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.massivecraft.mcore2.Predictate;

public interface IClassManager<T>
{
	// What do we handle?
	public Class<T> getManagedClass();
	
	// This simply creates and returns a new instance
	// It does not detach/attach or anything. Just creates a new instance.
	public T createNewInstance();
	
	// Creativeness
	public boolean getIsCreative();
	public void setIsCreative(boolean val);	

	// Create new instance with auto increment id
	public T create();
	// Create new instance with the requested id
	public T create(Object oid);
	
	// Add & Remove
	public String attach(T entity);
	public String attach(T entity, Object oid);
	public void detachEntity(T entity);
	public void detachId(Object oid);
	public boolean containsEntity(T entity);
	public boolean containsId(Object oid);
	
	// Disc io triggers
	public boolean saveEntity(T entity);
	public boolean saveId(Object oid);
	public boolean saveAll();
	public boolean loaded(Object oid);
	public T load(Object oid);
	public boolean loadAll();
	
	// Should that instance be saved or not?
	// If it is default it should not be saved.
	public boolean shouldBeSaved(T entity);
	
	// Id handling
	// Get the id for this entity. Return null if it does not have an ID
	public String id(T entity);
	
	// In some cases certain non string objects can represent a String id.
	// This method is used as a wrapper for that.
	// TODO: Javadoc: Should start with a null check as well as a String check.
	public String idFix(Object oid);
	public boolean idCanFix(Class<?> clazz);
	
	// autoIncrement ids
	public String idCurrent();
	public String idNext(boolean advance);
	public boolean idUpdateCurrentFor(Object oid);
	
	// Get the entity for the id. If creative it is created if it does not exist else null.
	public T get(Object oid, boolean creative);
	public T get(Object oid);
	
	// Get the entity that best matches the id.
	public T getBestMatch(Object oid);
	
	// Get all
	public Collection<T> getAllLoaded();
	public Collection<T> getAllLoaded(Predictate<T> where);
	public Collection<T> getAllLoaded(Predictate<T> where, Comparator<T> orderby);
	public Collection<T> getAllLoaded(Predictate<T> where, Comparator<T> orderby, Integer limit);
	public Collection<T> getAllLoaded(Predictate<T> where, Comparator<T> orderby, Integer limit, Integer offset);
	public Collection<T> getAll();
	public Collection<T> getAll(Predictate<T> where);
	public Collection<T> getAll(Predictate<T> where, Comparator<T> orderby);
	public Collection<T> getAll(Predictate<T> where, Comparator<T> orderby, Integer limit);
	public Collection<T> getAll(Predictate<T> where, Comparator<T> orderby, Integer limit, Integer offset);
	public Collection<String> getIds();
	public Map<String, T> getMap();
}
