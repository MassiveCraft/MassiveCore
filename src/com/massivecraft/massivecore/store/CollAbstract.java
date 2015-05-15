package com.massivecraft.massivecore.store;

import java.util.Map.Entry;

import com.massivecraft.massivecore.xlib.gson.JsonElement;


public abstract class CollAbstract<E> implements CollInterface<E>
{
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	// This is all placed in the implementation.
	
	// -------------------------------------------- //
	// SUPPORTING SYSTEM
	// -------------------------------------------- //
	// This is all placed in the implementation.
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
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
	
	@Override
	public String fixIdOrThrow(Object oid) throws IllegalArgumentException
	{
		String ret = this.fixId(oid);
		if (ret == null) throw new IllegalArgumentException(String.valueOf(oid) + " is not a valid id.");
		return ret;
	}
	
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
	public void loadFromRemote(Object oid, Entry<JsonElement, Long> remoteEntry)
	{
		if (oid == null) throw new NullPointerException("oid");
		this.loadFromRemoteFixed(this.fixIdOrThrow(oid), remoteEntry);
	}
	
	// -------------------------------------------- //
	// SYNC EXAMINE AND DO
	// -------------------------------------------- //
	
	// Examine
	@Override
	public Modification examineId(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.examineIdFixed(this.fixIdOrThrow(oid));
	}
	
	@Override
	public Modification examineId(Object oid, Long remoteMtime)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.examineIdFixed(this.fixIdOrThrow(oid), remoteMtime);
	}
	
	@Override
	public Modification examineIdFixed(String id)
	{
		// Null check done	 later.
		return this.examineIdFixed(id, null);
	}
	
	// Sync
	@Override
	public Modification syncId(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.syncIdFixed(this.fixIdOrThrow(oid));
	}
	
	@Override
	public Modification syncId(Object oid, Modification modificationState)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.syncIdFixed(this.fixIdOrThrow(oid), modificationState);
	}
	
	@Override
	public Modification syncId(Object oid, Modification modificationState, Entry<JsonElement, Long> remoteEntry)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.syncIdFixed(this.fixIdOrThrow(oid), modificationState, remoteEntry);
	}
	
	@Override
	public Modification syncIdFixed(String id)
	{
		// Null check done later.
		return this.syncIdFixed(id, null);
	}
	
	@Override
	public Modification syncIdFixed(String id, Modification modification)
	{
		// Null check done later.
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
