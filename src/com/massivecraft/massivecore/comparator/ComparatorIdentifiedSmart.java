package com.massivecraft.massivecore.comparator;

public class ComparatorIdentifiedSmart extends ComparatorIdentified
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorIdentifiedSmart i = new ComparatorIdentifiedSmart();
	public static ComparatorIdentifiedSmart get() { return i; }
	public ComparatorIdentifiedSmart()
	{
		this.setSmart(true);
	}

}
