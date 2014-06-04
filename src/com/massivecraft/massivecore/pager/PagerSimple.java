package com.massivecraft.massivecore.pager;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.Txt;

public class PagerSimple<T> extends PagerAbstract<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Collection<? extends T> items;
	@Override public Collection<? extends T> getItems() { return items; }
	public void setItems(Collection<? extends T> items) { this.items = items; }

	private int itemsPerPage;
	@Override public int getItemsPerPage() { return this.itemsPerPage; }
	public void setItemsPerPage(int itemsPerPage) { this.itemsPerPage = itemsPerPage; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PagerSimple(Collection<? extends T> items, int itemsPerPage)
	{
		this.items = items;
		this.itemsPerPage = itemsPerPage;
	}
	
	public PagerSimple(Collection<? extends T> items, CommandSender sender)
	{
		this(items, (sender instanceof Player) ? Txt.PAGEHEIGHT_PLAYER : Txt.PAGEHEIGHT_CONSOLE);
	}

}
