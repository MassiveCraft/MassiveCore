package com.massivecraft.massivecore.command.type;

import java.util.Collections;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.util.Txt;

public class TypeEnchantment extends TypeAbstractChoice<Enchantment>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeEnchantment i = new TypeEnchantment();
	public static TypeEnchantment get() { return i; }
	public TypeEnchantment()
	{
		this.setVisualColor(ChatColor.AQUA);
		this.setAll(Enchantment.values());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getNameInner(Enchantment enchantment)
	{
		return Txt.getNicedEnumString(enchantment.getName());
	}
	
	@Override
	public Set<String> getNamesInner(Enchantment enchantment)
	{
		Set<String> ret = new MassiveSet<>();
		ret.add(this.getName(enchantment));
		
		if (Enchantment.DAMAGE_ALL.equals(enchantment)) Collections.addAll(ret, "Sharpness");
		if (Enchantment.DAMAGE_ARTHROPODS.equals(enchantment)) Collections.addAll(ret, "BaneOfArthropdos");
		if (Enchantment.DAMAGE_UNDEAD.equals(enchantment)) Collections.addAll(ret, "Smite");
		if (Enchantment.DIG_SPEED.equals(enchantment)) Collections.addAll(ret, "Efficiency");
		if (Enchantment.DURABILITY.equals(enchantment)) Collections.addAll(ret, "Unbreaking");
		if (Enchantment.THORNS.equals(enchantment)) Collections.addAll(ret);
		if (Enchantment.FIRE_ASPECT.equals(enchantment)) Collections.addAll(ret);
		if (Enchantment.KNOCKBACK.equals(enchantment)) Collections.addAll(ret);
		if (Enchantment.LOOT_BONUS_BLOCKS.equals(enchantment)) Collections.addAll(ret, "Fortune");
		if (Enchantment.LOOT_BONUS_MOBS.equals(enchantment)) Collections.addAll(ret, "Looting");
		if (Enchantment.OXYGEN.equals(enchantment)) Collections.addAll(ret, "Respiration", "Breathing");
		if (Enchantment.PROTECTION_ENVIRONMENTAL.equals(enchantment)) Collections.addAll(ret, "Protection");
		if (Enchantment.PROTECTION_EXPLOSIONS.equals(enchantment)) Collections.addAll(ret, "BlastProtection", "ExplosionProtection");
		if (Enchantment.PROTECTION_FALL.equals(enchantment)) Collections.addAll(ret, "FeatherFalling", "FallProtection");
		if (Enchantment.PROTECTION_FIRE.equals(enchantment)) Collections.addAll(ret, "FireProtection");
		if (Enchantment.PROTECTION_PROJECTILE.equals(enchantment)) Collections.addAll(ret, "ProectileProtection");
		if (Enchantment.SILK_TOUCH.equals(enchantment)) Collections.addAll(ret);
		if (Enchantment.WATER_WORKER.equals(enchantment)) Collections.addAll(ret, "AquaAffinity");
		if (Enchantment.ARROW_FIRE.equals(enchantment)) Collections.addAll(ret, "Flame");
		if (Enchantment.ARROW_DAMAGE.equals(enchantment)) Collections.addAll(ret, "Power");
		if (Enchantment.ARROW_KNOCKBACK.equals(enchantment)) Collections.addAll(ret, "Punch");
		if (Enchantment.ARROW_INFINITE.equals(enchantment)) Collections.addAll(ret, "Infinity");
		if (Enchantment.LUCK.equals(enchantment)) Collections.addAll(ret, "LuckOfSea", "LuckOfTheSea");
		if (Enchantment.LURE.equals(enchantment)) Collections.addAll(ret);
		
		return ret;
	}
	
	@Override
	public String getIdInner(Enchantment enchantment)
	{
		return enchantment.getName();
	}

}
