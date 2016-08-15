package com.massivecraft.massivecore.mson;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.nms.NmsItem;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public final class MsonEvent implements Serializable
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	private static final long serialVersionUID = 1L;

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private final MsonEventAction action;
	public MsonEventAction getAction() { return this.action; }

	private final String value;
	public String getValue() { return this.value; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	protected MsonEvent(MsonEventAction action, String value)
	{
		this.action = action;
		this.value = value;
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
		return MsonEvent.valueOf(MsonEventAction.SHOW_ITEM, NmsItem.itemToString(item));
	}

	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //

	public MsonEventType getType() { return this.getAction().getType(); }

	// -------------------------------------------- //
	// EQUALS AND HASHCODE
	// -------------------------------------------- //

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(action);
		result = prime * result + Objects.hashCode(value);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (  ! (obj instanceof MsonEvent)) return false;
		MsonEvent that = (MsonEvent) obj;

		if ( ! MUtil.equals(this.action, that.action)) return false;
		if ( ! MUtil.equals(this.value, that.value)) return false;
		
		return true;
	}

}
