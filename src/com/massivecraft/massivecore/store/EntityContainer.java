package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.predicate.Predicate;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

// Most base EntityContainer
public interface EntityContainer<E>
{
	String attach(E entity);
	String attach(E entity, Object oid);
	
	E detachEntity(E entity);
	E detachId(Object oid);
	E detachIdFixed(String id);
	
	boolean isLive();
	
	Coll<?> getColl();
	
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	
	Class<E> getEntityClass();
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	String fixId(Object oid);
	String fixIdOrThrow(Object oid) throws IllegalArgumentException;
	
	Map<String, E> getIdToEntityRaw();
	Map<String, E> getIdToEntity();
	E get(Object oid);
	E get(Object oid, boolean creative);
	E getFixed(String id);
	E getFixed(String id, boolean creative);
	Collection<String> getIds();
	boolean containsId(Object oid);
	boolean containsIdFixed(String id);
	
	boolean containsEntity(Object entity);
	
	Collection<E> getAll();
	
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby, Integer limit);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Integer limit, Integer offset);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Integer limit);
	List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby, Integer limit, Integer offset);
	List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby, Integer limit);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where);
	List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby);
	List<E> getAll(Iterable<?> oids, Integer limit, Integer offset);
	List<E> getAll(Iterable<?> oids, Integer limit);
	List<E> getAll(Iterable<?> oids);
	
	List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset);
	List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby, Integer limit);
	List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby);
	List<E> getAll(Predicate<? super E> where, Integer limit, Integer offset);
	List<E> getAll(Predicate<? super E> where, Integer limit);
	List<E> getAll(Comparator<? super E> orderby, Integer limit, Integer offset);
	List<E> getAll(Comparator<? super E> orderby, Integer limit);
	List<E> getAll(Predicate<? super E> where);
	List<E> getAll(Comparator<? super E> orderby);
	List<E> getAll(Integer limit, Integer offset);
	List<E> getAll(Integer limit);
	
	// -------------------------------------------- //
	// BEHAVIOR
	// -------------------------------------------- //
	
	boolean isCreative();
	void setCreative(boolean creative);
	
	boolean isLowercasing();
	void setLowercasing(boolean lowercasing);
	
	// A default entity will not be saved.
	// This is often used together with creative collections to save disc space.
	boolean isDefault(E entity);
	
	// -------------------------------------------- //
	// COPY AND CREATE
	// -------------------------------------------- //
	
	void copy(E fromo, E too);
	
	// This simply creates and returns a new instance
	// It does not detach/attach or anything. Just creates a new instance.
	E createNewInstance();
	
	// Create new instance with automatic id
	E create();
	// Create new instance with the requested id
	E create(Object oid);
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	/*String attach(E entity);
	String attach(E entity, Object oid);
	
	E detachEntity(E entity);
	E detachId(Object oid);
	E detachIdFixed(String id);*/
	
	void preAttach(E entity, String id);
	void postAttach(E entity, String id);
	
	void preDetach(E entity, String id);
	void postDetach(E entity, String id);
	
	// -------------------------------------------- //
	// IDENTIFIED MODIFICATIONS
	// -------------------------------------------- //
	
	void putIdentifiedModification(Object oid, Modification modification);
	void putIdentifiedModificationFixed(String id, Modification modification);
	
	void removeIdentifiedModification(Object oid);
	void removeIdentifiedModificationFixed(String id);
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	// oid
	E removeAtLocal(Object oid);
	
	// fixed
	E removeAtLocalFixed(String id);
	
	// -------------------------------------------- //
	// NAME UTILITIES
	// -------------------------------------------- //
	
	E getByName(String name);
	boolean isNameTaken(String str);
	
}
