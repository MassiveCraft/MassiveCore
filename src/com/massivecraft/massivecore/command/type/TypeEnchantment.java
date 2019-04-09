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
	public static Enchantment[] getEnchantment = new Enchantment[]
			{
					Enchantment.PROTECTION_ENVIRONMENTAL,
					Enchantment.PROTECTION_FIRE,
					Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_EXPLOSIONS,
					Enchantment.PROTECTION_PROJECTILE,
					Enchantment.OXYGEN,
					Enchantment.WATER_WORKER,
					Enchantment.THORNS,
					Enchantment.DEPTH_STRIDER,
					Enchantment.FROST_WALKER,
					Enchantment.BINDING_CURSE,
					null,
					null,
					null,
					null,
					null,
					Enchantment.DAMAGE_ALL,
					Enchantment.DAMAGE_UNDEAD,
					Enchantment.DAMAGE_ARTHROPODS,
					Enchantment.KNOCKBACK,
					Enchantment.FIRE_ASPECT,
					Enchantment.LOOT_BONUS_MOBS,
					Enchantment.SWEEPING_EDGE,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					Enchantment.DIG_SPEED,
					Enchantment.SILK_TOUCH,
					Enchantment.DURABILITY,
					Enchantment.LOOT_BONUS_BLOCKS,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					Enchantment.ARROW_DAMAGE,
					Enchantment.ARROW_KNOCKBACK,
					Enchantment.ARROW_FIRE,
					Enchantment.ARROW_INFINITE,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					Enchantment.LUCK,
					Enchantment.LURE,
					null,
					null,
					Enchantment.LOYALTY,
					Enchantment.IMPALING,
					Enchantment.RIPTIDE,
					Enchantment.CHANNELING,
					null,
					Enchantment.MENDING,
					Enchantment.VANISHING_CURSE
			};

	public static Map<String, Integer> KEY_TO_ID = new MassiveMap<String, Integer>(
			Enchantment.PROTECTION_ENVIRONMENTAL.getKey().getKey(), 0, 
			Enchantment.PROTECTION_FIRE.getKey().getKey(), 1,
			Enchantment.PROTECTION_FALL.getKey().getKey(), 2,
			Enchantment.PROTECTION_EXPLOSIONS.getKey().getKey(), 3,
			Enchantment.PROTECTION_PROJECTILE.getKey().getKey(), 4,
			Enchantment.OXYGEN.getKey().getKey(), 5,
			Enchantment.WATER_WORKER.getKey().getKey(), 6,
			Enchantment.THORNS.getKey().getKey(), 7,
			Enchantment.DEPTH_STRIDER.getKey().getKey(), 8,
			Enchantment.FROST_WALKER.getKey().getKey(), 9,
			Enchantment.BINDING_CURSE.getKey().getKey(), 10,
			Enchantment.DAMAGE_ALL.getKey().getKey(), 16,
			Enchantment.DAMAGE_UNDEAD.getKey().getKey(), 17,
			Enchantment.DAMAGE_ARTHROPODS.getKey().getKey(), 18,
			Enchantment.KNOCKBACK.getKey().getKey(), 19,
			Enchantment.FIRE_ASPECT.getKey().getKey(), 20,
			Enchantment.LOOT_BONUS_MOBS.getKey().getKey(), 21,
			Enchantment.SWEEPING_EDGE.getKey().getKey(), 22,
			Enchantment.DIG_SPEED.getKey().getKey(), 32,
			Enchantment.SILK_TOUCH.getKey().getKey(), 33,
			Enchantment.DURABILITY.getKey().getKey(), 34,
			Enchantment.LOOT_BONUS_BLOCKS.getKey().getKey(), 35,
			Enchantment.ARROW_DAMAGE.getKey().getKey(), 48,
			Enchantment.ARROW_KNOCKBACK.getKey().getKey(), 49,
			Enchantment.ARROW_FIRE.getKey().getKey(), 50,
			Enchantment.ARROW_INFINITE.getKey().getKey(), 51,
			Enchantment.LUCK.getKey().getKey(), 61,
			Enchantment.LURE.getKey().getKey(), 62,
			Enchantment.LOYALTY.getKey().getKey(), 65,
			Enchantment.IMPALING.getKey().getKey(), 66,
			Enchantment.RIPTIDE.getKey().getKey(), 67,
			Enchantment.CHANNELING.getKey().getKey(), 68,
			Enchantment.MENDING.getKey().getKey(), 70,
			Enchantment.VANISHING_CURSE.getKey().getKey(), 71
	);

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
			65, new MassiveList<>("Loyalty", "LOYALTY"),
			66, new MassiveList<>("Impaling", "IMPALING"),
			67, new MassiveList<>("Riptide", "RIPTIDE"),
			68, new MassiveList<>("Channeling", "CHANNELING"),
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

	@Override
	public String getNameInner(Enchantment enchantment)
	{
		String rawname = enchantment.getKey().getKey();
		List<String> rawnames = ID_TO_RAWNAMES.get(KEY_TO_ID.get(enchantment.getKey().getKey()));
		if (rawnames != null) rawname = rawnames.get(0);
		return Txt.getNicedEnumString(rawname);
	}

	@Override
	public Set<String> getNamesInner(Enchantment enchantment)
	{
		// Create
		Set<String> ret = new MassiveSet<>();

		// Fill
		List<String> raws = new MassiveList<>();
		List<String> rawnames = ID_TO_RAWNAMES.get(KEY_TO_ID.get(enchantment.getKey().getKey()));
		if (rawnames != null) raws.addAll(rawnames);
		raws.add(enchantment.getKey().getKey());
		for (String raw : raws)
		{
			ret.add(Txt.getNicedEnumString(raw));
		}

		// Return
		return ret;
	}

	@Override
	public String getIdInner(Enchantment enchantment)
	{
		return String.valueOf(KEY_TO_ID.get(enchantment.getKey().getKey()));
	}

}
