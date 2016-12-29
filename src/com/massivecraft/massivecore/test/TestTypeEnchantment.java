package com.massivecraft.massivecore.test;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.TypeEnchantment;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TestTypeEnchantment extends Test
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TestTypeEnchantment i = new TestTypeEnchantment();
	public static TestTypeEnchantment get() { return i; }
	
	// -------------------------------------------- //
	// TEST
	// -------------------------------------------- //
	
	@Override
	public void test()
	{
		final List<Enchantment> enchantments = new MassiveList<>(Arrays.asList(Enchantment.values()));
		
		for (Iterator<Enchantment> iterator = enchantments.iterator(); iterator.hasNext();)
		{
			Enchantment enchantment = iterator.next();
			if (TypeEnchantment.ID_TO_RAWNAMES.containsKey(enchantment.getId()))
			{
				iterator.remove();
			}
		}
		
		for (Enchantment enchantment : enchantments)
		{
			String issue = Txt.parse("<i>The enchantment <h>%s (%d)<i> lacks nicename in TypeEnchantment.", enchantment.getName(), enchantment.getId());
			this.addIssue(issue);
		}
		
	}
}
