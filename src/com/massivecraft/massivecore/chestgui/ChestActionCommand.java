package com.massivecraft.massivecore.chestgui;

import java.security.InvalidParameterException;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.massivecraft.massivecore.mixin.Mixin;

public class ChestActionCommand extends ChestActionAbstract
{
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	protected String command = null;
	
	public void setCommand(String command)
	{
		if ( ! command.startsWith("/")) throw new InvalidParameterException("Commands start with \"/\". Do include the leading slash.");
		this.command = command;
	}
	
	public String getCommand()
	{
		return this.command;
	}
	
	public String getCommandLine()
	{
		if (this.command == null) return null;
		return this.command.substring(1);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ChestActionCommand()
	{
		
	}
	
	public ChestActionCommand(String command)
	{
		this.setCommand(command);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean onClick(InventoryClickEvent event, Player player)
	{
		String commandLine = this.getCommandLine();
		if (commandLine == null) return false;
		
		return Mixin.dispatchCommand(player, commandLine);
	}
	
}
