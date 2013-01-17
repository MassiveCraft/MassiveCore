package com.massivecraft.mcore5.sender;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;

public class BasicCommandSender extends PermissibleBase implements CommandSender
{
	private final String name;
	
	public BasicCommandSender(String name, boolean op, boolean opChangeable)
	{
		super(new BasicServerOperator(name, op, opChangeable));
		this.name = name;
	}

	@Override
	public void setOp(boolean value)
	{
		boolean before = this.isOp();
		super.setOp(value);
		boolean after = this.isOp();
		if (before == after) return;
		this.recalculatePermissions();
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public Server getServer()
	{
		return Bukkit.getServer();
	}

	@Override
	public void sendMessage(String message)
	{
		// Per default
	}

	@Override
	public void sendMessage(String[] messages)
	{
		for (String message : messages)
		{
			this.sendMessage(message);
		}
	}
}
