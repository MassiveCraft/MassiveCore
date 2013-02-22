package com.massivecraft.mcore.cmd.arg;

import java.util.Collection;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.store.SenderIdSource;

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
		arg = arg.toLowerCase();
		
		TreeSet<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (Collection<String> coll : this.source.getSenderIdCollections())
		{
			for (String senderId : coll)
			{
				if (this.isSenderIdOk(senderId, arg, sender))
				{
					ret.add(senderId);
					if (ret.size() >= MAX_COUNT)
					{
						return ret;
					}
				}
			}
		}
		
		return ret;
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
