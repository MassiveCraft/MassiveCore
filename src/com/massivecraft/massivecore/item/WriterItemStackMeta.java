package com.massivecraft.massivecore.item;

public class WriterItemStackMeta extends WriterAbstractItemStackMetaMorph<Object, Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMeta i = new WriterItemStackMeta();
	public static WriterItemStackMeta get() { return i; }
	public WriterItemStackMeta()
	{
		this.addWriterClasses(
			// UNSPECIFIC
			WriterItemStackMetaName.class,
			WriterItemStackMetaLore.class,
			WriterItemStackMetaEnchants.class,
			WriterItemStackMetaRepaircost.class,
			
			// BOOK
			WriterItemStackMetaTitle.class,
			WriterItemStackMetaAuthor.class,
			WriterItemStackMetaPages.class,
			
			// LEATHER ARMOR
			WriterItemStackMetaColor.class,
			
			// MAP
			WriterItemStackMetaScaling.class,
			
			// POTION EFFECTS
			WriterItemStackMetaPotionEffects.class,
			
			// SKULL
			WriterItemStackMetaSkull17.class,
			WriterItemStackMetaSkull18.class,
			
			// FIREWORK EFFECT
			WriterItemStackMetaFireworkEffect.class,
			
			// FIREWORK
			WriterItemStackMetaFireworkEffects.class,
			WriterItemStackMetaFireworkFlight.class,
			
			// STORED ENCHANTS
			WriterItemStackMetaStoredEnchants.class,
			
			// UNBREAKABLE
			WriterItemStackMetaUnbreakable.class,
			
			// FLAGS
			WriterItemStackMetaFlags.class,
			
			// BANNER
			WriterItemStackMetaBannerBase.class,
			WriterItemStackMetaBannerPatterns.class,
			
			// STATE
			WriterItemStackMetaState.class,
			
			// POTION
			WriterItemStackMetaPotion.class
		);
	}
	
}
