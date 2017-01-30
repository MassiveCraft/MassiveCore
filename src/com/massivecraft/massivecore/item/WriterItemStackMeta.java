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
			WriterItemStackMetaMapColor.class,
			
			// POTION EFFECTS
			WriterItemStackMetaPotionEffects.class,
			
			// SKULL
			WriterItemStackMetaSkull.class,
			
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
			
			// SHIELD
			WriterItemStackMetaShieldBase.class,
			WriterItemStackMetaShieldPatterns.class,
			
			// POTION
			WriterItemStackMetaPotion.class,
			WriterItemStackMetaPotionColor.class,
				
			// INVENTORY
			WriterItemStackMetaInventory.class
		);
	}
	
}
