package com.massivecraft.massivecore.item;

public class WriterItemStackMetaState extends WriterAbstractItemStackMetaStateMorph<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaState i = new WriterItemStackMetaState();
	public static WriterItemStackMetaState get() { return i; }
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		this.clearWriters();
		
		this.addWriters(
			// SHIELD
			WriterItemStackMetaStateShieldBase.class,
			WriterItemStackMetaStateShieldPatterns.class
		);
	}
	
}
