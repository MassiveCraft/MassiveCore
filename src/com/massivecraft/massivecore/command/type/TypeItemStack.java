package com.massivecraft.massivecore.command.type;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.Txt;

public class TypeItemStack extends TypeAbstract<ItemStack>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final ExceptionSet<Material> materialsAllowed;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeItemStack i = new TypeItemStack();
	public static TypeItemStack get() { return i; }
	
	public static TypeItemStack get(Material... materialWhitelist)
	{
		ExceptionSet<Material> materialsAllowed = new ExceptionSet<>(false, materialWhitelist);
		return new TypeItemStack(materialsAllowed);
	}
	
	public TypeItemStack(ExceptionSet<Material> materialsAllowed)
	{
		this.materialsAllowed = materialsAllowed;
	}
	
	public TypeItemStack()
	{
		this(new ExceptionSet<Material>(true));
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
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack read(String arg, CommandSender sender) throws MassiveException
	{
		if ( ! (sender instanceof Player)) throw new MassiveException().addMsg("<b>You must be a player to hold an item in your hand.");
		Player player = (Player)sender;
		
		ItemStack ret = player.getItemInHand();
		if (InventoryUtil.isNothing(ret)) throw new MassiveException().addMsg("<b>You must hold an item in your hand.");
		
		Material material = ret.getType();
		if ( ! this.materialsAllowed.contains(material)) throw new MassiveException().addMsg("<h>%s <b>is not allowed.", Txt.getNicedEnum(material));
		
		ret = new ItemStack(ret);
		return ret;
	}

}
