package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeEnchantment extends TypeAbstractChoice<Enchantment>
{
	// -------------------------------------------- //
	// DATA
	// -------------------------------------------- //
	// http://minecraft.gamepedia.com/Enchanting#Enchantments
	
	// The first name is taken from the wiki. Those names are those people think of.
	// The second name is the Bukkit enum name.
	// Thereafter comes assorted extras
	public static Map<Integer, List<String>> ID_TO_RAWNAMES = new MassiveMap<Integer, List<String>>(
		0, new MassiveList<>("Protection", "PROTECTION_ENVIRONMENTAL"),
		1, new MassiveList<>("Fire Protection", "PROTECTION_FIRE"),
		2, new MassiveList<>("Feather Falling", "PROTECTION_FALL", "FallProtection"),
		3, new MassiveList<>("Blast Protection", "PROTECTION_EXPLOSIONS", "ExplosionProtection"),
		4, new MassiveList<>("Projectile Protection", "PROTECTION_PROJECTILE", "ProjectileProtection"),
		5, new MassiveList<>("Respiration", "OXYGEN", "Breathing"),
		6, new MassiveList<>("Aqua Affinity", "WATER_WORKER"),
		7, new MassiveList<>("Thorns", "THORNS"),
		8, new MassiveList<>("Depth Strider", "DEPTH_STRIDER"),
		9, new MassiveList<>("Frost Walker", "FROST_WALKER"),
		10, new MassiveList<>("Curse of Binding", "BINDING_CURSE"),
		16, new MassiveList<>("Sharpness", "DAMAGE_ALL"),
		17, new MassiveList<>("Smite", "DAMAGE_UNDEAD"),
		18, new MassiveList<>("Bane of Arthropods", "DAMAGE_ARTHROPODS", "BaneArthropods", "Arthropods"),
		19, new MassiveList<>("Knockback", "KNOCKBACK"),
		20, new MassiveList<>("Fire Aspect", "FIRE_ASPECT"),
		21, new MassiveList<>("Looting", "LOOT_BONUS_MOBS"),
		22, new MassiveList<>("Sweeping Edge", "SWEEPING_EDGE"),
		32, new MassiveList<>("Efficiency", "DIG_SPEED"),
		33, new MassiveList<>("Silk Touch", "SILK_TOUCH"),
		34, new MassiveList<>("Unbreaking", "DURABILITY"),
		35, new MassiveList<>("Fortune", "LOOT_BONUS_BLOCKS"),
		48, new MassiveList<>("Power", "ARROW_DAMAGE"),
		49, new MassiveList<>("Punch", "ARROW_KNOCKBACK"),
		50, new MassiveList<>("Flame", "ARROW_FIRE"),
		51, new MassiveList<>("Infinity", "ARROW_INFINITE", "ArrowInfinity"),
		61, new MassiveList<>("Luck of the Sea", "LUCK", "LuckOfSea", "LuckTheSea", "LuckSea"),
		62, new MassiveList<>("Lure", "LURE"),
		70, new MassiveList<>("Mending", "MENDING"),
		71, new MassiveList<>("Curse of Vanishing", "VANISHING_CURSE")
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeEnchantment i = new TypeEnchantment();
	public static TypeEnchantment get() { return i; }
	public TypeEnchantment()
	{
		super(Enchantment.class);
		this.setVisualColor(ChatColor.AQUA);
		this.setAll(Enchantment.values());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public String getNameInner(Enchantment enchantment)
	{
		String rawname = enchantment.getName();
		List<String> rawnames = ID_TO_RAWNAMES.get(enchantment.getId());
		if (rawnames != null) rawname = rawnames.get(0);
		return Txt.getNicedEnumString(rawname);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Set<String> getNamesInner(Enchantment enchantment)
	{
		// Create
		Set<String> ret = new MassiveSet<>();
		
		// Fill
		List<String> raws = new MassiveList<>();
		List<String> rawnames = ID_TO_RAWNAMES.get(enchantment.getId());
		if (rawnames != null) raws.addAll(rawnames);
		raws.add(enchantment.getName());
		for (String raw : raws)
		{
			ret.add(Txt.getNicedEnumString(raw));
		}
		
		// Return
		return ret;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getIdInner(Enchantment enchantment)
	{
		return String.valueOf(enchantment.getId());
	}

}
