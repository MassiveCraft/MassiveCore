package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class WriterAbstractItemStackMeta<OB, CB, FA, FB> extends WriterAbstractItemStack<OB, CB, FA, FB>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Material material = Material.STONE;
	public Material getMaterial() { return this.material; }
	@SuppressWarnings("unchecked")
	public void setMaterial(Material material)
	{
		this.material = material;
		CB cb = this.createB();
		this.setClassB((Class<CB>) cb.getClass());
	}
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public CB createB()
	{
		ItemStack itemStack = (ItemStack) super.createB();
		itemStack.setType(this.getMaterial());
		return (CB) itemStack.getItemMeta();
	}
	
}
