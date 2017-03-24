package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.util.IdData;
import org.bukkit.command.CommandSender;

public abstract class EventMassiveCoreSenderRegistry extends EventMassiveCore
{	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	
	private final IdData data;
	public IdData getData() { return this.data; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreSenderRegistry(CommandSender sender, IdData data)
	{
		this.sender = sender;
		this.data = data;
	}
	
}
