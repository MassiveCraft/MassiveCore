package com.massivecraft.massivecore.mson;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.cmd.MassiveCommand;
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
	public MsonEventAction getEventActionType() { return this.action; }

	private final String value;
	public String getActionText() { return this.value; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	protected MsonEvent(MsonEventAction action, String value)
	{
		this.action = action;
		this.value = value;
	}

	// -------------------------------------------- //
	// FACTORY
	// -------------------------------------------- //

	public static MsonEvent valueOf(MsonEventAction type, String action)
	{
		return new MsonEvent(type, action);
	}

	// clickEvents
	public static MsonEvent openUrl(String url)
	{
		return MsonEvent.valueOf(MsonEventAction.OPEN_URL, url);
	}

	public static MsonEvent replace(String replace)
	{
		return MsonEvent.valueOf(MsonEventAction.SUGGEST_COMMAND , replace);
	}
	public static MsonEvent replace(MassiveCommand cmd, String... args)
	{
		return MsonEvent.replace(cmd.getCommandLine(args));
	}
	public static MsonEvent replace(MassiveCommand cmd, Iterable<String> args)
	{
		return MsonEvent.replace(cmd.getCommandLine(args));
	}
	
	public static MsonEvent performCmd(String cmd)
	{
		return MsonEvent.valueOf(MsonEventAction.RUN_COMMAND, cmd);
	}
	public static MsonEvent performCmd(MassiveCommand cmd, String... args)
	{
		return MsonEvent.performCmd(cmd.getCommandLine(args));
	}
	public static MsonEvent performCmd(MassiveCommand cmd, Iterable<String> args)
	{
		return MsonEvent.performCmd(cmd.getCommandLine(args));
	}

	// showText
	public static MsonEvent hoverText(String hoverText)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, hoverText);
	}

	public static MsonEvent hoverText(String... hoverTexts)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.implode(hoverTexts, "\n"));
	}

	public static MsonEvent hoverText(Collection<String> hoverTexts)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.implode(hoverTexts, "\n"));
	}
	
	// showTextParsed
	public static MsonEvent hoverTextParse(String hoverText)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.parse(hoverText));
	}

	public static MsonEvent hoverTextParse(String... hoverTexts)
	{
		return MsonEvent.valueOf(MsonEventAction.SHOW_TEXT, Txt.parse(Txt.implode(hoverTexts, "\n")));
	}

	public static MsonEvent hoverTextParse(Collection<String> hoverTexts)
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

	public boolean isHoverEvent() { return this.getEventActionType().isHoverAction(); }
	public boolean isClickEvent() { return this.getEventActionType().isClickAction(); }

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
