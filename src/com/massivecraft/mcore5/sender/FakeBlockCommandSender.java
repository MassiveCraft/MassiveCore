package com.massivecraft.mcore5.sender;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;

public class FakeBlockCommandSender extends BasicCommandSender implements BlockCommandSender
{
	public FakeBlockCommandSender()
	{
		super("@", true, false);
	}

	@Override
	public Block getBlock()
	{
		return Bukkit.getWorlds().get(0).getBlockAt(0, 0, 0);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static FakeBlockCommandSender i = new FakeBlockCommandSender();
	public static FakeBlockCommandSender get() { return i; }
	
}
