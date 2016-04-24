package com.massivecraft.massivecore.item;

public class WriterPotionEffect extends WriterAbstractPotionEffect<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterPotionEffect i = new WriterPotionEffect();
	public static WriterPotionEffect get() { return i; }
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		this.clearWriters();
		
		this.addWriters(
			WriterPotionEffectId.class,
			WriterPotionEffectDuraction.class,
			WriterPotionEffectAmplifier.class,
			WriterPotionEffectAmbient.class,
			WriterPotionEffectParticles.class,
			WriterPotionEffectColor.class
		);

	}
	
}
