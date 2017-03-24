package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.event.EventMassiveCoreDestination;
import com.massivecraft.massivecore.event.EventMassiveCoreDestinationTabList;
import com.massivecraft.massivecore.teleport.Destination;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class TypeDestination extends TypeAbstract<Destination>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDestination i = new TypeDestination();
	public static TypeDestination get() { return i; }
	public TypeDestination() { super(Destination.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(Destination value, CommandSender sender)
	{
		return value.getDesc(sender);
	}
	
	@Override
	public Destination read(String arg, CommandSender sender) throws MassiveException
	{
		EventMassiveCoreDestination event = new EventMassiveCoreDestination(arg, sender, null);
		event.run();
		
		MassiveException exception = event.getException();
		if (exception != null) throw exception;
		
		Destination ret = event.getDestination();
		if (ret == null) throw new MassiveException().addMsg("<b>Unknown destination \"<h>%s<b>\".", arg);
		
		// Throw exeption if ps is null.
		ret.getPs(sender);
		
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		EventMassiveCoreDestinationTabList event = new EventMassiveCoreDestinationTabList(arg, sender);
		event.run();
		return event.getSuggestions();
	}
	
}
