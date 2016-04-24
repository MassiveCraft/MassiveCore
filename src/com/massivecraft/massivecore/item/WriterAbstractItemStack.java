package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.nms.NmsItemStack;

public abstract class WriterAbstractItemStack<OB, CB, FA, FB> extends WriterAbstract<DataItemStack, OB, DataItemStack, CB, FA, FB>
{
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
		return (CB) NmsItemStack.get().createItemStack();
	}
	
}
