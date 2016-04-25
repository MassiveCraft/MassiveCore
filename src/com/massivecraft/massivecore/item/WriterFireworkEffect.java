package com.massivecraft.massivecore.item;

public class WriterFireworkEffect extends WriterAbstractFireworkEffect<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterFireworkEffect i = new WriterFireworkEffect();
	public static WriterFireworkEffect get() { return i; }
	public WriterFireworkEffect()
	{
		this.addWriterClasses(
			WriterFireworkEffectFlicker.class,
			WriterFireworkEffectTrail.class,
			WriterFireworkEffectColors.class,
			WriterFireworkEffectFadeColors.class,
			WriterFireworkEffectType.class
		);
	}
	
}
