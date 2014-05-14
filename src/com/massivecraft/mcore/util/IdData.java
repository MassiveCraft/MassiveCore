package com.massivecraft.mcore.util;

public class IdData
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String id;
	public String getId() { return this.id; }
	
	private String name;
	public String getName() { return this.name; }
	
	private long millis;
	public long getMillis() { return this.millis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	// It's always good to have a no-arg constructor
	public IdData()
	{
		
	}
	
	public IdData(String id, String name)
	{
		this(id, name, System.currentTimeMillis());
	}
	
	public IdData(String id, String name, long millis)
	{
		if (id == null && name == null) throw new NullPointerException("Either id or name must be set. They can't both be null!");
		
		this.id = id;
		this.name = name;
		this.millis = millis;
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
		result = prime * result + (int) (millis ^ (millis >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof IdData)) return false;
		IdData other = (IdData) obj;
		if (id == null)
		{
			if (other.id != null) return false;
		}
		else if (!id.equals(other.id)) return false;
		if (millis != other.millis) return false;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		return true;
	}
	
}
