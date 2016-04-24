package com.massivecraft.massivecore.item;

public class WriterFireworkEffect extends WriterAbstractFireworkEffect<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterFireworkEffect i = new WriterFireworkEffect();
	public static WriterFireworkEffect get() { return i; }
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		this.clearWriters();
		
		this.addWriters(
			WriterFireworkEffectFlicker.class,
			WriterFireworkEffectTrail.class,
			WriterFireworkEffectColors.class,
			WriterFireworkEffectFadeColors.class,
			WriterFireworkEffectType.class
		);

	}
	
}
