package com.massivecraft.massivecore.store;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.JsonObject;


public abstract class CollAbstract<E extends Entity<E>> implements CollInterface<E>
{
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return this.getId();
	}
	
	
	// -------------------------------------------- //
	// SUPPORTING SYSTEM
	// -------------------------------------------- //
	// This is all placed in the implementation.
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	@Override
	public String fixIdOrThrow(Object oid) throws IllegalArgumentException
	{
		String ret = this.fixId(oid);
		if (ret == null) throw new IllegalArgumentException(String.valueOf(oid) + " is not a valid id.");
		return ret;
	}
	
	@Override
	public E get(Object oid)
	{
		return this.getFixed(this.fixId(oid));
	}
	
	@Override
	public E get(Object oid, boolean creative)
	{
		return this.getFixed(this.fixId(oid), creative);
	}
	
	@Override 
	public E getFixed(String id) 
	{
		return this.getFixed(id, this.isCreative());
	}
	
	@Override
	public boolean containsId(Object oid)
	{
		return this.containsIdFixed(this.fixId(oid));
	}
	
	@Override public List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset) { return MUtil.transform(this.getAll(oids), where, orderby, limit, offset); }
	@Override public List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby, Integer limit) { return MUtil.transform(this.getAll(oids), where, orderby, limit); }
	@Override public List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby) { return MUtil.transform(this.getAll(oids), where, orderby); }
	@Override public List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Integer limit, Integer offset) { return MUtil.transform(this.getAll(oids), where, limit, offset); }
	@Override public List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Integer limit) { return MUtil.transform(this.getAll(oids), where, limit); }
	@Override public List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby, Integer limit, Integer offset) { return MUtil.transform(this.getAll(oids), limit, offset); }
	@Override public List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby, Integer limit) { return MUtil.transform(this.getAll(oids), limit); }
	@Override public List<E> getAll(Iterable<?> oids, Predicate<? super E> where) { return MUtil.transform(this.getAll(oids), where); }
	@Override public List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby) { return MUtil.transform(this.getAll(oids), orderby); }
	@Override public List<E> getAll(Iterable<?> oids, Integer limit, Integer offset) { return MUtil.transform(this.getAll(oids), limit, offset); }
	@Override public List<E> getAll(Iterable<?> oids, Integer limit) { return MUtil.transform(this.getAll(oids), limit); }
	
	@Override public List<E> getAll(Iterable<?> oids)
	{
		// Return Create
		List<E> ret = new ArrayList<E>();
		
		// Return Fill
		for (Object oid : oids)
		{
			E e = this.get(oid);
			if (e == null) continue;
			ret.add(e);
		}
		
		// Return Return
		return ret;
	}
	
	@Override public List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset) { return MUtil.transform(this.getAll(), where, orderby, limit, offset); }
	@Override public List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby, Integer limit) { return MUtil.transform(this.getAll(), where, orderby, limit); }
	@Override public List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby) { return MUtil.transform(this.getAll(), where, orderby); }
	@Override public List<E> getAll(Predicate<? super E> where, Integer limit, Integer offset) { return MUtil.transform(this.getAll(), where, limit, offset); }
	@Override public List<E> getAll(Predicate<? super E> where, Integer limit) { return MUtil.transform(this.getAll(), where, limit); }
	@Override public List<E> getAll(Comparator<? super E> orderby, Integer limit, Integer offset) { return MUtil.transform(this.getAll(), limit, offset); }
	@Override public List<E> getAll(Comparator<? super E> orderby, Integer limit) { return MUtil.transform(this.getAll(), limit); }
	@Override public List<E> getAll(Predicate<? super E> where) { return MUtil.transform(this.getAll(), where); }
	@Override public List<E> getAll(Comparator<? super E> orderby) { return MUtil.transform(this.getAll(), orderby); }
	@Override public List<E> getAll(Integer limit, Integer offset) { return MUtil.transform(this.getAll(), limit, offset); }
	@Override public List<E> getAll(Integer limit) { return MUtil.transform(this.getAll(), limit); }
	
	// -------------------------------------------- //
	// BEHAVIOR
	// -------------------------------------------- //
	// This is all placed in the implementation.
	
	// -------------------------------------------- //
	// COPY AND CREATE
	// -------------------------------------------- //
	
	// Create new instance with automatic id
	@Override
	public E create()
	{
		return this.create(null);
	}

	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	@Override
	public String attach(E entity)
	{
		return this.attach(entity, null);
	}

	@Override
	public E detachId(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.detachIdFixed(this.fixIdOrThrow(oid));
	}
	
	// -------------------------------------------- //
	// IDENTIFIED MODIFICATIONS
	// -------------------------------------------- //

	@Override
	public void putIdentifiedModification(Object oid, Modification modification)
	{
		if (oid == null) throw new NullPointerException("oid");
		this.putIdentifiedModificationFixed(this.fixIdOrThrow(oid), modification);
	}
	
	@Override
	public void removeIdentifiedModification(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		this.removeIdentifiedModificationFixed(this.fixIdOrThrow(oid));
	}
	
	// -------------------------------------------- //
	// SYNC LOG
	// -------------------------------------------- //
	// This is all in the implementation.
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	@Override
	public E removeAtLocal(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.removeAtLocalFixed(this.fixIdOrThrow(oid));
	}
	
	@Override
	public void removeAtRemote(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		this.removeAtRemoteFixed(this.fixIdOrThrow(oid));
	}
	
	@Override
	public void saveToRemote(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		this.saveToRemoteFixed(this.fixIdOrThrow(oid));
	}
	
	@Override
	public void loadFromRemote(Object oid, Entry<JsonObject, Long> remoteEntry)
	{
		if (oid == null) throw new NullPointerException("oid");
		this.loadFromRemoteFixed(this.fixIdOrThrow(oid), remoteEntry);
	}
	
	// -------------------------------------------- //
	// SYNC EXAMINE AND DO
	// -------------------------------------------- //
	
	// Examine
	@Override
	public Modification examineId(Object oid, Long remoteMtime, boolean local, boolean remote)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.examineIdFixed(this.fixIdOrThrow(oid), remoteMtime, local, remote);
	}
	
	// Sync
	@Override
	public Modification syncId(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.syncIdFixed(this.fixIdOrThrow(oid));
	}
	
	@Override
	public Modification syncId(Object oid, Modification modification)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.syncIdFixed(this.fixIdOrThrow(oid), modification);
	}
	
	@Override
	public Modification syncId(Object oid, Modification modification, Entry<JsonObject, Long> remoteEntry)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.syncIdFixed(this.fixIdOrThrow(oid), modification, remoteEntry);
	}

	// Sync fixed
	@Override
	public Modification syncIdFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		return this.syncIdFixed(id, null);
	}
	
	@Override
	public Modification syncIdFixed(String id, Modification modification)
	{
		if (id == null) throw new NullPointerException("id");
		return this.syncIdFixed(id, modification, null);
	}
	
	// -------------------------------------------- //
	// SYNC RUNNABLES / SCHEDULING
	// -------------------------------------------- //
	// This is all in the implementation
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	// This is all in the implementation;

}
