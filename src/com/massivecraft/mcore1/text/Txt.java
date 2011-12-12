package com.massivecraft.mcore1.text;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.TextWrapper;

public class Txt
{
	// Global Default Design TODO: Assign it somehow!
	private static TxtDesign defaultDesign = new TxtDesign();
	public static TxtDesign getDefaultDesign() { return defaultDesign; }
	public static void setDefaultDesign(TxtDesign val) { defaultDesign = val; }
	
	// Local Desgin Choice
	private TxtDesign design = null;
	public TxtDesign getDesign()
	{
		if (design != null) return design;
		return getDefaultDesign();
	}
	public void setDesign(TxtDesign design)
	{
		this.design = design;
	}
	
	// -------------------------------------------- //
	// CONSTRUCTORS
	// -------------------------------------------- //
	
	public Txt()
	{
		
	}
	
	public Txt(TxtDesign design)
	{
		this.design = design;
	}
	
	// -------------------------------------------- //
	// Top-level parsing functions.
	// -------------------------------------------- //
	
	public String parse(String string, Object... args)
	{
		return String.format(this.parse(string), args);
	}
	
	public String parse(String string)
	{
		return this.parseTags(parseColor(string));
	}
	
	public ArrayList<String> parse(Collection<String> strings)
	{
		return this.parseTags(parseColor(strings));
	}
	
	// -------------------------------------------- //
	// Tag parsing
	// -------------------------------------------- //
	
	public static final transient Pattern patternTag = Pattern.compile("<([a-zA-Z0-9_]*)>");
	public String replaceTags(String string, Map<String, String> tags)
	{
		StringBuffer ret = new StringBuffer();
		Matcher matcher = patternTag.matcher(string);
		while (matcher.find())
		{
			String tag = matcher.group(1);
			String repl = tags.get(tag);
			if (repl == null)
			{
				matcher.appendReplacement(ret, "<"+tag+">");
			}
			else
			{
				matcher.appendReplacement(ret, repl);
			}
		}
		matcher.appendTail(ret);
		return ret.toString();
	}
	
	public String parseTags(String string)
	{
		return replaceTags(string, this.getDesign().getTags());
	}
	
	public ArrayList<String> parseTags(Collection<String> strings)
	{
		ArrayList<String> ret = new ArrayList<String>(strings.size());
		for (String string : strings)
		{
			ret.add(this.parseTags(string));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// Color parsing
	// -------------------------------------------- //
	
	public String parseColor(String string)
	{
		string = parseColorAmp(string);
		string = parseColorAcc(string);
		string = parseColorTags(string);
		return string;
	}
	
	public ArrayList<String> parseColor(Collection<String> strings)
	{
		ArrayList<String> ret = new ArrayList<String>(strings.size());
		for (String string : strings)
		{
			ret.add(this.parseColor(string));
		}
		return ret;
	}
	
	public String parseColorAmp(String string)
	{
		string = string.replaceAll("(§([a-z0-9]))", "\u00A7$2");
	    string = string.replaceAll("(&([a-z0-9]))", "\u00A7$2");
	    string = string.replace("&&", "&");
	    return string;
	}
	
    public String parseColorAcc(String string)
    {
        return string.replace("`e", "")
		.replace("`r", ChatColor.RED.toString()) .replace("`R", ChatColor.DARK_RED.toString())
		.replace("`y", ChatColor.YELLOW.toString()) .replace("`Y", ChatColor.GOLD.toString())
		.replace("`g", ChatColor.GREEN.toString()) .replace("`G", ChatColor.DARK_GREEN.toString())
		.replace("`a", ChatColor.AQUA.toString()) .replace("`A", ChatColor.DARK_AQUA.toString())
		.replace("`b", ChatColor.BLUE.toString()) .replace("`B", ChatColor.DARK_BLUE.toString())
		.replace("`p", ChatColor.LIGHT_PURPLE.toString()) .replace("`P", ChatColor.DARK_PURPLE.toString())
		.replace("`k", ChatColor.BLACK.toString()) .replace("`s", ChatColor.GRAY.toString())
		.replace("`S", ChatColor.DARK_GRAY.toString()) .replace("`w", ChatColor.WHITE.toString());
    }
	
	public String parseColorTags(String string)
	{
        return string.replace("<empty>", "")
        .replace("<black>", "\u00A70")
        .replace("<navy>", "\u00A71")
        .replace("<green>", "\u00A72")
        .replace("<teal>", "\u00A73")
        .replace("<red>", "\u00A74")
        .replace("<purple>", "\u00A75")
        .replace("<gold>", "\u00A76")
        .replace("<silver>", "\u00A77")
        .replace("<gray>", "\u00A78")
        .replace("<blue>", "\u00A79")
        .replace("<lime>", "\u00A7a")
        .replace("<aqua>", "\u00A7b")
        .replace("<rose>", "\u00A7c")
        .replace("<pink>", "\u00A7d")
        .replace("<yellow>", "\u00A7e")
        .replace("<white>", "\u00A7f");
	}
	
	// -------------------------------------------- //
	// Standard utils like UCFirst, implode and repeat.
	// -------------------------------------------- //
	
	public String upperCaseFirst(String string)
	{
		return string.substring(0, 1).toUpperCase()+string.substring(1);
	}
	
	public String repeat(String string, int times)
	{
	    if (times <= 0) return "";
	    else return string + repeat(string, times-1);
	}
	
	public String implode(List<String> list, String glue)
	{
	    return this.implode(list.toArray(new String[0]), glue);
	}
	
	public String implode(Object[] list, String glue)
	{
	    StringBuilder ret = new StringBuilder();
	    for (int i=0; i<list.length; i++)
	    {
	        if (i!=0)
	        {
	        	ret.append(glue);
	        }
	        ret.append(list[i]);
	    }
	    return ret.toString();
	}
	
	public String implodeCommaAnd(List<String> list, String comma, String and)
	{
	    if (list.size() == 0) return "";
		if (list.size() == 1) return list.get(0);
		
		String lastItem = list.get(list.size()-1);
		String nextToLastItem = list.get(list.size()-2);
		String merge = nextToLastItem+and+lastItem;
		list.set(list.size()-2, merge);
		list.remove(list.size()-1);
		
		return implode(list, comma);
	}
	
	public String implodeCommaAnd(List<String> list, String color)
	{
	    return implodeCommaAnd(list, color+", ", color+" and ");
	}
	
	public String implodeCommaAnd(List<String> list)
	{
		return implodeCommaAnd(list, "");
	}
	
	// -------------------------------------------- //
	// Material name tools
	// -------------------------------------------- //
	
	public String getMaterialName(Material material)
	{
		return material.toString().replace('_', ' ').toLowerCase();
	}
	
	public String getMaterialName(int materialId)
	{
		return getMaterialName(Material.getMaterial(materialId));
	}
	
	// -------------------------------------------- //
	// Paging and chrome-tools like titleize
	// -------------------------------------------- //
	
	private final String titleizeLine = repeat("_", 52);
	private final int titleizeBalance = -1;
	public String titleize(String str)
	{
		String center = ".[ "+ this.getDesign().getColorLogo() + str + this.getDesign().getColorArt() + " ].";
		int centerlen = ChatColor.stripColor(center).length();
		int pivot = titleizeLine.length() / 2;
		int eatLeft = (centerlen / 2) - titleizeBalance;
		int eatRight = (centerlen - eatLeft) + titleizeBalance;

		if (eatLeft < pivot)
			return this.getDesign().getColorArt()+titleizeLine.substring(0, pivot - eatLeft) + center + titleizeLine.substring(pivot + eatRight);
		else
			return this.getDesign().getColorArt()+center;
	}
	
	public ArrayList<String> getPage(List<String> lines, int pageHumanBased, String title)
	{
		ArrayList<String> ret = new ArrayList<String>();
		int pageZeroBased = pageHumanBased - 1;
		int pageheight = 9;
		int pagecount = (lines.size() / pageheight)+1;
		
		ret.add(this.titleize(title+" "+pageHumanBased+"/"+pagecount));
		
		if (pagecount == 0)
		{
			ret.add(this.parseTags("<i>Sorry. No Pages available."));
			return ret;
		}
		else if (pageZeroBased < 0 || pageHumanBased > pagecount)
		{
			ret.add(this.parseTags("<i>Invalid page. Must be between 1 and "+pagecount));
			return ret;
		}
		
		int from = pageZeroBased * pageheight;
		int to = from+pageheight;
		if (to > lines.size())
		{
			to = lines.size();
		}
		
		ret.addAll(lines.subList(from, to));
		
		return ret;
	}
	
	// -------------------------------------------- //
	// Describing Time
	// -------------------------------------------- //
	
	/**
	 * Using this function you transform a delta in milliseconds
	 * to a String like "2 weeks from now" or "7 days ago".
	 */
	public static final long millisPerSecond = 1000;
	public static final long millisPerMinute =   60 * millisPerSecond;
	public static final long millisPerHour   =   60 * millisPerMinute;
	public static final long millisPerDay    =   24 * millisPerHour;
	public static final long millisPerWeek   =    7 * millisPerDay;
	public static final long millisPerMonth  =   31 * millisPerDay;
	public static final long millisPerYear   =  365 * millisPerDay;
	
	public static Map<String, Long> unitMillis;
	
	static
	{
		unitMillis = new LinkedHashMap<String, Long>();
		unitMillis.put("years", millisPerYear);
		unitMillis.put("months", millisPerMonth);
		unitMillis.put("weeks", millisPerWeek);
		unitMillis.put("days", millisPerDay);
		unitMillis.put("hours", millisPerHour);
		unitMillis.put("minutes", millisPerMinute);
		unitMillis.put("seconds", millisPerSecond);
	}
	
	public String getTimeDeltaDescriptionRelNow(long millis)
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
	// String comparison
	// -------------------------------------------- //
	
	public String getBestCIStart(Collection<String> candidates, String start)
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
	// Wrapping the Craftbukkit TextWrapper
	// -------------------------------------------- //
	public ArrayList<String> wrap(final String string)
	{
		return new ArrayList<String>(Arrays.asList(TextWrapper.wrapText(string)));
	}
	
	public ArrayList<String> wrap(final Collection<String> strings)
	{
		ArrayList<String> ret = new ArrayList<String>();
		for (String line : strings)
		{
			ret.addAll(this.wrap(line));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// Parse and Wrap combo
	// -------------------------------------------- //
	
	public ArrayList<String> parseWrap(final String string)
	{
		return this.wrap(this.parse(string));
	}
	
	public ArrayList<String> parseWrap(final Collection<String> strings)
	{
		return this.wrap(this.parse(strings));
	}
}
