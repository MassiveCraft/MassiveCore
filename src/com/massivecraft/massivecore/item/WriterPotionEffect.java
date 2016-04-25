package com.massivecraft.massivecore.item;

public class WriterPotionEffect extends WriterAbstractPotionEffect<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterPotionEffect i = new WriterPotionEffect();
	public static WriterPotionEffect get() { return i; }
	public WriterPotionEffect()
	{
		this.addWriterClasses(
			WriterPotionEffectId.class,
			WriterPotionEffectDuraction.class,
			WriterPotionEffectAmplifier.class,
			WriterPotionEffectAmbient.class,
			WriterPotionEffectParticles.class,
			WriterPotionEffectColor.class
		);
	}
	
}
