package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateEqualsIgnoreCase;
import com.massivecraft.massivecore.util.MUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

// Calls fixId when necessary
public abstract class EntityContainerAbstract<E extends EntityInternal<E>> implements EntityContainer<E>
{
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	@Override public Map<String, E> getIdToEntity() { return Collections.unmodifiableMap(this.getIdToEntityRaw()); }
	
	@Override
	public String fixId(Object oid)
	{
		if (oid == null) return null;
		
		String ret = null;
		if (oid instanceof String) ret = (String) oid;
		else if (oid.getClass() == this.getEntityClass()) ret = ((Entity<?>) oid).getId();
		if (ret == null) return null;
		
		return this.isLowercasing() ? ret.toLowerCase() : ret;
	}
	
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
	public E getFixed(String id, boolean creative)
	{
		return this.getFixed(id, creative, true);
	}
	protected E getFixed(String id, boolean creative, boolean noteModification)
	{
		if (id == null) return null;
		E ret = this.getIdToEntity().get(id);
		if (ret != null) return ret;
		if ( ! creative) return null;
		return this.create(id, noteModification);
	}
	
	@Override public Collection<String> getIds() { return this.getIdToEntity().keySet(); }
	
	@Override
	public boolean containsId(Object oid)
	{
		return this.containsIdFixed(this.fixId(oid));
	}
	
	@Override
	public boolean containsIdFixed(String id)
	{
		if (id == null) return false;
		return this.getIdToEntity().containsKey(id);
	}
	
	@Override
	public boolean containsEntity(Object entity)
	{
		return this.getIdToEntity().containsValue(entity);
	}
	
	@Override public Collection<E> getAll()
	{
		return this.getIdToEntity().values();
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
		List<E> ret = new ArrayList<>();
		
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
	
	protected boolean creative;
	@Override public boolean isCreative() { return this.creative; }
	@Override public void setCreative(boolean creative) { this.creative = creative; }
	
	// "Lowercasing" means that the ids are always converted to lower case when fixed.
	// This is highly recommended for sender colls.
	// The senderIds are case insensitive by nature and some times you simply can't know the correct casing.
	protected boolean lowercasing;
	@Override public boolean isLowercasing() { return this.lowercasing; }
	@Override public void setLowercasing(boolean lowercasing) { this.lowercasing = lowercasing; }
	
	// Should that instance be saved or not?
	// If it is default it should not be saved.
	@Override
	public boolean isDefault(E entity)
	{
		return entity.isDefault();
	}
	
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
	// COPY AND CREATE
	// -------------------------------------------- //
	
	@Override
	public void copy(E efrom, E eto)
	{
		if (efrom == null) throw new NullPointerException("efrom");
		if (eto == null) throw new NullPointerException("eto");
		
		eto.load(efrom);
	}
	
	// This simply creates and returns a new instance
	// It does not detach/attach or anything. Just creates a new instance.
	@Override
	public E createNewInstance()
	{
		try
		{
			return this.getEntityClass().newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	// Create new instance with the requested id
	@Override
	public synchronized E create(Object oid)
	{
		return this.create(oid, true);
	}
	
	public synchronized E create(Object oid, boolean noteModification)
	{
		E entity = this.createNewInstance();
		if (this.attach(entity, oid, noteModification) == null) return null;
		return entity;
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
	public String attach(E entity, Object oid)
	{
		return this.attach(entity, oid, true);
	}
	
	protected synchronized String attach(E entity, Object oid, boolean noteModification)
	{
		// Check entity
		if (entity == null) return null;
		if (entity.attached()) return entity.getId();
		
		String id;
		// Check/Fix id
		if (oid == null)
		{
			id = MStore.createId();
		}
		else
		{
			id = this.fixId(oid);
			if (id == null) return null;
			if (this.getIdToEntity().containsKey(id)) return null;
		}
		
		// PRE
		this.preAttach(entity, id);
		
		// Add entity reference info
		entity.setContainer(this);
		entity.setId(id);
		
		// Attach
		this.getIdToEntityRaw().put(id, entity);
		
		// Identify Modification
		if (noteModification)
		{
			this.putIdentifiedModificationFixed(id, Modification.LOCAL_ATTACH);
		}
		
		// POST
		this.postAttach(entity, id);
		
		return id;
	}
	
	@Override
	public E detachId(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.detachIdFixed(this.fixIdOrThrow(oid));
	}
	
	@Override
	public E detachEntity(E entity)
	{
		if (entity == null) throw new NullPointerException("entity");
		
		String id = entity.getId();
		if (id == null)
		{
			// It seems the entity is already detached.
			// In such case just silently return it.
			return entity;
		}
		
		this.detachFixed(entity, id);
		return entity;
	}
	
	@Override
	public E detachIdFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		E e = this.get(id, false);
		if (e == null) return null;
		
		this.detachFixed(e, id);
		return e;
	}
	
	protected void detachFixed(E entity, String id)
	{
		if (entity == null) throw new NullPointerException("entity");
		if (id == null) throw new NullPointerException("id");
		
		// PRE
		this.preDetach(entity, id);
		
		// Remove @ local
		this.removeAtLocalFixed(id);
		
		// Identify Modification
		this.putIdentifiedModificationFixed(id, Modification.LOCAL_DETACH);
		
		// POST
		this.postDetach(entity, id);
	}
	
	@Override
	public void preAttach(E entity, String id)
	{
		entity.preAttach(id);
	}
	
	@Override
	public void postAttach(E entity, String id)
	{
		entity.postAttach(id);
	}
	
	@Override
	public void preDetach(E entity, String id)
	{
		entity.preDetach(id);
	}
	
	@Override
	public void postDetach(E entity, String id)
	{
		entity.postDetach(id);
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
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	@Override
	public E removeAtLocal(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		return this.removeAtLocalFixed(this.fixIdOrThrow(oid));
	}
	
	// -------------------------------------------- //
	// NAME UTILITIES
	// -------------------------------------------- //
	
	@Override
	public E getByName(String name)
	{
		if (name == null) throw new NullPointerException("name");
		
		Predicate<String> predicate = PredicateEqualsIgnoreCase.get(name);
		for (E entity : this.getAll())
		{
			if (entity == null) continue;
			
			if ( ! (entity instanceof Named)) continue;
			Named named = (Named)entity;
			
			if (predicate.apply(named.getName())) return entity;
		}
		
		return null;
	}
	
	@Override
	public boolean isNameTaken(String str)
	{
		return this.getByName(str) != null;
	}
	
}
