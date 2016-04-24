package com.massivecraft.massivecore.item;

public class WriterBannerPattern extends WriterAbstractBannerPattern<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterBannerPattern i = new WriterBannerPattern();
	public static WriterBannerPattern get() { return i; }
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		this.clearWriters();
		
		this.addWriters(
			WriterBannerPatternId.class,
			WriterBannerPatternColor.class
		);

	}
	
}
