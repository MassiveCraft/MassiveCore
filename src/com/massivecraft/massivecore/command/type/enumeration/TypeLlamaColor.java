package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Llama.Color;

public class TypeLlamaColor extends TypeEnum<Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeLlamaColor i = new TypeLlamaColor();
	public static TypeLlamaColor get() { return i; }
	public TypeLlamaColor()
	{
		super(Color.class);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ChatColor getVisualColor(Color value, CommandSender sender)
	{
		return getChatColor(value);
	}
	
	private ChatColor getChatColor(Color color)
	{
		switch (color)
		{
			case CREAMY:
				return ChatColor.GRAY;
			
			case WHITE:
				return ChatColor.WHITE;
			
			case BROWN:
				return ChatColor.GOLD;
			
			case GRAY:
				return ChatColor.DARK_GRAY;
			
			default:
				return ChatColor.WHITE;
		}
	}
	
}
