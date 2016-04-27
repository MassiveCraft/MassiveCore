package com.massivecraft.massivecore.command.type;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.command.type.primitive.TypeObject;
import com.massivecraft.massivecore.item.DataItemStack;

public class TypeDataItemStack extends TypeTransformer<ItemStack, DataItemStack>
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static final TypeDataItemStack i = new TypeDataItemStack();
	public static TypeDataItemStack get() { return i; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeDataItemStack(Type<ItemStack> typeInner)
	{
		super(typeInner, TypeObject.get(DataItemStack.class));
	}
	
	public TypeDataItemStack()
	{
		this(TypeItemStack.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public DataItemStack innerToOuter(ItemStack inner)
	{
		// TODO: Why on earth doesn't this line work.
		DataItemStack outer = DataItemStack.fromBukkit(inner);
		
		System.out.println("1 from inner: " + inner);
		System.out.println("2 to outer: " + outer.getId());
		System.out.println("3 to outer: " + MassiveCore.get().getGson().toJson(outer, DataItemStack.class));
		
		return outer;
	}

	@Override
	public ItemStack outerToInner(DataItemStack outer)
	{
		ItemStack inner = DataItemStack.toBukkit(outer);
		
		System.out.println("from outer: " + MassiveCore.get().getGson().toJson(outer, DataItemStack.class));
		System.out.println("to inner: " + inner);
		
		return inner;
	}	

}
