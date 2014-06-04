package com.massivecraft.massivecore.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static String SPECIAL_PREFIX_LENIENT = "[";
	public final static String SPECIAL_PREFIX_STRICT = SPECIAL_PREFIX_LENIENT + ChatColor.DARK_GREEN.toString();
	
	public final static String SPECIAL_SUFFIX_LENIENT = "]";
	public final static String SPECIAL_SUFFIX_STRICT = ChatColor.BLACK.toString() + SPECIAL_SUFFIX_LENIENT;
	
	// -------------------------------------------- //
	// SPECIAL LINE, TITLE & PADDING
	// -------------------------------------------- //
	// "special line": The whole first line with title and padding.
	// "special title": The title part without the padding.
	// "special padding": The special prefix and suffix.
	
	public static String getSanitizedSpecialTitle(String title)
	{
		if (title == null) throw new NullPointerException("title");
		
		title = title.trim();
		title = ChatColor.stripColor(title);
		title = Txt.upperCaseFirst(title);
		
		return title;
	}
	
	public static String getSpecialLine(String title)
	{
		if (title == null) throw new NullPointerException("title");
		
		title = getSanitizedSpecialTitle(title);
		
		return SPECIAL_PREFIX_STRICT + title + SPECIAL_SUFFIX_STRICT;
	}
	
	public static String getSpecialTitle(String line, boolean strict)
	{
		if (line == null) throw new NullPointerException("line");
		
		String prefix = SPECIAL_PREFIX_STRICT;
		String suffix = SPECIAL_SUFFIX_STRICT;
		
		if (!strict)
		{
			line = line.trim();
			line = ChatColor.stripColor(line);
			
			prefix = SPECIAL_PREFIX_LENIENT;
			suffix = SPECIAL_SUFFIX_LENIENT;
		}
		
		if (!line.startsWith(prefix)) return null;
		if (!line.endsWith(suffix)) return null;
		
		String title = line.substring(prefix.length(), line.length() - suffix.length());
		
		title = getSanitizedSpecialTitle(title);
		
		return title;
	}
	
	public static String getSpecialTitle(String[] lines, boolean strict)
	{
		if (lines == null) throw new NullPointerException("lines");
		String line = lines[0];
		return getSpecialTitle(line, strict);
	}
	
	public static String getSpecialTitle(Sign sign, boolean strict)
	{
		if (sign == null) throw new NullPointerException("sign");
		String[] lines = sign.getLines();
		return getSpecialTitle(lines, strict);
	}
	
	public static String getSpecialTitle(Block block, boolean strict)
	{
		if (block == null) throw new NullPointerException("block");
		Sign sign = getSign(block);
		if (sign == null) return null;
		return getSpecialTitle(sign, strict);
	}
	
	// -------------------------------------------- //
	// SPECIAL PERMISSON FIX
	// -------------------------------------------- //
	// Returns true if the result is a special sign of with the specified title.
	
	public static boolean handleSpecialPermissionFix(SignChangeEvent event, String title, String permissionNode)
    {
		if (event == null) throw new NullPointerException("event");
		if (title == null) throw new NullPointerException("title");
		if (permissionNode == null) throw new NullPointerException("permissionNode");
		
		// If a player is changing a sign ...
		final Player player = event.getPlayer();
		
		// ... that leniently is a special sign ...
		String lenientTitle = getSpecialTitle(event.getLines(), false);
		if (lenientTitle == null) return false;
		
		// ... of the type we seek ...
		if (!title.equalsIgnoreCase(lenientTitle)) return false;
		
		// ... verify that the player has permission to create that type of sign ...
		if (!PermUtil.has(player, permissionNode, true))
		{
			event.setCancelled(true);
			return false;
		}
		
		// ... and fix the first line.
		event.setLine(0, getSpecialLine(title));
		return true;
    }
	
	// -------------------------------------------- //
	// SPECIAL PILLAR
	// -------------------------------------------- //
	// These methods are used for detecting special sign pillars
	
	public static Block getSpecialPillarTop(Block block)
	{
		World world = block.getWorld();
		int maxHeight = world.getMaxHeight();
		
		while (isSign(block) && block.getY() < maxHeight)
		{
			if (getSpecialTitle(block, true) != null) return block;
			block = block.getRelative(0, 1, 0);
		}
		
		return null;
	}
	
	public static List<Block> getSpecialPillarFromTop(Block block)
	{
		if (block == null) throw new NullPointerException("block");
		if (getSpecialTitle(block, true) == null) throw new InvalidParameterException("block");
		
		List<Block> ret = new ArrayList<Block>();
		
		do
		{
			ret.add(block);
			block = block.getRelative(0, -1, 0);
		}
		while (isSign(block) && getSpecialTitle(block, true) == null);
				
		return ret;
	}
	
	public static List<Block> getSpecialPillar(Block block)
	{
		if (block == null) throw new NullPointerException("block");
		
		block = getSpecialPillarTop(block);
		if (block == null) return null;
		
		return getSpecialPillarFromTop(block);
	}
	
	public static List<String> getSpecialPillarLines(Block block, String title)
	{
		if (block == null) throw new NullPointerException("block");
		
		List<Block> blocks = getSpecialPillar(block);
		if (blocks == null) return null;
		
		List<String> lines = getLines(blocks);
		if (lines == null) return null;
		
		if (title != null)
		{
			String specialLine = lines.remove(0);
			String specialTitle = getSpecialTitle(specialLine, true);
			if (!title.equalsIgnoreCase(specialTitle)) return null;
		}
		
		return lines;
	}
	
	public static List<String> getSpecialPillarLines(PlayerInteractEvent event, String title)
	{
		if (event == null) throw new NullPointerException("event");
		if (title == null) throw new NullPointerException("title");
		
		Block block = event.getClickedBlock();
		if (block == null) return null;
		
		List<String> lines = getSpecialPillarLines(block, title);
		if (lines == null) return null;
		
		return lines;
	}
	
	// -------------------------------------------- //
	// IS SIGN
	// -------------------------------------------- //
	
	public static boolean isSign(Material material)
	{
		if (material == null) throw new NullPointerException("material");
		
		if (material == Material.SIGN) return true;
		if (material == Material.SIGN_POST) return true;
		if (material == Material.WALL_SIGN) return true;
		
		return false;
	}
	
	public static boolean isSign(Block block)
	{
		if (block == null) throw new NullPointerException("block");
		
		return isSign(block.getType());
	}
	
	// -------------------------------------------- //
	// GET SIGN
	// -------------------------------------------- //
	
	public static Sign getSign(Block block)
	{
		BlockState blockState = block.getState();
		if (!(blockState instanceof Sign)) return null;
		return (Sign)blockState;
	}
	
	// -------------------------------------------- //
	// GET LINES
	// -------------------------------------------- //
	
	public static List<String> getLines(List<Block> blocks)
	{
		if (blocks == null) throw new NullPointerException("blocks");
		
		List<String> ret = new ArrayList<String>();
		
		for (Block block : blocks)
		{
			List<String> lines = getLines(block);
			if (lines != null)
			{
				ret.addAll(lines);
			}
		}
		
		return ret;
	}
	
	public static List<String> getLines(Block block)
	{
		if (block == null) throw new NullPointerException("block");
		
		Sign sign = getSign(block);
		if (sign == null) return null;
		
		return getLines(sign);
	}
	
	public static List<String> getLines(Sign sign)
	{
		if (sign == null) throw new NullPointerException("sign");
		
		return new ArrayList<String>(Arrays.asList(sign.getLines()));
	}

	
}
