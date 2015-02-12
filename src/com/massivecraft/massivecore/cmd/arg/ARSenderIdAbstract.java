package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
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
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		// Create Ret
		T ret;
		
		// arg --> senderId
		String senderId = this.getSenderIdFor(arg);
		// All of our subclasses return null if senderId is null
		// Thus we don't need to check for that being null, but only check ret
		
		// Populate Ret
		ret = this.getResultForSenderId(senderId);
		
		if (ret == null)
		{
			// No alternatives found
			throw new MassiveException().addMsg("<b>No player matches \"<h>%s<b>\".", arg);
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
