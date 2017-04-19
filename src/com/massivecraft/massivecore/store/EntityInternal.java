package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.accessor.Accessor;
import com.massivecraft.massivecore.xlib.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class EntityInternal<E extends EntityInternal<E>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected transient WeakReference<EntityContainer<E>> container = new WeakReference<>(null);
	protected void setContainer(EntityContainer<E> container)
	{
		this.container = new WeakReference<>(container);
	}
	public EntityContainer<E> getContainer()
	{
		return this.container.get();
	}
	
	public Coll<?> getColl()
	{
		return this.getContainer().getColl();
	}
	
	protected transient String id;
	protected void setId(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return this.id;
	}
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	public boolean attached()
	{
		return this.getContainer() != null && this.getId() != null;
	}
	
	public boolean detached()
	{
		return !this.attached();
	}
	
	public void preAttach(String id)
	{
		
	}
	
	public void postAttach(String id)
	{
		
	}
	
	public void preDetach(String id)
	{
		
	}
	
	public void postDetach(String id)
	{
		
	}
	
	// -------------------------------------------- //
	// SYNC AND IO ACTIONS
	// -------------------------------------------- //
	
	public boolean isLive()
	{
		String id = this.getId();
		if (id == null) return false;
		
		EntityContainer<E> container = this.getContainer();
		if (container == null) return false;
		
		if (!container.isLive()) return false;
		
		return true;
	}
	
	public void changed()
	{
		if (!this.isLive()) return;
		
		//System.out.println(this.getColl().getName() + ": " +this.getId() + " was modified locally");
		
		this.getContainer().putIdentifiedModificationFixed(this.getId(), Modification.UNKNOWN);
	}
	
	// -------------------------------------------- //
	// DERPINGTON
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public E load(E that)
	{
		Objects.requireNonNull(that, "that");
		Accessor.get(this.getClass()).copy(that, this);
		return (E) this;
	}
	
	public boolean isDefault()
	{
		return false;
	}
	
	// -------------------------------------------- //
	// CONVENIENCE: DATABASE
	// -------------------------------------------- //
	
	// GENERIC
	public <T> T convertGet(T value, T standard)
	{
		return value != null ? value : standard;
	}
	
	public <T> T convertSet(T value, T standard)
	{
		this.changed();
		return Objects.equals(value, standard) ? null : value;
	}
	
	// BOOLEAN
	public boolean convertGet(Boolean value)
	{
		return convertGet(value, false);
	}
	
	public Boolean convertSet(Boolean value)
	{
		return convertSet(value, false);
	}
	
	// -------------------------------------------- //
	// STANDARDS
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		Gson gson = MassiveCore.gson;
		Coll<?> coll = this.getColl();
		if (coll != null) gson = coll.getGson();
		
		return this.getClass().getSimpleName() + gson.toJson(this, this.getClass());
	}
	
}
