package com.massivecraft.massivecore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.cmd.arg.AR;
import com.massivecraft.massivecore.cmd.arg.ARInteger;

public class ArgSetting<T>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String DEFAULT_DESC_DEFAULT = null;
	public static final Object DEFAULT_VALUE_DEFAULT = null;
	public static final boolean REQUIRED_FROM_CONSOLE_DEFAULT = false;
	public static final String DESCRIPTION_DEFAULT = null;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected AR<T> reader;
	public AR<T> getReader() { return reader; }
	public ArgSetting<T> setReader(AR<T> reader) { this.reader = reader; return this; }
	
	protected String name;
	public String getName() { return name; }
	public ArgSetting<T> setName(String name) { this.name = name; return this; }
	
	protected String defaultDesc = null;
	public ArgSetting<T> setDefaultDesc(String defaultDesc) { this.defaultDesc = defaultDesc; return this; }
	public String getDefaultDesc()
	{
		if (this.defaultDesc != null) return defaultDesc;
		if (this.isDefaultValueSet()) return String.valueOf(this.getDefaultValue());
		return null;
	}
	
	protected T defaultValue = null;
	public T getDefaultValue() { return defaultValue; }
	public ArgSetting<T> setDefaultValue(T defaultValue)
	{
		this.defaultValue = defaultValue;
		this.defaultValueSet = true;
		return this;
	}
	
	// A default value can be null.
	// So we must keep track of this field too.
	protected boolean defaultValueSet = false;
	public boolean isDefaultValueSet() { return this.defaultValueSet; }
	public void setDefaultValueSet(boolean defaultValueSet) { this.defaultValueSet = defaultValueSet; }
	
	
	// Convenience
	public boolean isRequired() { return this.getDefaultDesc() == null; }
	public boolean isOptional() { return ! this.isRequired(); }
	
	// Is this arg ALWAYS required from the console?
	// That might the case if the arg is a player. and default is oneself.
	protected boolean requiredFromConsole = false;
	public boolean isRequiredFromConsole() { return requiredFromConsole; }
	public ArgSetting<T> setRequiredFromConsole(boolean requiredFromConsole) { this.requiredFromConsole = requiredFromConsole; return this; }
	
	// An optional description of this argument.
	// Examples:
	// 1. "the faction to show info about"
	// 2. "the ticket to pick"
	// 3. "the amount of money to pay"
	protected String desc = null;
	public String getDesc() { return desc; }
	public ArgSetting<T> setDesc(String desc) { this.desc = desc; return this; }
	
	public boolean hasDesc() { return this.getDesc() != null; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	// To minimize confusion and mixing of arguments for the constructor
	// description must not be set in the constructor.
	
	// All
	public ArgSetting(T defaultValue, AR<T> reader, boolean requiredFromConsole, String name, String defaultDesc)
	{
		// Null checks
		if (reader == null) throw new IllegalArgumentException("reader mustn't be null");
		if (name == null) throw new IllegalArgumentException("name mustn't be null");
		
		this.setReader(reader);
		this.setRequiredFromConsole(requiredFromConsole);
		this.setName(name);
		this.setDefaultDesc(defaultDesc);
		this.setDefaultValue(defaultValue);
	}
	
	// Without defaultValue
	@SuppressWarnings("unchecked")
	public ArgSetting(AR<T> reader, boolean requiredFromConsole, String name, String defaultDesc)
	{
		this((T) DEFAULT_VALUE_DEFAULT, reader, requiredFromConsole, name, defaultDesc);
		
		// In fact the default value is not set.
		this.defaultValueSet = false;
	}
	
	// Without reqFromConsole.
	public ArgSetting(T defaultValue, AR<T> reader, String name, String defaultDesc)
	{
		this(defaultValue, reader, REQUIRED_FROM_CONSOLE_DEFAULT, name, defaultDesc);
	}
	
	// Without defaultDesc.
	public ArgSetting(T defaultValue, AR<T> reader, boolean requiredFromConsole, String name)
	{
		this(defaultValue, reader, requiredFromConsole, name, DEFAULT_DESC_DEFAULT);
	}
	
	// Without defaultValue & reqFromConsole.
	public ArgSetting(AR<T> reader, String name, String defaultDesc)
	{
		this(reader, REQUIRED_FROM_CONSOLE_DEFAULT, name, defaultDesc);
	}

	// Without defaultValue & defaultDesc.
	public ArgSetting(AR<T> reader, boolean requiredFromConsole, String name)
	{
		this(reader, requiredFromConsole, name, DEFAULT_DESC_DEFAULT);
	}

	// Without reqFromConsole and defaultDesc.
	public ArgSetting(T defaultValue, AR<T> reader, String name)
	{
		this(defaultValue, reader, REQUIRED_FROM_CONSOLE_DEFAULT, name, DEFAULT_DESC_DEFAULT);
	}
	
	// Without defaultValue, reqFromConsole and defaultDesc.
	public ArgSetting(AR<T> reader, String name)
	{
		this(reader, REQUIRED_FROM_CONSOLE_DEFAULT, name, DEFAULT_DESC_DEFAULT);
	}
	
	// -------------------------------------------- //
	// STATIC FACTORY (DEPRECATED)
	// -------------------------------------------- //
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		
		String def = this.getDefaultDesc();
		def = (def != null ? "=" + def : "");
		return "[" + this.getName() + def + "]";
	}
	
	// -------------------------------------------- //
	// COMMONLY USED ARG SETTINGS
	// -------------------------------------------- //

	public static ArgSetting<Integer> getPage()
	{
		// We can't use a singletone, because people might
		// want to set a description.
		return new ArgSetting<Integer>(1, ARInteger.get(), "page", "1");
	}
	
}
