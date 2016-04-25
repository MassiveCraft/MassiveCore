package com.massivecraft.massivecore.item;

public class WriterBannerPattern extends WriterAbstractBannerPattern<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterBannerPattern i = new WriterBannerPattern();
	public static WriterBannerPattern get() { return i; }
	public WriterBannerPattern()
	{
		this.addWriterClasses(
			WriterBannerPatternId.class,
			WriterBannerPatternColor.class
		);
	}
	
}
