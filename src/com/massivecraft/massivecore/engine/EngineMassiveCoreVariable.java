package com.massivecraft.massivecore.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public class EngineMassiveCoreVariable extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreVariable i = new EngineMassiveCoreVariable();
	public static EngineMassiveCoreVariable get() { return i; }
	
	// -------------------------------------------- //
	// VARIABLE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void variable(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		event.setMessage(variable(player, event.getMessage()));
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void variable(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		event.setMessage(variable(player, event.getMessage()));
	}
	
	public static String variable(Player player, String message)
	{
		message = variableBook(player, message);
		message = variableBuffer(player, message);
		return message;
	}
	
	// -------------------------------------------- //
	// VARIABLE BOOK
	// -------------------------------------------- //
	
	public static String getBookText(CommandSender sender)
	{
		if (sender == null) return null;
		if (!(sender instanceof HumanEntity)) return null;
		HumanEntity human = (HumanEntity)sender;
		
		PlayerInventory inventory = human.getInventory();
		String ret;
		
		ret = getBookText(inventory.getItemInHand());
		if (ret != null) return ret;
		
		// TODO: Handle 1.9 API without breaking 1.8 support
		
		// ret = getBookText(inventory.getItemInMainHand());
		// if (ret != null) return ret;
		
		// ret = getBookText(inventory.getItemInOffHand());
		// if (ret != null) return ret;
		
		return null;
	}
	
	public static String getBookText(ItemStack item)
	{
		if (item == null) return null;
		if (!item.hasItemMeta()) return null;
		ItemMeta itemMeta = item.getItemMeta();
		if (!(itemMeta instanceof BookMeta)) return null;
		BookMeta bookMeta = (BookMeta)itemMeta;
		if (!bookMeta.hasPages()) return null;
		List<String> pages = bookMeta.getPages();
		String ret = Txt.implode(pages, " ");
		ret = ret.replaceAll("\\n+", " ");
		return ret;
	}
	
	public static String variableBook(Player player, String message)
	{
		// If we are using this variable ...
		if (!MassiveCoreMConf.get().usingVariableBook) return message;
		
		// ... get the variable content ...
		String content = getBookText(player);
		if (content == null) return message;
		
		// ... check use permission ...
		if (!MassiveCorePerm.VARIABLE_BOOK.has(player, false)) return message;
		
		// ... and replace.
		return StringUtils.replace(message, MassiveCoreMConf.get().variableBook, content);
	}
	
	// -------------------------------------------- //
	// VARIABLE BUFFER
	// -------------------------------------------- //
	
	public static final Map<String, String> idToBuffer = new HashMap<String, String>();
	
	public static String getBuffer(Object senderObject)
	{
		String id = IdUtil.getId(senderObject);
		if (id == null) return null;
		String ret = idToBuffer.get(id);
		if (ret == null) ret = "";
		return ret;
	}
	
	public static void setBuffer(Object senderObject, String buffer)
	{
		String id = IdUtil.getId(senderObject);
		idToBuffer.put(id, buffer);
	}
	
	public static String variableBuffer(Player player, String message)
	{
		// If we are using this variable ...
		if (!MassiveCoreMConf.get().usingVariableBuffer) return message;
		
		// ... get the variable content ...
		String content = getBuffer(player);
		if (content == null) return message;
		
		// ... check use permission ...
		if (!MassiveCorePerm.VARIABLE_BUFFER.has(player, false)) return message;
		
		// ... and replace.
		return StringUtils.replace(message, MassiveCoreMConf.get().variableBuffer, content);
	}
}
