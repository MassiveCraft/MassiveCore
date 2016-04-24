package com.massivecraft.massivecore.item;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import com.massivecraft.massivecore.Couple;
import com.massivecraft.massivecore.nms.NmsHead;

public class WriterItemStackMetaSkull18 extends WriterAbstractItemStackMetaField<SkullMeta, String, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaSkull18 i = new WriterItemStackMetaSkull18();
	public static WriterItemStackMetaSkull18 get() { return i; }
	public WriterItemStackMetaSkull18()
	{
		this.setMaterial(Material.SKULL_ITEM);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca)
	{
		return ca.getSkull();
	}

	@Override
	public void setA(DataItemStack ca, String fa)
	{
		if (fa != null) fa = fa.toLowerCase();
		ca.setSkull(fa);
	}

	@Override
	public String getB(SkullMeta cb)
	{
		return cb.getOwner();
	}

	@Override
	public void setB(SkullMeta cb, String fb)
	{
		String name = fb;
		
		UUID id = null;
		
		Couple<String, UUID> resolved = NmsHead.resolve(name, id);
		name = resolved.getFirst();
		id = resolved.getSecond();
		
		if (name != null || id != null)
		{
			NmsHead.set(cb, name, id);
		}
	}
	
}
