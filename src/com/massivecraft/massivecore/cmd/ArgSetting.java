package com.massivecraft.massivecore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.cmd.arg.AR;
import com.massivecraft.massivecore.cmd.arg.ARInteger;

public class ArgSetting
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected AR<?> reader;
	public AR<?> getReader() { return reader; }
	public ArgSetting setReader(AR<?> reader) { this.reader = reader; return this; }
	
	protected String name;
	public String getName() { return name; }
	public ArgSetting setName(String name) { this.name = name; return this; }
	
	protected String def = null; // "default" is a reserved keyword in Java.
	public String getDefault() { return def; }
	public ArgSetting setDefault(String def) { this.def = def; return this; }
	
	// Convenience
	public boolean isRequired() { return this.getDefault() == null; }
	public boolean isOptional() { return ! this.isRequired(); }
	
	// Is this arg ALWAYS required from the console?
	// That might the case if the arg is a player. and default is oneself.
	protected boolean requiredFromConsole = false;
	public boolean isRequiredFromConsole() { return requiredFromConsole; }
	public ArgSetting setRequiredFromConsole(boolean requiredFromConsole) { this.requiredFromConsole = requiredFromConsole; return this; }
	
	// An optional description of this argument.
	// Examples:
	// 1. "the faction to show info about"
	// 2. "the ticket to pick"
	// 3. "the amount of money to pay"
	protected String desc = null;
	public String getDesc() { return desc; }
	public ArgSetting setDesc(String desc) { this.desc = desc; return this; }
	
	public boolean hasDesc() { return this.getDesc() != null; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	// To minimize confusion and mixing of arguments for the constructor
	// description must not be set in the constructor.
	
	// All
	public ArgSetting(AR<?> reader, boolean requiredFromConsole, String name, String def)
	{
		// Null checks
		if (reader == null) throw new IllegalArgumentException("reader mustn't be null");
		if (name == null) throw new IllegalArgumentException("name mustn't be null");
		
		this.setReader(reader);
		this.setRequiredFromConsole(requiredFromConsole);
		this.setName(name);
		this.setDefault(def);
	}
	
	// Without reqFromConsole.
	public ArgSetting(AR<?> reader, String name, String def)
	{
		this(reader, false, name, def);
	}
	
	// Without default.
	public ArgSetting(AR<?> reader, boolean requiredFromConsole, String name)
	{
		this(reader, requiredFromConsole, name, null);
	}

	// Without reqFromConsole and default.
	public ArgSetting (AR<?> reader, String name)
	{
		this(reader, false, name, null);
	}
	
	// -------------------------------------------- //
	// STATIC FACTORY
	// -------------------------------------------- //
	
	@Deprecated
	public static ArgSetting of(AR<?> reader, boolean requiredFromConsole, String name, String def)
	{
		// Null checks
		if (reader == null) throw new IllegalArgumentException("reader mustn't be null");
		if (name == null) throw new IllegalArgumentException("name mustn't be null");
		
		// Create
		return new ArgSetting(reader, requiredFromConsole, name, def);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public boolean isRequiredFor(CommandSender sender)
	{
		if (this.isRequired()) return true; // Required for everyone.
		if ( ! this.isRequiredFromConsole()) return false; // If not required for console. Then not anyone.
		if (sender == null) return false; // If null we will suppose it is a player.
		if ( ! (sender instanceof Player)) return true; // Required for console.
		return false; // Not required.
	}
	
	public boolean isOptionalFor(CommandSender sender)
	{
		return ! this.isRequiredFor(sender);
	}
	
	public String getUseageTemplateDisplayFor(CommandSender sender)
	{
		if (this.isRequiredFor(sender)) return "<" + this.getName() + ">";
		
		String def = this.getDefault();
		def = (def != null ? "=" + def : "");
		return "[" + this.getName() + def + "]";
	}
	
	// -------------------------------------------- //
	// COMMONLY USED ARG SETTINGS
	// -------------------------------------------- //

	public static ArgSetting getPager()
	{
		// We can't use a singletone, because people might
		// want to set a description.
		return new ArgSetting(ARInteger.get(), "page", "1");
	}
	
}
