package com.massivecraft.massivecore;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public class Progressbar
{
	// -------------------------------------------- //
	// STANDARD INSTANCES
	// -------------------------------------------- //
	
	public static final transient Progressbar HEALTHBAR_CLASSIC = Progressbar.valueOf(1D, 30, "{c}[", "|", "&8", "|", "{c}]", 1D, "{c}", MUtil.map(
		1.0D, "&2",
		0.8D, "&a",
		0.5D, "&e",
		0.4D, "&6",
		0.3D, "&c",
		0.2D, "&4"
	));
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	private final double quota;
	public double getQuota() { return this.quota; }
	
	private final int width;
	public int getWidth() { return this.width; }
	
	private final String left;
	public String getLeft() { return this.left; }
	
	private final String solid;
	public String getSolid() { return this.solid; }
	
	private final String between;
	public String getBetween() { return this.between; }
	
	private final String empty;
	public String getEmpty() { return this.empty; }
	
	private final String right;
	public String getRight() { return this.right; }
	
	private final double solidsPerEmpty;
	public double getSolidsPerEmpty() { return this.solidsPerEmpty; }
	
	private final String colorTag;
	public String getColorTag() { return this.colorTag; }
	
	private final Map<Double, String> roofToColor;
	public Map<Double, String> getRoofToColor() { return this.roofToColor; }
	
	// -------------------------------------------- //
	// FIELDS: WITH
	// -------------------------------------------- //
	
	public Progressbar withQuota(double quota) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar withWidth(int width) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar withLeft(String left) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar solid(String solid) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar between(String between) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar empty(String empty) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar right(String right) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar solidsPerEmpty(double solidsPerEmpty) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar colorTag(String colorTag) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	public Progressbar roofToColor(Map<Double, String> roofToColor) { return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor); }
	
	// -------------------------------------------- //
	// PRIVATE CONSTRUCTOR
	// -------------------------------------------- //
	
	private Progressbar(double quota, int width, String left, String solid, String between, String empty, String right, double solidsPerEmpty, String colorTag, Map<Double, String> roofToColor)
	{
		this.quota = quota;
		this.width = width;
		this.left = left;
		this.solid = solid;
		this.between = between;
		this.empty = empty;
		this.right = right;
		this.solidsPerEmpty = solidsPerEmpty;
		this.colorTag = colorTag;
		this.roofToColor = Collections.unmodifiableMap(roofToColor);
	}
	
	// -------------------------------------------- //
	// FACTORY: VALUE OF
	// -------------------------------------------- //
	
	public static Progressbar valueOf(double quota, int width, String left, String solid, String between, String empty, String right, double solidsPerEmpty, String colorTag, Map<Double, String> roofToColor)
	{
		return new Progressbar(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor);
	}

	// -------------------------------------------- //
	// INSTANCE METHODS
	// -------------------------------------------- //
	
	public String render()
	{
		return render(quota, width, left, solid, between, empty, right, solidsPerEmpty, colorTag, roofToColor);
	}
		
	// -------------------------------------------- //
	// STATIC UTIL
	// -------------------------------------------- //
	
	public static String render(double quota, int width, String left, String solid, String between, String empty, String right, double solidsPerEmpty, String colorTag, Map<Double, String> roofToColor)
	{
		// Ensure between 0 and 1;
		quota = limit(quota);
		
		// What color is the health bar?
		String color = pick(quota, roofToColor);
		
		// how much solid should there be?
		int solidCount = (int) Math.ceil(width * quota);
		
		// The rest is empty
		int emptyCount = (int) ((width - solidCount) / solidsPerEmpty);
		
		// Create the non-parsed bar
		String ret = left + Txt.repeat(solid, solidCount) + between + Txt.repeat(empty, emptyCount) + right;
		
		// Replace color tag
		ret = ret.replace(colorTag, color);
				
		// Parse amp color codes
		ret = Txt.parse(ret);
		
		return ret;
	}
	
	public static double limit(double quota)
	{
		if (quota > 1) return 1;
		if (quota < 0) return 0;
		return quota;
	}
	
	public static <T> T pick(double quota, Map<Double, T> roofToValue)
	{
		Double currentRoof = null;
		T ret = null;
		for (Entry<Double, T> entry : roofToValue.entrySet())
		{
			double roof = entry.getKey();
			T value = entry.getValue();
			if (quota <= roof && (currentRoof == null || roof <= currentRoof))
			{
				currentRoof = roof;
				ret = value;
			}
		}
		return ret;
	}
	
}
