package com.massivecraft.massivecore.mson;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.nms.NmsItemStackTooltip;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public final class MsonEvent implements Serializable
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	private static final long serialVersionUID = 1L;

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	// TODO: should be final. only temporairly mutable for repairs.
	private MsonEventAction action;
	public MsonEventAction getAction() { return this.action; }
	
	// TODO: should be final. only temporairly mutable for repairs.
	@Deprecated
	public void setAction(MsonEventAction action) { this.action = action; }

	private final String value;
	public String getValue() { return this.value; }

	// -------------------------------------------- //
	// REPAIR
	// -------------------------------------------- //
	// TODO: Remove this soon.
	
	public void repair()
	{
		MsonEventAction action = this.getAction();
		if (action != null) return;
		
		String value = this.getValue();
		if (value == null) return;
		
		action = guessAction(value);
		this.setAction(action);
	}
	
	private static MsonEventAction guessAction(String value)
	{
		if (value.startsWith("{id:")) return MsonEventAction.SHOW_ITEM;
		if (value.startsWith("/")) return MsonEventAction.SUGGEST_COMMAND;
		if (value.startsWith("http")) return MsonEventAction.OPEN_URL;
		return MsonEventAction.SHOW_TEXT;
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	protected MsonEvent(MsonEventAction action, String value)
	{
		this.action = action;
		this.value = value;
	}
	
	// NoArg Constructor for GSON 
	protected MsonEvent()
	{
		this(null, null);
	}

	// -------------------------------------------- //
	// TOOLTIP
	// -------------------------------------------- //
	
	public String createTooltip()
	{
		String prefix = this.getAction().getTooltipPrefix();
		if (prefix == null) return null;
		return prefix + this.getValue();
	}
	
	// -------------------------------------------- //
	// FACTORY
	// -------------------------------------------- //

	public static MsonEvent valueOf(MsonEventAction action, String value)
	{
		return new MsonEvent(action, value);
	}

	// clickEvents
	public static MsonEvent link(String url)
	{
		return MsonEvent.valueOf(MsonEventAction.OPEN_URL, url);
	}

	public static MsonEvent suggest(String replace)
	{
		return MsonEvent.valueOf(MsonEventAction.SUGGEST_COMMAND, replace);
	}
	public static MsonEvent suggest(MassiveCommand cmd, String... args)
	{
		return MsonEvent.suggest(cmd.getCommandLine(args));
	}
	public static MsonEvent suggest(MassiveCommand cmd, Iterable<String> args)
	{
		return MsonEvent.suggest(cmd.getCommandLine(args));
	}
	
	public static MsonEvent command(String cmd)
	{
		return MsonEvent.valueOf(MsonEventAction.RUN_COMMAND, cmd);
	}
	public static MsonEvent command(MassiveCommand cmd, String... args)
	{
		return MsonEvent.command(cmd.getCommandLine(args));
	}
	public static MsonEvent command(MassiveCommand cmd, Iterable<String> args)
	{
		return MsonEvent.command(cmd.getCommandLine(args));
	}

	// showText
	public static MsonEvent tooltip(String hoverText)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, hoverText);
	}

	public static MsonEvent tooltip(String... hoverTexts)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.implode(hoverTexts, "\n"));
	}

	public static MsonEvent tooltip(Collection<String> hoverTexts)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.implode(hoverTexts, "\n"));
	}
	
	// showTextParsed
	public static MsonEvent tooltipParse(String hoverText)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.parse(hoverText));
	}

	public static MsonEvent tooltipParse(String... hoverTexts)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.parse(Txt.implode(hoverTexts, "\n")));
	}

	public static MsonEvent tooltipParse(Collection<String> hoverTexts)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.parse(Txt.implode(hoverTexts, "\n")));
	}
	
	// showItem
	public static MsonEvent item(ItemStack item)
	{
		if (item == null) throw new NullPointerException("item");
		item = getItemSanitizedForTooltip(item);
		String value = NmsItemStackTooltip.get().getNbtStringTooltip(item);
		return MsonEvent.valueOf(MsonEventAction.SHOW_ITEM, value);
	}
	
	private static ItemStack getItemSanitizedForTooltip(ItemStack item)
	{
		if (item == null) throw new NullPointerException("item");
		
		if (!item.hasItemMeta()) return item;
		
		ItemMeta meta = item.getItemMeta();
		
		if (meta instanceof BookMeta)
		{
			BookMeta book = (BookMeta)meta;
			book.setPages();
			item = item.clone();
			item.setItemMeta(meta);
			return item;
		}
		
		return item;
	}

	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //

	public MsonEventType getType() { return this.getAction().getType(); }
	
	// -------------------------------------------- //
	// JSON
	// -------------------------------------------- //
	
	public static MsonEvent fromJson(JsonElement json)
	{
		return Mson.getGson(true).fromJson(json, MsonEvent.class);
	}
	
	public static JsonElement toJson(MsonEvent event)
	{
		return Mson.getGson(true).toJsonTree(event);
	}

	// -------------------------------------------- //
	// EQUALS AND HASHCODE
	// -------------------------------------------- //

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.action,
			this.value
		);
	}

	@Override
	public boolean equals(Object object)
	{
		if (this == object) return true;
		if ( ! (object instanceof MsonEvent)) return false;
		MsonEvent that = (MsonEvent) object;
		return MUtil.equals(
			this.action, that.action,
			this.value, that.value
		);
	}

}
