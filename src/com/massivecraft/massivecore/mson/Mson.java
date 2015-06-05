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

import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.adapter.LowercaseEnumAdapter;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

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
	protected String text = "";
	public String text() { return this.text; }
	public Mson text(String text) { this.text = Objects.requireNonNull(text); return this; }
	public boolean textHas() { return ! this.text().isEmpty(); }

	// FIELD: Color of the mson
	protected ChatColor color = null;
	public ChatColor color() { return this.color; }
	public ChatColor colorEffective() { return color() != null ? color() : colorInherited(); }
	public ChatColor colorInherited() { return parentHas() ? parent().colorEffective() : null; }
	public Mson color(ChatColor color)
	{
		if (color != null && ! color.isColor()) throw new IllegalArgumentException("Color must be a color.");
		this.color = color;
		return this;
	}

	// FIELD: bold
	protected Boolean bold = null;
	public Boolean bold() { return bold; }
	public Mson bold(Boolean bold) { this.bold = bold; return this; }
	public boolean boldEffective() { return bold() != null ? bold() : boldInherited(); }
	public boolean boldInherited() { return parentHas() && parent().boldEffective(); }

	// FIELD: italic
	protected Boolean italic = null;
	public Boolean italic() { return this.italic; }
	public Mson italic(Boolean italic) { this.italic = italic; return this; }
	public boolean italicEffective() { return italic() != null ? italic() : italicInherited(); }
	protected boolean italicInherited() { return parentHas() && parent().italicEffective(); }

	// FIELD: underlined
	protected Boolean underlined = null;
	public Boolean underlined() { return this.underlined; }
	public Mson underlined(Boolean underlined) { this.underlined = underlined; return this; }
	public boolean underlinedEffective() { return italic() != null ? italic() : italicInherited(); }
	protected boolean underlinedInherited() { return parentHas() && parent().underlinedEffective(); }

	// FIELD: strikethrough
	protected Boolean strikethrough = null;
	public Boolean strikethrough() { return this.strikethrough; }
	public Mson strikethrough(Boolean strikethrough) { this.strikethrough = strikethrough; return this; }
	public boolean strikethroughEffective() { return strikethrough() != null ? strikethrough() : strikethroughInherited(); }
	protected boolean strikethroughInherited() { return parentHas() && parent().strikethroughEffective(); }

	// FIELD: obfuscated
	protected Boolean obfuscated = null;
	public Boolean obfuscated() { return this.obfuscated; }
	public Mson obfuscated(Boolean obfuscated) { this.obfuscated = obfuscated; return this; }
	public boolean obfuscatedEffective() { return obfuscated() != null ? obfuscated() : obfuscatedInherited(); }
	protected boolean obfuscatedInherited() { return parentHas() && parent().obfuscatedEffective(); }

	// FIELD: The Events which happen when you click, hover over or shift-click the message
	protected MsonEvent clickEvent = null;
	public MsonEvent clickEvent() { return this.clickEvent; }
	public MsonEvent clickEventEffective() { return clickEvent() != null ? clickEvent() : clickEventInherited(); }
	protected MsonEvent clickEventInherited() { return this.parentHas() ? this.parent().clickEventEffective() : null; }
	public Mson clickEvent(MsonEvent clickEvent)
	{
		if (clickEvent != null && ! clickEvent.isClickEvent()) throw new IllegalArgumentException("ClickEvent may not be a HoverEvent.");
		this.clickEvent = clickEvent;
		return this;
	}

	protected MsonEvent hoverEvent = null;
	public MsonEvent hoverEvent() { return this.hoverEvent; }
	public MsonEvent hoverEventEffective() { return hoverEvent() != null ? hoverEvent() : hoverEventInherited(); }
	protected MsonEvent hoverEventInherited() { return this.parentHas() ? this.parent().hoverEventEffective() : null; }
	public Mson hoverEvent(MsonEvent hoverEvent)
	{
		if (hoverEvent != null && ! hoverEvent.isHoverEvent()) throw new IllegalArgumentException("HoverEvent may not be a ClickEvent.");
		this.hoverEvent = hoverEvent;
		return this;
	}

	protected String insertionString = null;
	public String insertionString() { return this.insertionString; }
	public String insertionStringEffective() { return insertionString() != null ? insertionString() : insertionStringInherited(); }
	public Mson insertionString(String insertionString) { this.insertionString = insertionString; return this; }
	protected String insertionStringInherited() { return this.parentHas() ? this.parent().insertionStringEffective() : null; }

	// The other parts of the message
	protected List<Mson> extra = null;
	public List<Mson> extra() { return this.extra; }
	public Mson extra(List<Mson> extra) { this.extra = extra; return this; }
	public boolean extraHas() { return this.extra() != null; }

	public List<Mson> extraCreative()
	{
		if ( ! this.extraHas()) this.extra(new MassiveList<Mson>());
		return this.extra();
	}

	// Parent & Root
	protected transient Mson parent = null;
	public Mson parent() { return this.parent; }
	public Mson parent(Mson parent) { this.parent = parent; return this; }
	public boolean parentHas() { return this.parent() != null; }

	public Mson parentCreative()
	{
		if ( ! this.parentHas()) this.parent(new Mson());
		return this.parent();
	}

	public boolean isRoot() { return this.parent() == null; }
	public Mson root()
	{
		Mson root = this;
		while (true)
		{
			Mson parent = root.parent();
			if (parent == null) break;
			root = parent;
		}
		return root;
	}

	// -------------------------------------------- //
	// CONVENIENCE MSON EVENT
	// -------------------------------------------- //

	public Mson link(String link) { this.clickEvent(MsonEvent.openUrl(link)); return this; }
	
	public Mson suggest(String replace) { this.clickEvent(MsonEvent.replace(replace)); return this; }
	
	public Mson command(String command) { this.clickEvent(MsonEvent.performCmd(command)); return this; }
	
	public Mson tooltip(String tooltip) { this.hoverEvent(MsonEvent.hoverText(text)); return this; }
	public Mson tooltip(String... tooltip) { this.hoverEvent(MsonEvent.hoverText(text)); return this; }
	public Mson tooltip(Collection<String> tooltip) { this.hoverEvent(MsonEvent.hoverText(text)); return this; }
	
	public Mson tooltipParse(String tooltip) { this.hoverEvent(MsonEvent.hoverTextParse(text)); return this; }
	public Mson tooltipParse(String... tooltip) { this.hoverEvent(MsonEvent.hoverTextParse(text)); return this; }
	public Mson tooltipParse(Collection<String> tooltip) { this.hoverEvent(MsonEvent.hoverTextParse(text)); return this; }
	
	public Mson tooltip(ItemStack item) { this.hoverEvent(MsonEvent.item(item)); return this; }
	
	// -------------------------------------------- //
	// CONVENIENCE STYLE
	// -------------------------------------------- //
	
	public Mson style(ChatColor... styles)
	{
		for (ChatColor style : styles)
		{
			this.style(style);
		}
		return this;
	}
	
	public Mson style(ChatColor style)
	{
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
		this.color = ChatColor.WHITE;
		this.bold = false;
		this.italic = false;
		this.underlined = false;
		this.strikethrough = false;
		this.obfuscated = false;
		return this;
	}
	
	// -------------------------------------------- //
	// BUILD TREE
	// -------------------------------------------- //

	// Child, called on parent or root
	public Mson addChild(Object child)
	{
		if (child == null) throw new NullPointerException("child");

		Mson mson = Mson.mson(child);
		List<Mson> extra = this.extraCreative();

		mson.parent(this);
		extra.add(mson);

		return mson; // Return child
	}

	public Mson addChildren(Object... children)
	{
		if (children == null) throw new NullPointerException("children");

		for (Object part : children)
		{
			this.addChild(part);
		}

		return this; // Return this
	}

	// Sibling, normally called on child
	public Mson addSibling(Object sibling)
	{
		if (sibling == null) throw new NullPointerException("sibling");
		Mson parent = this.parentCreative();
		return parent.addChild(sibling); // Return sibling
	}

	public Mson addSiblings(Object... siblings)
	{
		if (siblings == null) throw new NullPointerException("siblings");
		Mson parent = this.parentCreative();

		if ( ! parent.equals(this.parent())) this.parent(parent);
		parent.addChildren(siblings);
		return this; // Return this
	}

	// -------------------------------------------- //
	// SIMPLE CLEAN
	// -------------------------------------------- //
	// These methods, exist to clean up after mistakes.
	// So they are very forgiving.

	public boolean isEmpty()
	{
		// It has text, not empty.
		if (this.textHas()) return false;

		if (this.extraHas())
		{
			for (Mson extra : this.extra())
			{
				// It is null. So kinda empty.
				if (extra == null) continue;
				
				// It is empty
				if (extra.isEmpty()) continue;
				
				// It was not empty.
				return false;
			}
		}

		// We are empty.
		return true;
	}

	// Detaches uneccessary extras.
	public Mson simpleClean()
	{	
		if ( ! this.extraHas()) return this;
		if (this.extra().isEmpty())
		{
			this.extra(null);
			return this;
		}
		
		for (ListIterator<Mson> it = this.extra().listIterator(); it.hasNext();)
		{
			Mson extra = it.next();
			if (extra == null)
			{
				it.remove();
				continue;
			}

			if (extra.isEmpty())
			{
				extra.parent(null);
				it.remove();
				continue;
			}
			
			extra.simpleClean();
		}
		return this;
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Mson()
	{
		
	}
	
	public static Mson mson()
	{
		return new Mson();
	}
	
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
			return mson().text(text);
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
			
			Mson mson = new Mson()
			.text(text)
			.color(latestColor)
			.bold(bold)
			.italic(italic)
			.underlined(underlined)
			.strikethrough(strikethrough)
			.obfuscated(obfuscated);

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
		return Mson.mson().text(String.format(format, args));
	}

	public Mson copy()
	{
		Mson copy = new Mson()
		.text(this.text)
		.color(this.color)
		.bold(this.bold)
		.italic(this.italic)
		.underlined(this.underlined)
		.strikethrough(this.strikethrough)
		.obfuscated(this.obfuscated)
		.insertionString(this.insertionString)
		.clickEvent(this.clickEvent)
		.hoverEvent(this.hoverEvent);

		if (this.extraHas())
		{
			List<Mson> extras = new MassiveList<Mson>(this.extra.size());
			for (Mson extra : this.extra)
			{
				Mson extraCopy = extra.copy();
				extraCopy.parent(copy);
				extras.add(extraCopy);
			}
			copy.extra(extras);
		}

		return copy;
	}

	private Mson copyFormatAndBehaviour(Mson ancestor)
	{
		if (ancestor == null) throw new NullPointerException("ancestor");
		
		this.color(ancestor.color());
		this.bold(ancestor.bold());
		this.italic(ancestor.italic());
		this.underlined(ancestor.underlined());
		this.strikethrough(ancestor.strikethrough());
		this.obfuscated(ancestor.obfuscated());
		this.hoverEvent(ancestor.hoverEvent());
		this.clickEvent(ancestor.clickEvent());
		this.insertionString(ancestor.insertionString());

		return this;
	}

	// -------------------------------------------- //
	// STRING LIKE METHODS
	// -------------------------------------------- //

	// Split
	public List<Mson> split(String regex)
	{
		return this.split(regex, 0);
	}

	public List<Mson> split(String regex, int limit)
	{
		return split(Pattern.compile(regex), limit);
	}
	
	public List<Mson> split(Pattern pattern)
	{
		return this.split(pattern, 0);
	}

	public List<Mson> split(Pattern pattern, int limit)
	{
		if (pattern == null) throw new NullPointerException("pattern");

		boolean limited = (limit == 0 ? false : true);
		List<Mson> msons = new MassiveList<Mson>();

		String[] splited = pattern.split(this.text(), limit);
		if (splited.length == 1) return new MassiveList<Mson>(this);

		for (String string : splited)
		{
			if (string.isEmpty()) continue;
			Mson part = new Mson().text(string);
			part.copyFormatAndBehaviour(this);
			msons.add(part);
		}

		int size = msons.size();

		for (Mson part : this.extraCreative())
		{
			if (limited)
			{
				limit -= size;
				if (limit <= 0) break;
			}

			List<Mson> innerMsons = part.split(pattern, limit);
			msons.addAll(innerMsons);
			size = innerMsons.size();
		}

		return msons;
	}

	// Replace
	public Mson replace(char oldChar, char newChar)
	{
		this.text(this.text().replace(oldChar, newChar));

		if (this.extraHas())
		{
			for (Mson part : this.extra())
			{
				part.replace(oldChar, newChar);
			}
		}

		return this;
	}

	public Mson replace(CharSequence replace, CharSequence replacement)
	{
		if (replace == null) throw new NullPointerException("replace");
		if (replacement == null) throw new NullPointerException("replacement");

		this.text(this.text().replace(replace, replacement));

		if (this.extraHas())
		{
			for (Mson part : this.extra())
			{
				part.replace(replace, replacement);
			}
		}

		return this;
	}
	
	public Mson replaceAll(String regex, String replacement)
	{
		if (regex == null) throw new NullPointerException("regex");
		if (replacement == null) throw new NullPointerException("replacement");

		this.text(this.text().replaceAll(regex, replacement));
		
		if (this.extraHas())
		{
			for (Mson part : this.extra())
			{
				part.replaceAll(regex, replacement);
			}
		}

		return this;
	}

	// Special replace all
	
	// This is old and not the best solution.
	// Awaiting further decision.
	
/*	public Mson replaceAll(String regex, Mson replacement)
	{
		if (regex == null) throw new NullPointerException("regex");
		if (replacement == null) throw new NullPointerException("replacement");
		return this.replaceAllOld(Pattern.compile(regex), replacement);
	}
	
	
	public Mson replaceAll(Pattern pattern, Mson replacement)
	{
		if (pattern == null) throw new NullPointerException("pattern");
		if (replacement == null) throw new NullPointerException("replacement");

		// We don't want the same object to eventually be part of itself
		Mson repCopy = replacement.copy();
		String text = (this.text() + " "); // Prepare text

		// Split the string of this msons text and create an iterator
		// and create the list of mson with the replacements in between ...
		List<Mson> msons = new MassiveList<Mson>();

		for (ListIterator<String> it = Arrays.asList(pattern.split(text)).listIterator(); it.hasNext();)
		{
			String string = it.next();

			// string might be empty, we don't want these empty msons
			if ( ! string.isEmpty())
			{
				Mson part = Mson.mson(string);
				msons.add(part);

				// Delete security spacing at the last string
				if ( ! it.hasNext()) part.text(string.substring(0, string.length() - 1));
			}

			// In between every part, add in replacement
			if (it.hasNext()) msons.add(repCopy);
		}

		// ... and forge back together, unless the whole text is "replaced", then set it to be the replacement itself.
		Mson mson;
		if ( ! msons.isEmpty())
		{
			mson = msons.get(0).copy();
			msons.remove(0);
			mson.copyFormatAndBehaviour(this);
			if ( ! msons.isEmpty()) mson.extra(msons);
		}
		else
		{
			mson = repCopy;
		}

		mson.parent(this.parent());

		// If there is no extra, return here...
		List<Mson> extra = this.extra();
		if (extra == null) return mson;

		// ... otherwise iterate over the extra and modify it.
		for (ListIterator<Mson> it = extra.listIterator(); it.hasNext();)
		{
			Mson part = it.next();
			int index = extra.indexOf(part);

			part = part.replaceAll(pattern, replacement);
			part.parent(mson);

			extra.set(index, part);
		}

		// If the current mson has any extra ...
		List<Mson> extras = mson.extra();
		if( extras != null)
		{
			// ... apply tree structure again ...
			extras.get(extras.size() - 1).extra(extra);
		}
		else
		{
			// ... set the extra directly ...
			mson.extra(extra);
		}
		
		mson.simpleClean();
		// ... and return recreated mson
		return mson;
	}*/
	
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
			public Object getReplacement(String match)
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
		
		Mson ret = mson().copyFormatAndBehaviour(this);
		
		List<Mson> msons = new ArrayList<Mson>();
		StringBuffer currentString = new StringBuffer();
		Matcher matcher = pattern.matcher(text());
		while (matcher.find())
		{
			String match = matcher.group(0);
			Object replacement = replacer.getReplacement(match);
			
			// Add the match
			if (replacement == null) matcher.appendReplacement(currentString, match);
			
			// Add the string
			else if (replacement instanceof String) matcher.appendReplacement(currentString, replacement.toString());
			
			// Add the mson
			else if (replacement instanceof Mson)
			{
				// Fixup current string
				matcher.appendReplacement(currentString, "");
				if (addStringBuffer(msons, currentString)) currentString = new StringBuffer();
				
				// Add this replacement
				msons.add((Mson) replacement);
			}
			
			// Not allowed
			else throw new IllegalArgumentException("We only support null, String and Mson.");
		}
		
		// Add the remaining string pieces
		matcher.appendTail(currentString);
		addStringBuffer(msons, currentString);
		
		// Recurse on extras.
		if (this.extraHas())
		{
			for (Mson extra : this.extra())
			{
				msons.add(extra.replaceAll(pattern, replacer));
			}
		}
		
		// Set extras
		for (Mson mson : msons)
		{
			mson.parent(ret);
		}
		ret.extra(msons);
		
		return ret;
	}
	
	private static boolean addStringBuffer(List<Mson> msons, StringBuffer buffer)
	{
		if (buffer.length() == 0) return false;
		Mson mson = mson().text(buffer.toString());
		msons.add(mson);
		return true;
	}

	public Mson replaceAll(Mson replace, Mson replacement)
	{
		if (replace == null) throw new NullPointerException("replace");
		if (replacement == null) throw new NullPointerException("replacement");

		// We don't want the same object to eventually be part of itself
		Mson repCopy = replacement.copy();

		Mson mson = this;
		List<Mson> extra = this.extra();

		if (mson.equals(replace))
		{
			mson = repCopy;
			mson.parent(this.parent());
			if (extra != null) mson.extraCreative().addAll(extra);
		}

		if (extra == null) return mson;

		for (ListIterator<Mson> it = extra.listIterator(); it.hasNext();)
		{
			int index = it.nextIndex();
			Mson part = it.next();

			part = part.replaceAll(replace, replacement);
			part.parent(mson);

			extra.set(index, part);
		}

		mson.extra(extra);
		return mson;
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
	// TO JSON, RAW, PLAIN  & STRING
	// -------------------------------------------- //
	
	public JsonElement rootToJson() { return this.root().toJson(); }
	public JsonElement toJson()
	{
		return GSON.toJsonTree(this);
	}
	
	public String rootToRaw() { return this.root().toRaw(); }
	public String toRaw()
	{
		return this.toJson().toString();
	}

	public String toPlain()
	{
		StringBuilder ret = new StringBuilder();

		ret.append(toPlainSimple(this));

		if (this.extraHas())
		{
			for (Mson part : this.extra())
			{
				ret.append(ChatColor.RESET);
				ret.append(part.toPlain());
			}
		}

		return ret.toString();
	}

	// Turns a single mson (without it's children) into plain text.
	protected static String toPlainSimple(Mson mson)
	{
		if (mson.textHas())
		{
			// Color must be put in BEFORE formatting.
			// http://minecraft.gamepedia.com/Formatting_codes#Formatting_codes

			StringBuilder ret = new StringBuilder(mson.text().length());
			if (mson.colorEffective() != null) ret.append(mson.colorEffective());
			if (mson.boldEffective()) ret.append(ChatColor.BOLD);
			if (mson.italicEffective()) ret.append(ChatColor.ITALIC);
			if (mson.underlinedEffective()) ret.append(ChatColor.UNDERLINE);
			if (mson.strikethroughEffective()) ret.append(ChatColor.STRIKETHROUGH);
			if (mson.obfuscatedEffective()) ret.append(ChatColor.MAGIC);
			ret.append(mson.text());

			return ret.toString();
		}

		return "";
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
		if (  ! (obj instanceof Mson)) return false;
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
