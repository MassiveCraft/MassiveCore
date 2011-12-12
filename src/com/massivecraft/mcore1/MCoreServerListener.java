package com.massivecraft.mcore1;

import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListener;


public class MCoreServerListener extends ServerListener
{
	MCore p;
	
	private final static String refCommand = "mcoresilenteater";
	
	public MCoreServerListener(MCore p)
	{
		this.p = p;
	}
	
	@Override
	public void onServerCommand(ServerCommandEvent event)
	{
		if (event.getCommand().length() == 0) return;
		if (MCore.handleCommand(event.getSender(), event.getCommand(), false))
		{
			event.setCommand(refCommand);
		}
	}
}
