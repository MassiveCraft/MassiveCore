package com.massivecraft.mcore.sender;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.util.IdUtil;

public class BasicCommandSender extends PermissibleBase implements CommandSender
{
	public final String name;
	
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
		// Nothing per default
	}

	@Override
	public void sendMessage(String[] messages)
	{
		for (String message : messages)
		{
			this.sendMessage(message);
		}
	}
	
	public void register()
	{
		final BasicCommandSender ME = this;
		
		// register later
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				ME.registerImmediately();
			}
		});
		
		// and register now
		registerImmediately();
	}
	
	public void registerImmediately()
	{
		IdUtil.register(this);
	}
	
	public void unregister()
	{
		IdUtil.unregister(this);
	}
	
}
