package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.enumeration.TypeDyeColor;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class TypeColor extends TypeAbstract<Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeColor i = new TypeColor();
	public static TypeColor get() { return i; }
	public TypeColor() { super(Color.class); }
	
	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(Color value, CommandSender sender)
	{
		DyeColor dyeColor = DyeColor.getByColor(value);
		if (dyeColor != null) return MUtil.getChatColor(dyeColor) + Txt.getNicedEnum(dyeColor);
		return ChatColor.RED.toString() + value.getRed() + " " + ChatColor.GREEN.toString() + value.getGreen() + " " + ChatColor.BLUE.toString() + value.getBlue(); 
	}
	
	// -------------------------------------------- //
	// WRITE ID
	// -------------------------------------------- //
	
	@Override
	public String getIdInner(Color value)
	{
		return value.getRed() + " " + value.getGreen() + " " + value.getBlue(); 
	}
	
	// -------------------------------------------- //
	// READ
	// -------------------------------------------- //
	
	@Override
	public Color read(String arg, CommandSender sender) throws MassiveException
	{	
		Color ret;
		
		// Try RGB
		ret = readInnerRgb(arg);
		if (ret != null) return ret;
		
		// Try Hex
		ret = readInnerHex(arg);
		if (ret != null) return ret;
		
		// Try DyeColor
		ret = readInnerDyeColor(arg);
		if (ret != null) return ret;
		
		throw new MassiveException().addMsg("<b>No color matches \"<h>%s<b>\".", arg);
	}
	
	public Color readInnerRgb(String arg) throws MassiveException
	{
		String[] rgb = Txt.PATTERN_WHITESPACE.split(arg);
		if (rgb.length != 3) return null;
		
		int red = readInnerRgbNumber(rgb[0]);
		int green = readInnerRgbNumber(rgb[1]);
		int blue = readInnerRgbNumber(rgb[2]);

		return Color.fromRGB(red, green, blue);
	}
	
	private int readInnerRgbNumber(String arg) throws MassiveException
	{
		int ret = TypeInteger.get().read(arg);
		if (ret > 255 || ret < 0) throw new MassiveException().addMsg("<b>RGB number must be between 0 and 255.");
		return ret;
	}
	
	public Color readInnerHex(String arg) throws MassiveException
	{
		boolean verbose = false;
		
		// Explicit verbose hex
		if (arg.startsWith("#"))
		{
			arg = arg.substring(1);
			verbose = true;
		}
		
		// Length check 
		if (arg.length() != 6)
		{
			if (verbose) throw new MassiveException().addMsg("<b>Hex must be 6 hexadecimals.");
			return null;
		}
		
		try
		{
			int red = Integer.parseInt(arg.substring(0, 2), 16);
			int green = Integer.parseInt(arg.substring(2, 4), 16);
			int blue = Integer.parseInt(arg.substring(4, 6), 16);
			
			return Color.fromRGB(red, green, blue);
		}
		catch (IllegalArgumentException e)
		{
			if (verbose) throw new MassiveException().addMsg("<b>\"<h>%s<b>\" is not valid hexadecimal.", arg);
			return null;
		}
	}
	
	public Color readInnerDyeColor(String arg) throws MassiveException
	{
		try
		{
			DyeColor color = TypeDyeColor.get().read(arg);
			return color.getColor();
		}
		catch (MassiveException e)
		{
			return null;
		}
	}
	
	// -------------------------------------------- //
	// TAB LIST
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return TypeDyeColor.get().getTabList(sender, arg);
	}
	
}
