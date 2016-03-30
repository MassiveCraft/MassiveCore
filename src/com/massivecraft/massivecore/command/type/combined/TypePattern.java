package com.massivecraft.massivecore.command.type.combined;

import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.enumeration.TypeDyeColor;
import com.massivecraft.massivecore.command.type.enumeration.TypePatternType;

public class TypePattern extends TypeCombined<Pattern>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePattern i = new TypePattern();
	public static TypePattern get() { return i; }
	
	public TypePattern()
	{
		super(
			TypeDyeColor.get(),
			TypePatternType.get()
		);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public List<Object> split(Pattern value)
	{
		return new MassiveList<Object>(
			value.getColor(),
			value.getPattern()
		);
	}
	
	@Override
	public Pattern combine(List<Object> parts)
	{
		DyeColor color = null;
		PatternType pattern = null;
		
		for (int i = 0 ; i < parts.size() ; i++)
		{
			Object part = parts.get(i);
			
			if (i == 0)
			{
				color = (DyeColor)part;
			}
			else if (i == 1)
			{
				pattern = (PatternType) part;
			}
		}
		
		return new Pattern(color, pattern);
	}
	
}
