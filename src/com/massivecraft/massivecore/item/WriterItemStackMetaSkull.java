package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.Couple;
import com.massivecraft.massivecore.nms.NmsSkullMeta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class WriterItemStackMetaSkull extends WriterAbstractItemStackMetaField<SkullMeta, String, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaSkull i = new WriterItemStackMetaSkull();
	public static WriterItemStackMetaSkull get() { return i; }
	public WriterItemStackMetaSkull()
	{
		super(SkullMeta.class);
		this.setMaterial(Material.SKULL_ITEM);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca, ItemStack d)
	{
		return ca.getSkull();
	}

	@Override
	public void setA(DataItemStack ca, String fa, ItemStack d)
	{
		if (fa != null) fa = fa.toLowerCase();
		ca.setSkull(fa);
	}

	@Override
	public String getB(SkullMeta cb, ItemStack d)
	{
		return cb.getOwner();
	}

	@Override
	public void setB(SkullMeta cb, String fb, ItemStack d)
	{
		String name = fb;
		UUID id = null;
		
		NmsSkullMeta nms = NmsSkullMeta.get();
		
		Couple<String, UUID> resolved = nms.resolve(name, id);
		name = resolved.getFirst();
		id = resolved.getSecond();
		
		if (name != null || id != null)
		{
			nms.set(cb, name, id);
		}
	}
	
}
