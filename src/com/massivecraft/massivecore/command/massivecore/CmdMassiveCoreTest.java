package com.massivecraft.massivecore.command.massivecore;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.TypeItemStack;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.InventoryUtil;

public class CmdMassiveCoreTest extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreTest()
	{
		// Aliases
		this.addAliases("test");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.TEST.node));
		this.addRequirements(RequirementIsPlayer.get());
		
		// VisibilityMode
		this.setVisibility(Visibility.SECRET);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		inform("helmet", InventoryUtil.getHelmet(me));
		inform("chestplate", InventoryUtil.getChestplate(me));
		inform("leggings", InventoryUtil.getLeggings(me));
		inform("boots", InventoryUtil.getBoots(me));
		inform("weapon", InventoryUtil.getWeapon(me));
		inform("shield", InventoryUtil.getShield(me));
		
		inform("all", InventoryUtil.getContentsAll(me.getInventory()));
		inform("storage", InventoryUtil.getContentsStorage(me.getInventory()));
		inform("armor", InventoryUtil.getContentsArmor(me.getInventory()));
		inform("extra", InventoryUtil.getContentsExtra(me.getInventory()));
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public Mson visualize(ItemStack item)
	{
		return TypeItemStack.get().getVisualMson(item, sender);
	}
	
	public Mson visualize(ItemStack... items)
	{
		List<Mson> msons = new MassiveList<>();
		for (ItemStack item : items)
		{
			msons.add(visualize(item));
		}
		return Mson.implode(msons, Mson.SPACE);
	}
		
	public void inform(String key, ItemStack... items)
	{
		
		
		message(mson(key, ": ", visualize(items)));
	}
	
}
