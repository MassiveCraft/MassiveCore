package com.massivecraft.massivecore.cmd.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;

public class TypePotionEffectType extends TypeAbstractSelect<PotionEffectType> implements TypeAllAble<PotionEffectType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePotionEffectType i = new TypePotionEffectType();
	public static TypePotionEffectType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public PotionEffectType select(String str, CommandSender sender) throws MassiveException
	{
		// Prepare
		str = getComparable(str);
		
		// Match Name
		for (PotionEffectType potionEffectType : PotionEffectType.values())
		{
			String name = getComparable(potionEffectType);
			if (str.equals(name)) return potionEffectType;
		}
		
		// :(
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		// Create Ret
		List<String> ret = new MassiveList<String>();
		
		// Match Name
		for (PotionEffectType potionEffectType : PotionEffectType.values())
		{
			if (potionEffectType == null) continue;
			String name = potionEffectType.getName();
			if (name == null) continue;
			
			ret.add(name);
		}
		
		// Return Ret
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.altNames(sender);
	}

	@Override
	public Collection<PotionEffectType> getAll(CommandSender sender)
	{
		return Arrays.asList(PotionEffectType.values());
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getComparable(PotionEffectType potionEffectType)
	{
		if (potionEffectType == null) return null;
		return getComparable(potionEffectType.getName());
	}
	
	public static String getComparable(String string)
	{
		if (string == null) return null;
		return string.toLowerCase();
	}

}
