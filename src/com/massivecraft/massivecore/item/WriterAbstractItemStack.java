package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.nms.NmsItemStack;

public abstract class WriterAbstractItemStack<OB, CB, FA, FB> extends WriterAbstract<DataItemStack, OB, DataItemStack, CB, FA, FB, ItemStack>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Material material = Material.STONE;
	public Material getMaterial() { return this.material; }
	public void setMaterial(Material material) { this.material = material; }
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public DataItemStack createOA()
	{
		return new DataItemStack();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public OB createOB()
	{
		return (OB) this.createItemStack();
	}
	
	// -------------------------------------------- //
	// CREATE INNER
	// -------------------------------------------- //
	
	public ItemStack createItemStack()
	{
		ItemStack ret = NmsItemStack.get().createItemStack();
		ret.setType(this.getMaterial());
		return ret;
	}
	
	// -------------------------------------------- //
	// CREATE & WRITE
	// -------------------------------------------- //
	// We some times need to access the ItemStack even at deeper levels of writing.
	// With that in mind we pass it along in the data generic.
	
	@Override
	public void writeInner(DataItemStack oa, OB ob, DataItemStack ca, CB cb, ItemStack d, boolean a2b)
	{
		// Ensure there is an ItemStack data. Create if necessary (used by setup method).
		if (d == null) d = (ItemStack)((ob instanceof ItemStack) ? ob : this.createItemStack());
		super.writeInner(oa, ob, ca, cb, d, a2b);
	}
	
}
