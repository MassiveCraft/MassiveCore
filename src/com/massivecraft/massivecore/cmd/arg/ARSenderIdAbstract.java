package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.util.IdUtil;

public abstract class ARSenderIdAbstract<T> extends ArgReaderAbstract<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final SenderIdSource source;
	private final boolean online;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARSenderIdAbstract(SenderIdSource source, boolean online)
	{
		this.source = source;
		this.online = online;
	}
	
	public ARSenderIdAbstract(SenderIdSource source)
	{
		this(source, false);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T getResultForSenderId(String senderId);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ArgResult<T> read(String arg, CommandSender sender)
	{
		// Create Ret
		ArgResult<T> ret = new ArgResult<T>();
		
		// arg --> senderId
		String senderId = this.getSenderIdFor(arg);
		
		// Populate Ret
		if (senderId == null)
		{
			// No alternatives found
			ret.setErrors("<b>No player matches \"<h>"+arg+"<b>\".");
		}
		else
		{
			ret.setResult(this.getResultForSenderId(senderId));
		}
	
		// Return Ret
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public String getSenderIdFor(String arg)
	{
		// Get senderId from the arg.
		// Usually it's just the lowercase form.
		// It might also be a name resolution.
		String senderId = arg.toLowerCase();
		String betterId = IdUtil.getId(senderId);
		if (betterId != null) senderId = betterId;
		
		for (Collection<String> coll : this.source.getSenderIdCollections())
		{
			// If the senderId exists ...
			if (!coll.contains(senderId)) continue;
			
			// ... and the online check passes ...
			if (this.online && !Mixin.isOnline(senderId)) continue;
			
			// ... and the result is non null ...
			T result = this.getResultForSenderId(senderId);
			if (result == null) continue;
			
			// ... then we are go!
			return senderId;
		}
		
		return null;
	}

}
