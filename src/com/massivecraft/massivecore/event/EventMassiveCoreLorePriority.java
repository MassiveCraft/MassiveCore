package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.predicate.Predicate;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class EventMassiveCoreLorePriority extends EventMassiveCore
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	public static int PRIORITY_DEFAULT = 0;

	// Any lore added by a MassiveX plugin should start with this string and then be followed by its own unique sequence of characters.
	public static String LORE_PREFIX = ChatColor.BLACK.toString() + ChatColor.GOLD + ChatColor.WHITE;

	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //

	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }

	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //

	private final ItemStack item;
	public ItemStack getItem() { return this.item; }

	private final List<Entry<String, Integer>> lore;
	public List<Entry<String, Integer>> getLore() { return this.lore; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public EventMassiveCoreLorePriority(ItemStack item)
	{
		if (item == null) throw new NullPointerException("item");
		this.item = item;
		this.lore = getLoreEntries(item);
	}

	private static List<Entry<String, Integer>> getLoreEntries(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		if ( ! meta.hasLore()) return Collections.emptyList();

		List<Entry<String, Integer>> ret = new MassiveList<>();
		for (String line : meta.getLore())
		{
			ret.add(new SimpleEntry<>(line, PRIORITY_DEFAULT));
		}
		return ret;
	}

	// -------------------------------------------- //
	// UTILITY SORT
	// -------------------------------------------- //

	public void setPriorityByPredicate(Predicate<String> predicate, int priority)
	{
		// Look over all the lore ...
		for (Entry<String, Integer> loreEntry : this.getLore())
		{
			String line = loreEntry.getKey();

			// ... and if predicate matches ...
			if ( ! predicate.apply(line)) continue;

			// ... set priority.
			loreEntry.setValue(priority);
		}
	}

	public void setPriorityByPrefix(final String prefix, int priority)
	{
		Predicate<String> predicate = new Predicate<String>()
		{
			@Override public boolean apply(String type)
			{
				return type.startsWith(prefix);
			}
		};
		this.setPriorityByPredicate(predicate, priority);
	}

	public void setPriorityByRegex(final Pattern pattern, int priority)
	{
		Predicate<String> predicate = new Predicate<String>()
		{
			@Override public boolean apply(String type)
			{
				return pattern.matcher(type).matches();
			}
		};
		this.setPriorityByPredicate(predicate, priority);
	}

	public void setPriorityByRegex(final String regex, int priority)
	{
		setPriorityByRegex(Pattern.compile(regex), priority);
	}
}
