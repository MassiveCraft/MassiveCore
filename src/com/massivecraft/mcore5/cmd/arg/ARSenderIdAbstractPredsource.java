package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.store.SenderIdSource;

public abstract class ARSenderIdAbstractPredsource<T> extends ARSenderIdAbstract<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final SenderIdSource source;
	private final String typename;
	private final ArgPredictate<String> argPredictate;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARSenderIdAbstractPredsource(SenderIdSource source, String typename, ArgPredictate<String> argPredictate)
	{
		this.source = source;
		this.typename = typename;
		this.argPredictate = argPredictate;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypename()
	{
		return this.typename;
	}
	
	@Override
	public Collection<String> getSenderIdsFor(String arg, CommandSender sender)
	{
		Collection<String> senderIds = this.source.getSenderIds();
		arg = arg.toLowerCase();
		
		Iterator<String> iter = senderIds.iterator();
		while(iter.hasNext())
		{
			String senderId = iter.next();
			if (this.isSenderIdOk(senderId, arg, sender)) continue;
			iter.remove();
		}
		
		return senderIds;
	}
	
	protected boolean isSenderIdOk(String senderId, String arg, CommandSender sender)
	{
		// If the predictate applies ...
		if (!this.argPredictate.apply(senderId, arg, sender)) return false;
		
		// ... and the result is non null ...
		T result = this.getResultForSenderId(senderId);
		if (result == null) return false;
		
		// ... then the senderId is ok.
		return true;
	}
	
	
}
