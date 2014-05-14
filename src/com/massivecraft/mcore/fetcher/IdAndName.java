package com.massivecraft.mcore.fetcher;

import java.util.UUID;

public class IdAndName
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private UUID id;
	public UUID getId() { return this.id; }
	
	private String name;
	public String getName() { return this.name; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public IdAndName(UUID id, String name)
	{
		if (id == null && name == null)
		{
			throw new NullPointerException("one of id and name can be null but not both!");
		}
		
		this.id = id;
		this.name = name;
	}

	// -------------------------------------------- //
	// HASH CODE & EQUALS
	// -------------------------------------------- //
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof IdAndName)) return false;
		IdAndName other = (IdAndName) obj;
		if (id == null)
		{
			if (other.id != null) return false;
		}
		else if (!id.equals(other.id)) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		return true;
	}	
	
}
