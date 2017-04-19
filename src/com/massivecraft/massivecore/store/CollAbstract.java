package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.xlib.gson.JsonObject;

import java.util.Map.Entry;


public abstract class CollAbstract<E extends Entity<E>> extends EntityContainerAbstract<E> implements CollInterface<E>
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
	
}
