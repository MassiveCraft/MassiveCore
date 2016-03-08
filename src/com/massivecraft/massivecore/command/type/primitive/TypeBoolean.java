package com.massivecraft.massivecore.command.type.primitive;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;

public class TypeBoolean extends TypeAbstractChoice<Boolean>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final Set<String> NAMES_TRUE = new MassiveSet<String>(
		"Yes",
		"True",
		"On"
	);
	
	public static final Set<String> NAMES_FALSE = new MassiveSet<String>(
		"No",
		"False",
		"Off"
	);
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static TypeBoolean iYes = new TypeBoolean("Yes", "No");
	private static TypeBoolean iTrue = new TypeBoolean("True", "False");
	private static TypeBoolean iOn = new TypeBoolean("On", "Off");
	
	public static TypeBoolean getYes() { return iYes; }
	public static TypeBoolean getTrue() { return iTrue; }
	public static TypeBoolean getOn() { return iOn; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final String stringTrue;
	public String getNameTrue() { return this.stringTrue; }
	
	protected final String stringFalse;
	public String getNameFalse() { return this.stringFalse; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeBoolean(String t, String f)
	{
		this.stringTrue = t;
		this.stringFalse = f;
		this.setAll(
			Boolean.TRUE,
			Boolean.FALSE
		);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return "toggle";
	}
	
	@Override
	public String getVisualInner(Boolean value, CommandSender sender)
	{
		String name = this.getNameInner(value);
		ChatColor color = (value ? ChatColor.GREEN : ChatColor.RED);
		return color + name;
	}
	
	@Override
	public String getNameInner(Boolean value)
	{
		return value ? this.getNameTrue() : this.getNameFalse();
	}
	
	@Override
	public Set<String> getNamesInner(Boolean value)
	{
		// Create
		Set<String> ret = new MassiveSet<String>();
		
		// Fill
		ret.add(this.getNameInner(value));
		ret.addAll(value ? NAMES_TRUE : NAMES_FALSE);
		
		// Return
		return ret;
	}
	
	@Override
	public String getIdInner(Boolean value)
	{
		return value.toString();
	}

}
