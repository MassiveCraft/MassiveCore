package com.massivecraft.massivecore.item;

public class WriterItemStackMetaState extends WriterAbstractItemStackMetaStateMorph<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaState i = new WriterItemStackMetaState();
	public static WriterItemStackMetaState get() { return i; }
	public WriterItemStackMetaState()
	{
		this.addWriterClasses(
			// SHIELD
			WriterItemStackMetaStateShieldBase.class,
			WriterItemStackMetaStateShieldPatterns.class
		);
	}
	
}
