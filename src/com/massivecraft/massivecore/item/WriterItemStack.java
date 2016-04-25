package com.massivecraft.massivecore.item;

public class WriterItemStack extends WriterAbstractItemStackField<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStack i = new WriterItemStack();
	public static WriterItemStack get() { return i; }
	public WriterItemStack()
	{
		this.addWriterClasses(
			// BASIC
			WriterItemStackId.class,
			WriterItemStackCount.class,
			WriterItemStackDamage.class,
			
			// META
			WriterItemStackMeta.class
		);
	}
	
}
