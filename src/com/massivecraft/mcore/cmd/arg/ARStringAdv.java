package com.massivecraft.mcore.cmd.arg;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.mcore.util.Txt;

public class ARStringAdv extends ArgReaderAbstract<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARStringAdv i = new ARStringAdv();
	public static ARStringAdv get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ArgResult<String> read(String arg, CommandSender sender)
	{
		// Prepare
		ArgResult<String> ret = new ArgResult<String>(arg);
		if (arg == null) return ret;
		
		// null
		if (arg.equalsIgnoreCase("null"))
		{
			ret.setResult(null);
			return ret;
		}
		
		// book
		if (arg.equalsIgnoreCase("book"))
		{
			String bookText = getBookText(sender);
			ret.setResult(bookText);
			if (!ret.hasResult())
			{
				ret.getErrors().add("<b>No book content detected.");
			}
			return ret;
		}
		
		if (!ret.hasResult())
		{
			ret.getErrors().add("<b>No sound matches \"<h>"+arg+"<b>\".");
			ret.getErrors().add("<aqua>https://github.com/Bukkit/Bukkit/blob/master/src/main/java/org/bukkit/Sound.java");
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getBookText(CommandSender sender)
	{
		if (sender == null) return null;
		if (!(sender instanceof HumanEntity)) return null;
		HumanEntity human = (HumanEntity)sender;
		ItemStack item = human.getItemInHand();
		if (item == null) return null;
		if (!item.hasItemMeta()) return null;
		ItemMeta itemMeta = item.getItemMeta();
		if (!(itemMeta instanceof BookMeta)) return null;
		BookMeta bookMeta = (BookMeta)itemMeta;
		if (!bookMeta.hasPages()) return null;
		List<String> pages = bookMeta.getPages();
		return Txt.implode(pages, "\n\n");
	}
	
}
