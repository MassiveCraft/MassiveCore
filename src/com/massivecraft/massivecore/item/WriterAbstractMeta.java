package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.nms.NmsItemStack;

public abstract class WriterAbstractMeta<OB, CB, FA, FB> extends WriterAbstract<DataItemStack, OB, DataItemStack, CB, FA, FB>
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
	
	@Override
	public DataItemStack createA()
	{
		return new DataItemStack();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CB createB()
	{
		ItemStack itemStack = NmsItemStack.get().createItemStack();
		itemStack.setType(this.getMaterial());
		return (CB) itemStack.getItemMeta();
	}
	
}
