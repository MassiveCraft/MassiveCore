package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.collections.MassiveList;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import java.util.List;

public class EventMassiveCoreDestinationTabList extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final String arg;
	public String getArg() { return this.arg; }
	
	private final CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	
	private final List<String> suggestions;
	public List<String> getSuggestions() { return this.suggestions; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreDestinationTabList(String arg, CommandSender sender)
	{
		this.arg = arg;
		this.sender = sender;
		this.suggestions = new MassiveList<>();
	}
	
}
