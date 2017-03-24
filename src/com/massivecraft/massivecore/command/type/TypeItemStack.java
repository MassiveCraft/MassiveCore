package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class TypeItemStack extends TypeAbstract<ItemStack>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final ExceptionSet materialsAllowed;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeItemStack i = new TypeItemStack();
	public static TypeItemStack get() { return i; }
	
	public static TypeItemStack get(Material... materialWhitelist)
	{
		ExceptionSet materialsAllowed = new ExceptionSet(false, materialWhitelist);
		return new TypeItemStack(materialsAllowed);
	}
	
	public TypeItemStack(ExceptionSet materialsAllowed)
	{
		super(ItemStack.class);
		this.materialsAllowed = materialsAllowed;
	}
	
	public TypeItemStack()
	{
		this(new ExceptionSet(true));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Mson getVisualMsonInner(ItemStack value, CommandSender sender)
	{
		return Txt.createItemMson(value);
	}

	@Override
	public String getNameInner(ItemStack value)
	{
		return null;
	}

	@Override
	public String getIdInner(ItemStack value)
	{
		return null;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return null;
	}
	
	@Override
	public ItemStack read(String arg, CommandSender sender) throws MassiveException
	{
		if ( ! (sender instanceof Player)) throw new MassiveException().addMsg("<b>You must be a player to hold an item in your hand.");
		Player player = (Player)sender;
		
		ItemStack ret = InventoryUtil.getWeapon(player);
		if (InventoryUtil.isNothing(ret)) throw new MassiveException().addMsg("<b>You must hold an item in your hand.");
		
		Material material = ret.getType();
		if ( ! this.materialsAllowed.contains(material)) throw new MassiveException().addMsg("<h>%s <b>is not allowed.", Txt.getNicedEnum(material));
		
		ret = new ItemStack(ret);
		return ret;
	}

}
