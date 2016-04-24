package com.massivecraft.massivecore.item;

public class WriterItemStack extends WriterAbstractItemStack<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStack i = new WriterItemStack();
	public static WriterItemStack get() { return i; }
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		this.clearWriters();
		 
		this.addWriters(
			// BASIC
			WriterItemStackId.class,
			WriterItemStackCount.class,
			WriterItemStackDamage.class,
			
			// META
			WriterItemStackMeta.class
		);
	}

	
}
