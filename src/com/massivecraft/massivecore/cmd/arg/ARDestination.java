package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.event.EventMassiveCoreDestination;
import com.massivecraft.massivecore.teleport.Destination;

public class ARDestination extends ARAbstract<Destination>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARDestination i = new ARDestination();
	public static ARDestination get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Destination read(String arg, CommandSender sender) throws MassiveException
	{
		EventMassiveCoreDestination event = new EventMassiveCoreDestination(arg, sender, null);
		event.run();
		
		MassiveException exception = event.getException();
		if (exception != null) throw exception;
		
		Destination ret = event.getDestination();
		if (ret == null) throw new MassiveException().addMsg("<b>Unknown destination \"<h>%s<b>\".", arg);
		
		if ( ! ret.hasPs()) throw new MassiveException().addMessage(ret.getMessagePsNull(sender));
		
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return null;
	}
	
}
