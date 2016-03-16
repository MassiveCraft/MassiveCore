package com.massivecraft.massivecore.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateStartsWithIgnoreCase;

import static com.massivecraft.massivecore.mson.Mson.mson;

public class Txt
{
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static final int PAGEHEIGHT_PLAYER = 9;
	public static final int PAGEHEIGHT_CONSOLE = 50;
	
	public static final Map<String, String> parseReplacements;
	public static final Pattern parsePattern;
	
	public static final Pattern PATTERN_WHITESPACE = Pattern.compile("\\s+");
	public static final Pattern PATTERN_NEWLINE = Pattern.compile("\\r?\\n");
	
	public static final long millisPerSecond = 1000;
	public static final long millisPerMinute = 60 * millisPerSecond;
	public static final long millisPerHour = 60 * millisPerMinute;
	public static final long millisPerDay = 24 * millisPerHour;
	public static final long millisPerWeek = 7 * millisPerDay;
	public static final long millisPerMonth = 31 * millisPerDay;
	public static final long millisPerYear = 365 * millisPerDay;
	
	public static final Set<String> vowel = MUtil.set(
		"A", "E", "I", "O", "U", "Y", "Å", "Ä", "Ö", "Æ", "Ø",
		"a", "e", "i", "o", "u", "y", "å", "ä", "ö", "æ", "ø"
	); 
	
	public static final Map<String, Long> unitMillis = MUtil.map(
		"years", millisPerYear,
		"months", millisPerMonth,
		"weeks", millisPerWeek,
		"days", millisPerDay,
		"hours", millisPerHour,
		"minutes", millisPerMinute,
		"seconds", millisPerSecond
	);
	
	static
	{
		// Create the parce replacements map
		parseReplacements = new HashMap<String, String>();
		
		// Color by name
		parseReplacements.put("<empty>", "");
		parseReplacements.put("<black>", "\u00A70");
		parseReplacements.put("<navy>", "\u00A71");
		parseReplacements.put("<green>", "\u00A72");
		parseReplacements.put("<teal>", "\u00A73");
		parseReplacements.put("<red>", "\u00A74");
		parseReplacements.put("<purple>", "\u00A75");
		parseReplacements.put("<gold>", "\u00A76");
		parseReplacements.put("<orange>", "\u00A76");
		parseReplacements.put("<silver>", "\u00A77");
		parseReplacements.put("<gray>", "\u00A78");
		parseReplacements.put("<grey>", "\u00A78");
		parseReplacements.put("<blue>", "\u00A79");
		parseReplacements.put("<lime>", "\u00A7a");
		parseReplacements.put("<aqua>", "\u00A7b");
		parseReplacements.put("<rose>", "\u00A7c");
		parseReplacements.put("<pink>", "\u00A7d");
		parseReplacements.put("<yellow>", "\u00A7e");
		parseReplacements.put("<white>", "\u00A7f");
		parseReplacements.put("<magic>", "\u00A7k");
		parseReplacements.put("<bold>", "\u00A7l");
		parseReplacements.put("<strong>", "\u00A7l");
		parseReplacements.put("<strike>", "\u00A7m");
		parseReplacements.put("<strikethrough>", "\u00A7m");
		parseReplacements.put("<under>", "\u00A7n");
		parseReplacements.put("<underline>", "\u00A7n");
		parseReplacements.put("<italic>", "\u00A7o");
		parseReplacements.put("<em>", "\u00A7o");
		parseReplacements.put("<reset>", "\u00A7r");
		
		// Color by semantic functionality
		parseReplacements.put("<l>", "\u00A72");
		parseReplacements.put("<logo>", "\u00A72");
		parseReplacements.put("<a>", "\u00A76");
		parseReplacements.put("<art>", "\u00A76");
		parseReplacements.put("<n>", "\u00A77");
		parseReplacements.put("<notice>", "\u00A77");
		parseReplacements.put("<i>", "\u00A7e");
		parseReplacements.put("<info>", "\u00A7e");
		parseReplacements.put("<g>", "\u00A7a");
		parseReplacements.put("<good>", "\u00A7a");
		parseReplacements.put("<b>", "\u00A7c");
		parseReplacements.put("<bad>", "\u00A7c");
		
		parseReplacements.put("<k>", "\u00A7b");
		parseReplacements.put("<key>", "\u00A7b");
		
		parseReplacements.put("<v>", "\u00A7d");
		parseReplacements.put("<value>", "\u00A7d");
		parseReplacements.put("<h>", "\u00A7d");
		parseReplacements.put("<highlight>", "\u00A7d");
		
		parseReplacements.put("<c>", "\u00A7b");
		parseReplacements.put("<command>", "\u00A7b");
		parseReplacements.put("<p>", "\u00A73");
		parseReplacements.put("<parameter>", "\u00A73");
		parseReplacements.put("&&", "&");
		parseReplacements.put("§§", "§");
		
		// Color by number/char
		for (int i = 48; i <= 122; i++)
		{
			char c = (char)i;
			parseReplacements.put("§"+c, "\u00A7"+c);
			parseReplacements.put("&"+c, "\u00A7"+c);
			if (i == 57) i = 96;
		}
		
		// Build the parse pattern and compile it
		StringBuilder patternStringBuilder = new StringBuilder();
		for (String find : parseReplacements.keySet())
		{
			patternStringBuilder.append('(');
			patternStringBuilder.append(Pattern.quote(find));
			patternStringBuilder.append(")|");
		}
		String patternString = patternStringBuilder.toString();
		patternString = patternString.substring(0, patternString.length()-1); // Remove the last |
		parsePattern = Pattern.compile(patternString);
	}
	
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN)
	// -------------------------------------------- //
	
	private Txt()
	{
		
	}
	
	// -------------------------------------------- //
	// PARSE
	// -------------------------------------------- //
	
	public static String parse(String string)
	{
		if (string == null) return null;
		StringBuffer ret = new StringBuffer();
		Matcher matcher = parsePattern.matcher(string);
		while (matcher.find())
		{
			matcher.appendReplacement(ret, parseReplacements.get(matcher.group(0)));
		}
		matcher.appendTail(ret);
		return ret.toString();
	}
	
	public static String parse(String string, Object... args)
	{
		return String.format(parse(string), args);
	}
	
	public static ArrayList<String> parse(Collection<String> strings)
	{
		ArrayList<String> ret = new ArrayList<String>(strings.size());
		for (String string : strings)
		{
			ret.add(parse(string));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// SPLIT AT LINEBREAKS
	// -------------------------------------------- //
	
	public static ArrayList<String> wrap(final String string)
	{
		if (string == null) return null;
		return new ArrayList<String>(Arrays.asList(PATTERN_NEWLINE.split(string)));
	}
	
	public static ArrayList<String> wrap(final Collection<String> strings)
	{
		ArrayList<String> ret = new ArrayList<String>();
		for (String string : strings)
		{
			ret.addAll(wrap(string));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// Parse and Wrap combo
	// -------------------------------------------- //
	
	public static ArrayList<String> parseWrap(final String string)
	{
		return wrap(parse(string));
	}
	
	public static ArrayList<String> parseWrap(final Collection<String> strings)
	{
		return wrap(parse(strings));
	}
	
	// -------------------------------------------- //
	// Standard utils like UCFirst, implode and repeat.
	// -------------------------------------------- //
	
	public static String upperCaseFirst(String string)
	{
		if (string == null) return null;
		if (string.length() == 0) return string;
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	public static String lowerCaseFirst(String string)
	{
		if (string == null) return null;
		if (string.length() == 0) return string;
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}
	
	public static String repeat(String string, int times)
	{
		// Create Ret
		StringBuilder ret = new StringBuilder(times);
		
		// Fill Ret
		for (int i = 0; i < times; i++)
		{
			ret.append(string);
		}
		
		// Return Ret
		return ret.toString();
	}
	
	public static String implode(final Object[] list, final String glue, final String format)
	{
		StringBuilder ret = new StringBuilder();
		for (int i=0; i<list.length; i++)
		{
			Object item = list[i];
			String str = (item == null ? "NULL" : item.toString());
			
			if (i!=0)
			{
				ret.append(glue);
			}
			if (format != null)
			{
				ret.append(String.format(format, str));
			}
			else
			{
				ret.append(str);
			}
		}
		return ret.toString();
	}
	public static String implode(final Object[] list, final String glue)
	{
		return implode(list, glue, null);
	}
	public static String implode(final Collection<? extends Object> coll, final String glue, final String format)
	{
		return implode(coll.toArray(new Object[0]), glue, format);
	}
	public static String implode(final Collection<? extends Object> coll, final String glue)
	{
		return implode(coll, glue, null);
	}
	
	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String format, final String comma, final String and, final String dot)
	{
		if (objects.size() == 0) return "";
		if (objects.size() == 1)
		{
			return implode(objects, comma, format);
		}
		
		List<Object> ourObjects = new ArrayList<Object>(objects);
		
		String lastItem = ourObjects.get(ourObjects.size()-1).toString();
		String nextToLastItem = ourObjects.get(ourObjects.size()-2).toString();
		if (format != null)
		{
			lastItem = String.format(format, lastItem);
			nextToLastItem = String.format(format, nextToLastItem);
		}
		String merge = nextToLastItem+and+lastItem;
		ourObjects.set(ourObjects.size()-2, merge);
		ourObjects.remove(ourObjects.size()-1);
		
		return implode(ourObjects, comma, format)+dot;
	}
	
	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String comma, final String and, final String dot)
	{
		return implodeCommaAndDot(objects, null, comma, and, dot);
	}
	
	public static String implodeCommaAnd(final Collection<? extends Object> objects, final String comma, final String and)
	{
		return implodeCommaAndDot(objects, comma, and, "");
	}
	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String color)
	{
		return implodeCommaAndDot(objects, color+", ", color+" and ", color+".");
	}
	public static String implodeCommaAnd(final Collection<? extends Object> objects, final String color)
	{
		return implodeCommaAndDot(objects, color+", ", color+" and ", "");
	}
	public static String implodeCommaAndDot(final Collection<? extends Object> objects)
	{
		return implodeCommaAndDot(objects, "");
	}
	public static String implodeCommaAnd(final Collection<? extends Object> objects)
	{
		return implodeCommaAnd(objects, "");
	}
	
	public static Integer indexOfFirstDigit(final String str)
	{
		Integer ret = null;
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			boolean isDigit = (c >= '0' && c <= '9');
			if (isDigit)
			{
				ret = i;
				break;
			}
		}
		return ret;
	}
	
	public static String removeLeadingCommandDust(String string)
	{
		return string.replaceAll("^[/\\s]+", "");
	}
	
	public static Entry<String, String> divideOnFirstSpace(String string)
	{
		String[] parts = string.split("\\s+", 2);
		String first = parts[0];
		String second = null;
		if (parts.length > 1)
		{
			second = parts[1];
		}
		return new SimpleEntry<String, String>(first, second);
	}
	
	public static boolean isVowel(String str)
	{
		if (str == null || str.length() == 0) return false;
		return vowel.contains(str.substring(0, 1));
	}
	
	public static String aan(String noun)
	{
		return isVowel(noun) ? "an" : "a";
	}
	
	// -------------------------------------------- //
	// START COLORS
	// -------------------------------------------- //
	// This method never returns null
	
	public static final String START_COLORS_REGEX = "^((?:§.)+).*$";
	public static final Pattern START_COLORS_PATTERN = Pattern.compile(START_COLORS_REGEX);
	
	public static String getStartColors(String string)
	{
		Matcher matcher = START_COLORS_PATTERN.matcher(string);
		if (!matcher.find()) return "";
		return matcher.group(1);
	}
	
	// -------------------------------------------- //
	// Material name tools
	// -------------------------------------------- //
	
	protected static Pattern PATTERN_ENUM_SPLIT = Pattern.compile("[\\s_]+");
	public static String getNicedEnumString(String str)
	{
		List<String> parts = new ArrayList<String>();  
		for (String part : PATTERN_ENUM_SPLIT.split(str.toLowerCase()))
		{
			parts.add(upperCaseFirst(part));
		}
		return implode(parts, "");
	}
	
	public static <T extends Enum<T>> String getNicedEnum(T enumObject)
	{
		return getNicedEnumString(enumObject.name());
	}
	
	public static String getMaterialName(Material material)
	{
		return getNicedEnum(material);
	}
	
	public static String getItemName(ItemStack itemStack)
	{
		if (InventoryUtil.isNothing(itemStack)) return Txt.parse("<silver><em>Nothing");
		
		ChatColor color = (itemStack.getEnchantments().size() > 0) ? ChatColor.AQUA : ChatColor.WHITE;
		
		if (itemStack.hasItemMeta())
		{
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (itemMeta.hasDisplayName())
			{
				return color.toString() + ChatColor.ITALIC.toString() + itemMeta.getDisplayName();
			}
		}
		
		return color + Txt.getMaterialName(itemStack.getType());
	}
	
	public static Mson createItemMson(ItemStack item)
	{
		String name = Txt.getItemName(item);
		String colors = Txt.getStartColors(name);
		name = colors + "[" + ChatColor.stripColor(name) + "]";
		
		Mson ret = Mson.fromParsedMessage(name);
		
		if (InventoryUtil.isSomething(item)) ret = ret.item(item);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// Paging and chrome-tools like titleize
	// -------------------------------------------- //
	
	private final static String titleizeLine = repeat("_", 52);
	private final static int titleizeBalance = -1;
	public static Mson titleize(Object obj)
	{
		Mson title = mson(obj);
		if (title.getColor() == null) title = title.color(ChatColor.DARK_GREEN);
		
		Mson center = mson(
			mson(".[ ").color(ChatColor.GOLD),
			title,
			mson(" ].").color(ChatColor.GOLD)
		);
		
		int centerlen = center.length();
		int pivot = titleizeLine.length() / 2;
		int eatLeft = (centerlen / 2) - titleizeBalance;
		int eatRight = (centerlen - eatLeft) + titleizeBalance;

		if (eatLeft < pivot)
			return mson(
				mson(titleizeLine.substring(0, pivot - eatLeft)).color(ChatColor.GOLD),
				center,
				mson(titleizeLine.substring(pivot + eatRight)).color(ChatColor.GOLD)
			);
		else
			return center;
	}
	
	public static Mson getMessageEmpty()
	{
		return mson("Sorry, no pages available.").color(ChatColor.YELLOW);
	}
	
	public static Mson getMessageInvalid(int size)
	{
		if (size == 0)
		{
			return getMessageEmpty();
		}
		else if (size == 1)
		{
			return mson("Invalid, there is only one page.").color(ChatColor.RED);
		}
		else
		{
			return Mson.format("Invalid, page must be between 1 and %d.", size).color(ChatColor.RED);
		}
	}
	
	public static Mson titleizeMson(Object obj, int pagecount, int pageHumanBased, MassiveCommand command, List<String> args)
	{
		if (command == null)
		{
			return titleize(mson(
				obj,
				Mson.SPACE,
				mson(pageHumanBased + "/" + pagecount).color(ChatColor.GOLD)
			));
		}
		
		// Math
		Mson title = mson(obj, Mson.SPACE, "[<]", String.valueOf(pageHumanBased), "/", String.valueOf(pagecount), "[>]");
		int centerlen = ".[ ".length() + title.length() + " ].".length();
		int pivot = titleizeLine.length() / 2;
		int eatLeft = (centerlen / 2) - titleizeBalance;
		int eatRight = (centerlen - eatLeft) + titleizeBalance;

		// Mson
		Mson centerMson = mson(
			mson(".[ ").color(ChatColor.GOLD),
			mson(obj, Mson.SPACE).color(ChatColor.DARK_GREEN),
			getFlipSection(pagecount, pageHumanBased, args, command),
			mson(" ].").color(ChatColor.GOLD)
		);

		if (eatLeft < pivot)
		{
			Mson ret = mson(
				mson(titleizeLine.substring(0, pivot - eatLeft)).color(ChatColor.GOLD),
				centerMson,
				mson(titleizeLine.substring(pivot + eatRight)).color(ChatColor.GOLD)
			);

			return ret;
		}
		else
		{
			return centerMson;
		}
	}
	
	public static List<Mson> getPage(List<?> lines, int pageHumanBased, Object title)
	{
		return getPage(lines, pageHumanBased, title, null, null, null);
	}
	
	public static List<Mson> getPage(List<?> lines, int pageHumanBased, Object title, CommandSender sender)
	{
		return getPage(lines, pageHumanBased, title, sender, null, null);
	}
	
	public static List<Mson> getPage(List<?> lines, int pageHumanBased, Object title, MassiveCommand command)
	{
		return getPage(lines, pageHumanBased, title, command, command.getArgs());
	}
	
	public static List<Mson> getPage(List<?> lines, int pageHumanBased, Object title, MassiveCommand command, List<String> args)
	{
		return getPage(lines, pageHumanBased, title, command.sender, command, args);
	}
	
	public static List<Mson> getPage(List<?> lines, int pageHumanBased, Object title, CommandSender sender, MassiveCommand command, List<String> args)
	{
		return getPage(lines, pageHumanBased, title, (sender == null || sender instanceof Player) ? Txt.PAGEHEIGHT_PLAYER : Txt.PAGEHEIGHT_CONSOLE, command, args);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Mson> getPage(List<?> lines, int pageHumanBased, Object title, int pageheight, MassiveCommand command, List<String> args)
	{
		// Create Ret
		List<Mson> ret = new MassiveList<>();
		int pageZeroBased = pageHumanBased - 1;
		int pagecount = (int)Math.ceil(((double)lines.size()) / pageheight);
		
		// Add Title
		Mson msonTitle = Txt.titleizeMson(title, pagecount, pageHumanBased, command, args);
		ret.add(msonTitle);
		
		// Check empty and invalid
		if (pagecount == 0)
		{
			ret.add(getMessageEmpty());
			return ret;
		}
		else if (pageZeroBased < 0 || pageHumanBased > pagecount)
		{
			ret.add(getMessageInvalid(pagecount));
			return ret;
		}
		
		// Get Lines
		int from = pageZeroBased * pageheight;
		int to = from + pageheight;
		if (to > lines.size())
		{
			to = lines.size();
		}
		
		// Check object type and add lines
		Object first = lines.get(0);
		
		if (first instanceof String)
		{
			for (String line : (List<String>) lines.subList(from, to))
			{
				ret.add(Mson.fromParsedMessage(line));
			}
		}
		else if (first instanceof Mson)
		{
			ret.addAll((List<Mson>) lines.subList(from, to));
		}
		else
		{
			throw new IllegalArgumentException("The lines must be either String or Mson.");
		}
		
		// Return Ret
		return ret;
	}
	
	private static Mson getFlipSection(int pagecount, int pageHumanBased, List<String> args, MassiveCommand command)
	{
		// Construct Mson
		Mson start = mson(String.valueOf(pageHumanBased)).color(ChatColor.GOLD);
		Mson backward = mson("[<] ").color(ChatColor.GRAY);
		Mson forward = mson(" [>]").color(ChatColor.GRAY);
		Mson end = mson(String.valueOf(pagecount)).color(ChatColor.GOLD);

		// Set flip page backward commands
		if (pageHumanBased > 1)
		{
			start = setFlipPageCommand(start, pageHumanBased, 1, args, command);
			backward = setFlipPageCommand(backward, pageHumanBased, pageHumanBased - 1, args, command).color(ChatColor.AQUA);
		}

		// Set flip page forward commands
		if (pagecount > pageHumanBased)
		{
			forward = setFlipPageCommand(forward, pageHumanBased, pageHumanBased + 1, args, command).color(ChatColor.AQUA);
			end = setFlipPageCommand(end, pageHumanBased, pagecount, args, command);
		}
		
		Mson flipMson = mson(
			backward,
			start,
			mson("/").color(ChatColor.GOLD),
			end,
			forward
		);
		
		return flipMson;
	}
	
	private static Mson setFlipPageCommand(Mson mson, int pageHumanBased, int destinationPage, List<String> args, MassiveCommand command)
	{
		String number = String.valueOf(destinationPage);
		String oldNumber = String.valueOf(pageHumanBased);
		String commandLine;

		if (args != null && args.contains(oldNumber))
		{
			List<String> arguments = new ArrayList<String>(args);
			arguments.set(arguments.indexOf(oldNumber), number);

			commandLine = command.getCommandLine(arguments);
		}
		else
		{
			commandLine = command.getCommandLine(number);
		}

		return mson.command(commandLine);
	}
	
	// -------------------------------------------- //
	// Describing Time
	// -------------------------------------------- //
	
	public static String getTimeDeltaDescriptionRelNow(long millis)
	{
		String ret = "";
		
		double millisLeft = (double) Math.abs(millis);
		
		List<String> unitCountParts = new ArrayList<String>();
		for (Entry<String, Long> entry : unitMillis.entrySet())
		{
			if (unitCountParts.size() == 3 ) break;
			String unitName = entry.getKey();
			long unitSize = entry.getValue();
			long unitCount = (long) Math.floor(millisLeft / unitSize);
			if (unitCount < 1) continue;
			millisLeft -= unitSize*unitCount;
			unitCountParts.add(unitCount+" "+unitName);
		}
		
		if (unitCountParts.size() == 0) return "just now";
		
		ret += implodeCommaAnd(unitCountParts);
		ret += " ";
		if (millis <= 0)
		{
			ret += "ago";
		}
		else
		{
			ret += "from now";
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// FORMATTING CANDY
	// -------------------------------------------- //
	
	public static String parenthesize(String string)
	{
		return Txt.parse("<silver>(%s<silver>)", string);
	}
	
	// -------------------------------------------- //
	// "SMART" QUOTES
	// -------------------------------------------- //
	// The quite stupid "Smart quotes" design idea means replacing normal characters with mutated UTF-8 alternatives.
	// The normal characters look good in Minecraft.
	// The UFT-8 "smart" alternatives look quite bad.
	// http://www.fileformat.info/info/unicode/block/general_punctuation/list.htm
	
	public static String removeSmartQuotes(String string)
	{
		if (string == null) return null;
		
		// LEFT SINGLE QUOTATION MARK
		string = string.replace("\u2018", "'");
		
		// RIGHT SINGLE QUOTATION MARK
		string = string.replace("\u2019", "'");
		
		// LEFT DOUBLE QUOTATION MARK
		string = string.replace("\u201C", "\"");
		
		// RIGHT DOUBLE QUOTATION MARK
		string = string.replace("\u201D", "\"");
		
		// ONE DOT LEADER
		string = string.replace("\u2024", ".");
		
		// TWO DOT LEADER
		string = string.replace("\u2025", "..");
		
		// HORIZONTAL ELLIPSIS
		string = string.replace("\u2026", "...");

		return string;
	}
	
	// -------------------------------------------- //
	// String comparison
	// -------------------------------------------- //
	
	public static String getBestCIStart(Collection<String> candidates, String start)
	{
		String ret = null;
		int best = 0;
		
		start = start.toLowerCase();
		int minlength = start.length();
		for (String candidate : candidates)
		{
			if (candidate.length() < minlength) continue;
			if ( ! candidate.toLowerCase().startsWith(start)) continue;
			
			// The closer to zero the better
			int lendiff = candidate.length() - minlength;
			if (lendiff == 0)
			{
				return candidate;
			}
			if (lendiff < best || best == 0)
			{
				best = lendiff;
				ret = candidate;
			}
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// FILTER
	// -------------------------------------------- //
	
	public static <T> List<T> getFiltered(Iterable<T> elements, Predicate<T> predicate)
	{
		// Create Ret
		List<T> ret = new ArrayList<T>();
		
		// Fill Ret
		for (T element : elements)
		{
			if ( ! predicate.apply(element)) continue;
			ret.add(element);
		}
		
		// Return Ret
		return ret;
	}
	
	public static <T> List<T> getFiltered(T[] elements, Predicate<T> predicate)
	{
		return getFiltered(Arrays.asList(elements), predicate);
	}
	
	public static List<String> getStartsWithIgnoreCase(Iterable<String> elements, String prefix)
	{
		return getFiltered(elements, PredicateStartsWithIgnoreCase.get(prefix));
	}
	
	public static List<String> getStartsWithIgnoreCase(String[] elements, String prefix)
	{
		return getStartsWithIgnoreCase(Arrays.asList(elements), prefix);
	}
	
	// -------------------------------------------- //
	// Tokenization
	// -------------------------------------------- //
	
	public static List<String> tokenizeArguments(String str)
	{
		List<String> ret = new ArrayList<String>();
		StringBuilder token = null;
		boolean escaping = false;
		boolean citing = false;
		
		for(int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (token == null)
			{
				 token = new StringBuilder();
			}
			
			if (escaping)
			{
				escaping = false;
				token.append(c);
			}
			else if (c == '\\')
			{
				escaping = true;
			}
			else if (c == '"')
			{
				if (citing || token.length() > 0)
				{
					ret.add(token.toString());
					token = null;
				}
				citing = !citing;
			}
			else if (citing == false && c == ' ')
			{
				if (token.length() > 0)
				{
					ret.add(token.toString());
					token = null;
				}
			}
			else
			{
				token.append(c);
			}
		}
		
		if (token != null)
		{
			ret.add(token.toString());
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// PREPONDFIX
	// -------------------------------------------- //
	// This weird algorithm takes:
	// - A prefix
	// - A centerpiece single string or a list of strings.
	// - A suffix
	// If the centerpiece is a single String it just concatenates prefix + centerpiece + suffix.
	// If the centerpiece is multiple Strings it concatenates prefix + suffix and then appends the centerpice at the end.
	// This algorithm is used in the editor system.
	
	public static List<String> prepondfix(String prefix, List<String> strings, String suffix)
	{
		// Create
		List<String> ret = new MassiveList<String>();
		
		// Fill
		List<String> parts = new MassiveList<String>();
		if (prefix != null) parts.add(prefix);
		if (strings.size() == 1) parts.add(strings.get(0));
		if (suffix != null) parts.add(suffix);
		
		ret.add(Txt.implode(parts, " "));
		
		if (strings.size() != 1)
		{
			ret.addAll(strings);
		}
		
		// Return
		return ret;
	}
	
	public static String prepondfix(String prefix, String string, String suffix)
	{
		List<String> strings = Arrays.asList(PATTERN_NEWLINE.split(string));
		List<String> ret = prepondfix(prefix, strings, suffix);
		return implode(ret, "\n");
	}
	
}
