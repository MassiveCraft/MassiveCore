package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TypeChatColor extends TypeEnum<ChatColor>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeChatColor i = new TypeChatColor();
	public static TypeChatColor get() { return i; }
	public TypeChatColor()
	{
		super(ChatColor.class);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ChatColor getVisualColor(ChatColor value, CommandSender sender)
	{
		return value;
	}

}
