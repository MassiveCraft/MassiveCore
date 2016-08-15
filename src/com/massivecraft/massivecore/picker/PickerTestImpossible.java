package com.massivecraft.massivecore.picker;

public class PickerTestImpossible extends PickerTest
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final PickerTestImpossible i = new PickerTestImpossible();
	public static PickerTestImpossible get() { return i; }
	
	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	
	@Override
	public Object provoke() throws Throwable
	{
		throw new RuntimeException("so provokative!");
	}
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	@Override
	public String getMsg()
	{
		return "<rose>Impossible and Unexpected";
	}
	
}
