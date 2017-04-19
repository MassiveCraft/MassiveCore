package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.collections.MassiveSet;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EntityInternalMap<E extends EntityInternal<E>> extends EntityContainerAbstract<E>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private EntityInternalMap()
	{
		this.entityClass = null;
	}
	
	public EntityInternalMap(Class<E> entityClass)
	{
		this.entityClass = entityClass;
	}
	
	public EntityInternalMap(EntityInternal<?> entity, Class<E> entityClass)
	{
		Objects.requireNonNull(entity, "entity");
		Objects.requireNonNull(entityClass, "entityClass");
		
		this.setEntity(entity);
		this.entityClass = entityClass;
	}
	
	// -------------------------------------------- //
	// REFERENCE
	// -------------------------------------------- //
	
	protected transient WeakReference<EntityInternal<?>> entity = new WeakReference<>(null);
	protected void setEntity(EntityInternal<?> entity) { this.entity = new WeakReference<EntityInternal<?>>(entity); }
	public EntityInternal<?> getEntity() { return this.entity.get(); }
	
	@Override
	public boolean isLive()
	{
		EntityInternal<?> entity = this.getEntity();
		if (entity == null) return false;
		
		if (!entity.isLive()) return false;
		
		return true;
	}
	
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	
	protected final transient Class<E> entityClass;
	@Override public Class<E> getEntityClass() { return this.entityClass; }
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	private final ConcurrentHashMap<String, E> id2Entity = new ConcurrentHashMap<>();
	
	@Override public Map<String, E> getIdToEntityRaw() { return this.id2Entity; }

	// -------------------------------------------- //
	// REFERENCE
	// -------------------------------------------- //

	@Override
	public Coll<?> getColl()
	{
		return this.getEntity().getContainer().getColl();
	}
	
	// -------------------------------------------- //
	// LOAD
	// -------------------------------------------- //
	
	public EntityInternalMap<E> load(EntityInternalMap<E> that)
	{
		Objects.requireNonNull(that, "that");
		
		// Loop over all the entities in that
		for (Entry<String, E> entry : that.id2Entity.entrySet())
		{
			String id = entry.getKey();
			E entity = entry.getValue();
			
			E current = this.id2Entity.get(id);
			if (current != null)
			{
				// Load if present
				current.load(entity);
			}
			else
			{
				// attach if not present
				this.attach(entity, id);
			}
		}
		
		// Clean entities of those that are not in that
		if (this.id2Entity.size() != that.id2Entity.size())
		{
			// Avoid CME
			Set<Entry<String, E>> removals = new MassiveSet<>();
			
			// Loop over all current entries ...
			for (Iterator<Entry<String, E>> it = this.id2Entity.entrySet().iterator(); it.hasNext(); )
			{
				Entry<String, E> entry = it.next();
				String id = entry.getKey();
				
				// ... if it is not present in those ...
				if (that.id2Entity.containsKey(id)) continue;
				
				// ... remove.
				removals.add(entry);
			}
			
			// Remove
			for (Entry<String, E> removal : removals)
			{
				this.detachFixed(removal.getValue(), removal.getKey());
			}
		}
		
		return this;
	}

	// -------------------------------------------- //
	// IDENTIFIED MODIFICATIONS
	// -------------------------------------------- //
	
	protected Map<String, Modification> identifiedModifications;
	
	@Override
	public synchronized void putIdentifiedModificationFixed(String id, Modification modification)
	{
		Objects.requireNonNull(id, "id");
		this.changed();
	}
	
	@Override
	public synchronized void removeIdentifiedModificationFixed(String id)
	{
		Objects.requireNonNull(id, "id");
		this.changed();
	}
	
	private void changed()
	{
		if (!this.isLive()) return;
		
		//System.out.println(this.getColl().getName() + ": " +this.getId() + " was modified locally");
		
		this.getEntity().changed();
	}
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	@Override
	public synchronized E removeAtLocalFixed(String id)
	{
		Objects.requireNonNull(id, "id");
		
		this.removeIdentifiedModificationFixed(id);
		
		E entity = this.getIdToEntity().remove(id);
		if (entity == null) return null;
		
		// Remove entity reference info
		entity.setContainer(null);
		entity.setId(null);
		
		return entity;
	}
	
	// -------------------------------------------- //
	// MAP DELEGATION
	// -------------------------------------------- //
	
	public Set<Entry<String, E>> entrySet()
	{
		return this.getIdToEntityRaw().entrySet();
	}
	public Set<String> keySet()
	{
		return this.getIdToEntity().keySet();
	}
	
	public boolean containsKey(String id)
	{
		Objects.requireNonNull(id, "id");
		return this.getIdToEntityRaw().containsKey(id);
	}
	
	public E remove(String id)
	{
		Objects.requireNonNull(id, "id");
		return this.getIdToEntityRaw().remove(id);
	}
	
	public int size()
	{
		return this.getIdToEntityRaw().size();
	}
	
	public boolean isEmpty()
	{
		return this.getIdToEntityRaw().isEmpty();
	}
	
	public void clear()
	{
		this.getIdToEntityRaw().clear();
	}
	
}
