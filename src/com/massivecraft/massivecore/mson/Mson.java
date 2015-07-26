package com.massivecraft.massivecore.mson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;
import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.adapter.LowercaseEnumAdapter;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class Mson implements Serializable
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	private static final long serialVersionUID = 1L;

	public static final Pattern PARSE_PREFIX = Pattern.compile("\u00A7");
	
	public static final LowercaseEnumAdapter<ChatColor> ADAPTER_LOWERCASE_CHAT_COLOR = LowercaseEnumAdapter.get(ChatColor.class);
	public static final LowercaseEnumAdapter<MsonEventAction> ADAPTER_LOWERCASE_MSON_EVENT_ACTION = LowercaseEnumAdapter.get(MsonEventAction.class);
	
	// -------------------------------------------- //
	// GSON
	// -------------------------------------------- //

	public static final Gson GSON = new GsonBuilder()
	.disableHtmlEscaping()
	.registerTypeAdapter(ChatColor.class, ADAPTER_LOWERCASE_CHAT_COLOR)
	.registerTypeAdapter(MsonEventAction.class, ADAPTER_LOWERCASE_MSON_EVENT_ACTION)
	.create();

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	// FIELD: The Msons text
	// A parents text can't be null, then Mojang throws an exception.
	// It does not make sense for something which doesn't have extras
	// to not have text, because then it doesn't show up at all.
	private final String text;
	public String getText() { return this.text; }

	// FIELD: Color of the mson
	private final ChatColor color;
	public ChatColor getColor() { return this.color; }
	public ChatColor getEffectiveColor() { return color != null ? color : getInheritedColor(); }
	public ChatColor getInheritedColor() { return hasParent() ? getParent().getEffectiveColor() : null; }

	// FIELD: bold
	private final Boolean bold;
	public Boolean isBold() { return bold; }
	public boolean isEffectiveBold() { return bold != null ? bold : isInheritedBold(); }
	public boolean isInheritedBold() { return hasParent() && getParent().isEffectiveBold(); }

	// FIELD: italic
	private final Boolean italic;
	public Boolean isItalic() { return this.italic; }
	public boolean isEffectiveItalic() { return italic != null ? italic : isInheritedItalic(); }
	protected boolean isInheritedItalic() { return hasParent() && getParent().isEffectiveItalic(); }

	// FIELD: underlined
	private final Boolean underlined;
	public Boolean isUnderlined() { return this.underlined; }
	public boolean isEffectiveUnderlined() { return underlined != null ? underlined : isInheritedUnderlined(); }
	protected boolean isInheritedUnderlined() { return hasParent() && getParent().isEffectiveUnderlined(); }

	// FIELD: strikethrough
	private final Boolean strikethrough;
	public Boolean isStrikethrough() { return this.strikethrough; }
	public boolean isEffectiveStrikethrough() { return strikethrough != null ? strikethrough : isInheritedStrikethrough(); }
	protected boolean isInheritedStrikethrough() { return hasParent() && getParent().isEffectiveStrikethrough(); }

	// FIELD: obfuscated
	private final Boolean obfuscated;
	public Boolean isObfuscated() { return this.obfuscated; }
	public boolean isEffectiveObfuscated() { return obfuscated != null ? obfuscated : isInheritedObfuscated(); }
	protected boolean isInheritedObfuscated() { return hasParent() && getParent().isEffectiveObfuscated(); }

	// FIELD: The Events which happen when you click, hover over or shift-click the message
	private final MsonEvent clickEvent;
	public MsonEvent getClickEvent() { return this.clickEvent; }
	public MsonEvent getEffectiveClickEvent() { return clickEvent != null ? clickEvent : getInheritedClickEvent(); }
	protected MsonEvent getInheritedClickEvent() { return this.hasParent() ? this.getParent().getEffectiveClickEvent() : null; }

	private final MsonEvent hoverEvent;
	public MsonEvent getHoverEvent() { return this.hoverEvent; }
	public MsonEvent getEffectiveHoverEvent() { return hoverEvent != null ? hoverEvent : getInheritedHoverEvent(); }
	protected MsonEvent getInheritedHoverEvent() { return this.hasParent() ? this.getParent().getEffectiveHoverEvent() : null; }

	private final String insertionString;
	public String getInsertionString() { return this.insertionString; }
	public String getEffectiveInsertionString() { return insertionString != null ? insertionString : getInheritedInsertionString(); }
	protected String getInheritedInsertionString() { return this.hasParent() ? this.getParent().getEffectiveInsertionString() : null; }

	// The other parts of the message
	private final List<Mson> extra;
	public List<Mson> getExtra() { return this.extra; }
	public boolean hasExtra() { return this.getExtra() != null; }

	// Parent & Root
	private final transient Mson parent;
	public Mson getParent() { return this.parent; }
	public boolean hasParent() { return this.getParent() != null; }

	public boolean isRoot() { return this.getParent() == null; }
	public Mson getRoot()
	{
		Mson root = this;
		while (true)
		{
			Mson parent = root.getParent();
			if (parent == null) break;
			root = parent;
		}
		return root;
	}
	
	// -------------------------------------------- //
	// STATE CHECKING
	// -------------------------------------------- //
	
	public boolean isEmpty()
	{
		// It has text, not empty.
		if ( ! this.getText().isEmpty()) return false;

		if (this.hasExtra())
		{
			for (Mson extra : this.getExtra())
			{
				// It is empty
				if (extra.isEmpty()) continue;
				
				// It was not empty.
				return false;
			}
		}

		// We are empty.
		return true;
	}
	
	public boolean isTextOnly()
	{
		if (this.getColor() != null) return false;
		if (this.isBold() != null) return false;
		if (this.isItalic() != null) return false;
		if (this.isUnderlined() != null) return false;
		if (this.isStrikethrough() != null) return false;
		if (this.isObfuscated() != null) return false;
		if (this.getClickEvent() != null) return false;
		if (this.getHoverEvent() != null) return false;
		if (this.getInsertionString() != null) return false;
		if (this.hasExtra()) return false;
		return true;
	}
	
	public boolean hasSpecialBehaviour()
	{
		if (this.getClickEvent() != null) return true;
		if (this.getHoverEvent() != null) return true;
		if (this.getInsertionString() != null) return true;
		
		if (this.hasExtra())
		{
			for (Mson extra : this.getExtra())
			{
				if (extra.hasSpecialBehaviour()) return true;
			}
		}
		
		return false;
	}

	// -------------------------------------------- //
	// WITH FIELDS
	// -------------------------------------------- //
	
	public Mson text(String text) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson color(ChatColor color) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson bold(Boolean bold) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson italic(Boolean italic) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson underlined(Boolean underlined) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson strikethrough(Boolean strikethrough) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson obfuscated(Boolean obfuscated) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson clickEvent(MsonEvent clickEvent) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson hoverEvent(MsonEvent hoverEvent) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson insertionString(String insertionString) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson extra(List<Mson> extra) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	public Mson extra(Mson[] extra) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, ImmutableList.copyOf(extra), parent); }
	public Mson parent(Mson parent) { return Mson.valueOf(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent); }
	
	// -------------------------------------------- //
	// CONVENIENCE MSON EVENT
	// -------------------------------------------- //

	public Mson link(String link) { return this.clickEvent(MsonEvent.openUrl(link)); }
	
	public Mson suggest(String replace) { return this.clickEvent(MsonEvent.replace(replace)); }
	public Mson suggest(MassiveCommand command, String... args) { return this.clickEvent(MsonEvent.replace(command, args)); }
	public Mson suggest(MassiveCommand command, Iterable<String> args) { return this.clickEvent(MsonEvent.replace(command, args)); }
	
	public Mson command(String command) { return this.clickEvent(MsonEvent.performCmd(command)); }
	public Mson command(MassiveCommand command, String... args) { return this.clickEvent(MsonEvent.performCmd(command, args)); }
	public Mson command(MassiveCommand command, Iterable<String> args) { return this.clickEvent(MsonEvent.performCmd(command, args)); }
	
	public Mson tooltip(String tooltip) { return this.hoverEvent(MsonEvent.hoverText(tooltip)); }
	public Mson tooltip(String... tooltip) { return this.hoverEvent(MsonEvent.hoverText(tooltip)); }
	public Mson tooltip(Collection<String> tooltip) { return this.hoverEvent(MsonEvent.hoverText(tooltip)); }
	
	public Mson tooltipParse(String tooltip) { return this.hoverEvent(MsonEvent.hoverTextParse(tooltip)); }
	public Mson tooltipParse(String... tooltip) { return this.hoverEvent(MsonEvent.hoverTextParse(tooltip)); }
	public Mson tooltipParse(Collection<String> tooltip) { return this.hoverEvent(MsonEvent.hoverTextParse(tooltip)); }
	
	public Mson tooltip(ItemStack item) { return this.hoverEvent(MsonEvent.item(item)); }
	
	// -------------------------------------------- //
	// CONVENIENCE STYLE
	// -------------------------------------------- //
	
	public Mson style(ChatColor... styles)
	{
		Mson ret = this;
		for (ChatColor style : styles)
		{
			ret = ret.style(style);
		}
		return ret;
	}
	
	public Mson style(ChatColor style)
	{
		if (style == null) throw new NullPointerException("style");
		
		if (style == ChatColor.RESET) return this.removeStyles();
		if (style == ChatColor.BOLD) return this.bold(true);
		if (style == ChatColor.ITALIC) return this.italic(true);
		if (style == ChatColor.UNDERLINE) return this.underlined(true);
		if (style == ChatColor.STRIKETHROUGH) return this.strikethrough(true);
		if (style == ChatColor.MAGIC) return this.obfuscated(true);
		if (style.isColor()) return this.color(style);
		
		throw new UnsupportedOperationException(style.name());
	}
	
	public Mson removeStyles()
	{
		// NOTE: We can't use null.
		// Since we want to override color and format in parents.
		return Mson.valueOf(text, ChatColor.WHITE, false, false, false, false, false, clickEvent, hoverEvent, insertionString, extra, parent);
	}

	public Mson stripStyle()
	{
		Mson ret = Mson.valueOf(text, null, null, null, null, null, null, clickEvent, hoverEvent, insertionString, null, parent);
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				extra[i] = part.stripStyle();
				i++;
			}
			ret = ret.extra(extra);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	// Empty
	private static final Mson EMPTY = mson("");
	
	public static Mson mson()
	{
		return EMPTY;
	}
	
	// Text
	Mson(String text)
	{
		this(text, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public static Mson mson(String text)
	{
		return new Mson(text);
	}
	
	// Full
	Mson(String text, ChatColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, MsonEvent clickEvent, MsonEvent hoverEvent, String insertionString, List<Mson> extra, Mson parent)
	{
		// Text
		this.text = Objects.requireNonNull(text);
		
		// Color
		if (color != null && ! color.isColor()) throw new IllegalArgumentException(color.name() + " is not a color");
		this.color = color;
		
		// Format
		this.bold = bold;
		this.italic = italic;
		this.underlined = underlined;
		this.strikethrough = strikethrough;
		this.obfuscated = obfuscated;
		
		// Click event
		if ( clickEvent != null && ! clickEvent.isClickEvent()) throw new IllegalArgumentException(clickEvent.getEventActionType().name() + " is not a clickEventType");
		this.clickEvent = clickEvent;
		
		// Hover event
		if ( hoverEvent != null && ! hoverEvent.isHoverEvent()) throw new IllegalArgumentException(hoverEvent.getEventActionType().name() + " is not a hoverEventType");
		this.hoverEvent = hoverEvent;
		
		// Insertionstring
		this.insertionString = insertionString;
		
		// Mojang doesn't allow zero sized arrays, but null is fine. So null.
		if (extra != null && extra.size() == 0) extra = null;
		
		// Extra
		if (extra != null)
		{
			Mson[] extras = new Mson[extra.size()];
			for (ListIterator<Mson> it = extra.listIterator(); it.hasNext();)
			{
				int i = it.nextIndex();
				Mson part = it.next();
				extras[i] = part.parent(this);
			}
			this.extra = ImmutableList.copyOf(extras);
		}
		else
		{
			this.extra = null;
		}
		
		// Parent
		if (this == parent) throw new IllegalArgumentException("Parent can't be oneself.");
		this.parent = parent;
	}
	
	public static Mson valueOf(String text, ChatColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, MsonEvent clickEvent, MsonEvent hoverEvent, String insertionString, List<Mson> extra, Mson parent)
	{
		return new Mson(text, color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertionString, extra, parent);
	}
	
	// Object
	public static Mson mson(Object part)
	{
		return Mson.getMson(part);
	}
	
	public static Mson mson(Object... parts)
	{
		return Mson.getMson(parts);
	}

	private static Mson getMson(Object part)
	{
		if (part == null) throw new NullPointerException("part");

		if (part instanceof Mson)
		{
			return (Mson) part;
		}
		else if (part instanceof String)
		{
			String text = (String) part;
			return mson(text);
		}
		else if (part instanceof Collection<?>)
		{
			Collection<?> parts = (Collection<?>) part;
			List<Mson> msons = Mson.msons(parts);
			
			if (msons.isEmpty()) return mson();
			if (msons.size() == 1) return msons.get(0);
			
			return mson().extra(msons);
		}
		else if (part instanceof Object[])
		{
			Object[] parts = (Object[]) part;
			return getMson(Arrays.asList(parts));
		}
		else
		{
			throw new IllegalArgumentException("We only accept Strings, Msons, Collections and Arrays.");
		}
	}
	
	public static List<Mson> msons(Object... parts)
	{
		if (parts == null) throw new NullPointerException("parts");
		
		return msons(Arrays.asList(parts));
	}

	public static List<Mson> msons(Iterable<?> parts)
	{
		if (parts == null) throw new NullPointerException("parts");

		List<Mson> msons = new MassiveList<Mson>();

		for (Object part : parts)
		{
			msons.add(getMson(part));
		}

		return msons;
	}
	
	// -------------------------------------------- //
	// PARSE & FORMAT
	// -------------------------------------------- //

	public static Mson fromParsedMessage(String message)
	{
		if (message == null) throw new NullPointerException("message");

		// Everything must have a color.
		// Because when we split, we assume that each part starts with a color code.
		// Here we assure it starts with one.
		message = ensureStartsWithColorCode(message);

		// We split at color/format change.
		String[] parts = PARSE_PREFIX.split(message);

		// Since we start with a color, the first element will be empty.
		// We don't want that empty element.
		parts = Arrays.copyOfRange(parts, 1, parts.length);

		List<Mson> msons = new ArrayList<Mson>();

		ChatColor latestColor = null;
		Boolean bold = null;
		Boolean italic = null;
		Boolean underlined = null;
		Boolean strikethrough = null;
		Boolean obfuscated = null;

		for (String part : parts)
		{
			ChatColor color = ChatColor.getByChar(part.charAt(0));
			String text = part.substring(1);
			
			if ((color != null && color.isColor()) || color == ChatColor.RESET)
			{
				latestColor = color;
				bold = null;
				italic = null;
				underlined = null;
				strikethrough = null;
				obfuscated = null;
			}
			if (color == ChatColor.RESET) latestColor = null;
			else if (color == ChatColor.BOLD) bold = true;
			else if (color == ChatColor.ITALIC) italic = true;
			else if (color == ChatColor.UNDERLINE) underlined = true;
			else if (color == ChatColor.STRIKETHROUGH) strikethrough = true;
			else if (color == ChatColor.MAGIC) obfuscated = true;
			
			// Don't add empty msons.
			if (text.isEmpty()) continue;
			
			Mson mson = Mson.valueOf(text, latestColor, bold, italic, underlined, strikethrough, obfuscated, null, null, null, null, null);

			msons.add(mson);
		}

		return Mson.mson(msons);
	}

	private static String ensureStartsWithColorCode(String message)
	{
		if ( ! message.startsWith("\u00A7"))
		{
			message = ChatColor.RESET + message;
		}
		return message;
	}

	// Parse redirects, convert to Mson directly
	public static Mson parse(String string) { return Mson.fromParsedMessage(Txt.parse(string)); }
	public static Mson parse(String format, Object... args) { return Mson.fromParsedMessage(Txt.parse(format, args)); }

	public static Mson format(String format, Object... args)
	{
		return Mson.mson(String.format(format, args));
	}

	// -------------------------------------------- //
	// STRING LIKE METHODS
	// -------------------------------------------- //
	
	// Case
	public Mson toLowerCase()
	{
		Mson ret = this.text(this.getText().toLowerCase());
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				extra[i] = part.toLowerCase();
				i++;
			}
			ret = ret.extra(extra);
		}

		return ret;
	}
	
	public Mson toUpperCase()
	{
		Mson ret = this.text(this.getText().toUpperCase());
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				extra[i] = part.toUpperCase();
				i++;
			}
			ret = ret.extra(extra);
		}

		return ret;
	}
	
	public Mson uppercaseFirst()
	{
		if ( ! this.getText().isEmpty())
		{
			return this.text(Txt.upperCaseFirst(this.getText()));
		}
		
		Mson ret = this;
		boolean uppercased = false;
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				if ( ! uppercased)
				{
					Mson uppercase = part.uppercaseFirst();
					uppercased = (uppercase != part);
					part = uppercase;
				}
				extra[i] = part;
				i++;
			}
			if (uppercased)
			{
				ret = ret.extra(extra);
			}
		}
		
		return ret;
	}
	
	// Whitespace
	public Mson trim()
	{
		Mson ret = this.text(this.getText().trim());
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				extra[i] = part.trim();
				i++;
			}
			ret = ret.extra(extra);
		}

		return ret;
	}
	
	// Replace
	public Mson replace(char oldChar, char newChar)
	{
		Mson ret = this.text(this.getText().replace(oldChar, newChar));
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				extra[i] = part.replace(oldChar, newChar);
				i++;
			}
			ret = ret.extra(extra);
		}

		return ret;
	}
	
	public Mson replace(CharSequence replace, CharSequence replacement)
	{
		if (replace == null) throw new NullPointerException("replace");
		if (replacement == null) throw new NullPointerException("replacement");
		
		Mson ret = this.text(this.getText().replace(replace, replacement));
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				extra[i] = part.replace(replace, replacement);
				i++;
			}
			ret = ret.extra(extra);
		}

		return ret;
	}
	
	public Mson replaceAll(String regex, String replacement)
	{
		if (regex == null) throw new NullPointerException("regex");
		if (replacement == null) throw new NullPointerException("replacement");

		return replaceAll(Pattern.compile(regex), replacement);
	}
	
	public Mson replaceAll(Pattern pattern, String replacement)
	{
		if (pattern == null) throw new NullPointerException("pattern");
		if (replacement == null) throw new NullPointerException("replacement");
		
		Mson ret = this.text(pattern.matcher(this.getText()).replaceAll(replacement));
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				extra[i] = part.replaceAll(pattern, replacement);
				i++;
			}
			ret = ret.extra(extra);
		}

		return ret;
	}

	// Special replace all
	
	public Mson replaceAll(String regex, Mson replacement)
	{
		if (regex == null) throw new NullPointerException("regex");
		if (replacement == null) throw new NullPointerException("replacement");
		return this.replaceAll(Pattern.compile(regex), replacement);
	}
	
	public Mson replaceAll(Pattern pattern, final Mson replacement)
	{
		if (pattern == null) throw new NullPointerException("pattern");
		if (replacement == null) throw new NullPointerException("replacement");
		MsonReplacement replacer = new MsonReplacement()
		{
			@Override
			public Mson getReplacement(String match)
			{
				return replacement;
			}
			
		};
		return this.replaceAll(pattern, replacer);
	}
	
	public Mson replaceAll(String regex, MsonReplacement replacer)
	{
		if (regex == null) throw new NullPointerException("regex");
		if (replacer == null) throw new NullPointerException("replacer");
		return this.replaceAll(Pattern.compile(regex), replacer);
	}
	
	public Mson replaceAll(Pattern pattern, MsonReplacement replacer)
	{
		if (pattern == null) throw new NullPointerException("pattern");
		if (replacer == null) throw new NullPointerException("replacer");
		
		Mson ret = this.text("");
		
		List<Mson> msons = new ArrayList<Mson>();
		StringBuffer currentString = new StringBuffer();
		Matcher matcher = pattern.matcher(getText());
		while (matcher.find())
		{
			String match = matcher.group(0);
			Mson replacement = replacer.getReplacement(match);
			
			// Add the match
			if (replacement == null) matcher.appendReplacement(currentString, match);
			
			// Add the string
			else if (replacement.isTextOnly()) matcher.appendReplacement(currentString, replacement.getText());
			
			// Add the mson
			else
			{
				// Fixup current string
				matcher.appendReplacement(currentString, "");
				if (addStringBuffer(msons, currentString)) currentString = new StringBuffer();
				
				// Add this replacement
				msons.add((Mson) replacement);
			}
		}
		
		// Add the remaining string pieces
		matcher.appendTail(currentString);
		addStringBuffer(msons, currentString);
		
		// Recurse on extras.
		if (this.hasExtra())
		{
			for (Mson extra : this.getExtra())
			{
				msons.add(extra.replaceAll(pattern, replacer));
			}
		}
		
		// Set extras
		ret = ret.extra(msons);
		
		return ret;
	}
	
	private static boolean addStringBuffer(List<Mson> msons, StringBuffer buffer)
	{
		if (buffer.length() == 0) return false;
		Mson mson = mson(buffer.toString());
		msons.add(mson);
		return true;
	}

	public Mson replaceAll(Mson replace, Mson replacement)
	{
		if (replace == null) throw new NullPointerException("replace");
		if (replacement == null) throw new NullPointerException("replacement");
		
		Mson ret = this;
		
		if (this.hasExtra())
		{
			Mson[] extra = new Mson[this.getExtra().size()];
			int i = 0;
			for (Mson part : this.getExtra())
			{
				extra[i] = part.equals(replace) ? replacement : part;
				i++;
			}
			ret = ret.extra(extra);
		}

		return ret;
	}

	// -------------------------------------------- //
	// SEND
	// -------------------------------------------- //

	// All
	public boolean sendAll()
	{
		return Mixin.messageRawAll(this);
	}

	// Predictate
	public boolean sendPredictate(Predictate<CommandSender> predictate)
	{
		return Mixin.messageRawPredictate(predictate, this);
	}

	// One
	public boolean sendOne(Object senderObject)
	{
		return Mixin.messageRawOne(senderObject, this);
	}
	
	// -------------------------------------------- //
	// TO JSON, RAW, PLAIN & STRING
	// -------------------------------------------- //
	
	public JsonElement toJson()
	{
		return GSON.toJsonTree(this);
	}
	public static Mson fromJson(JsonObject json)
	{
		return GSON.fromJson(json, Mson.class);
	}
	
	private transient String raw = null;
	public String toRaw()
	{
		if (raw == null) raw = this.toJson().toString();
		return raw;
	}
	
	public String toPlain()
	{
		StringBuilder ret = new StringBuilder();
		this.toPlain0(ret);
		return ret.toString();
	}

	private void toPlain0(StringBuilder builder)
	{
		if ( ! this.getText().isEmpty())
		{
			// Color must be put in BEFORE formatting.
			// http://minecraft.gamepedia.com/Formatting_codes#Formatting_codes

			if (this.getEffectiveColor() != null) builder.append(this.getEffectiveColor());
			if (this.isEffectiveBold()) builder.append(ChatColor.BOLD);
			if (this.isEffectiveItalic()) builder.append(ChatColor.ITALIC);
			if (this.isEffectiveUnderlined()) builder.append(ChatColor.UNDERLINE);
			if (this.isEffectiveStrikethrough()) builder.append(ChatColor.STRIKETHROUGH);
			if (this.isEffectiveObfuscated()) builder.append(ChatColor.MAGIC);
			builder.append(this.getText());
		}

		if (this.hasExtra())
		{
			for (Mson part : this.getExtra())
			{
				builder.append(ChatColor.RESET);
				part.toPlain0(builder);
			}
		}
	}
	
	@Override
	public String toString()
	{
		return this.toRaw();
	}

	// -------------------------------------------- //
	// EQUALS AND HASHCODE
	// -------------------------------------------- //

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(this.text);
		result = prime * result + Objects.hashCode(this.color);
		result = prime * result + Objects.hashCode(this.bold);
		result = prime * result + Objects.hashCode(this.italic);
		result = prime * result + Objects.hashCode(this.obfuscated);
		result = prime * result + Objects.hashCode(this.strikethrough);
		result = prime * result + Objects.hashCode(this.underlined);
		result = prime * result + Objects.hashCode(this.clickEvent);
		result = prime * result + Objects.hashCode(this.hoverEvent);
		result = prime * result + Objects.hashCode(this.insertionString);
		result = prime * result + Objects.hashCode(this.extra);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if ( ! (obj instanceof Mson)) return false;
		Mson that = (Mson) obj;

		if ( ! MUtil.equals(this.text, that.text)) return false;
		if ( ! MUtil.equals(this.color, that.color)) return false;
		if ( ! MUtil.equals(this.bold, that.bold)) return false;
		if ( ! MUtil.equals(this.italic, that.italic)) return false;
		if ( ! MUtil.equals(this.obfuscated, that.obfuscated)) return false;
		if ( ! MUtil.equals(this.strikethrough, that.strikethrough)) return false;
		if ( ! MUtil.equals(this.underlined, that.underlined)) return false;
		if ( ! MUtil.equals(this.clickEvent, that.clickEvent)) return false;
		if ( ! MUtil.equals(this.hoverEvent, that.hoverEvent)) return false;
		if ( ! MUtil.equals(this.insertionString, that.insertionString)) return false;
		if ( ! MUtil.equals(this.extra, that.extra)) return false;

		return true;
	}

}
