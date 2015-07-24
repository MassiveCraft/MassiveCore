package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;

public abstract class ARSenderIdAbstract<T> extends ARAbstract<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final SenderIdSource source;
	protected final SenderPresence presence;
	protected final SenderType type;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARSenderIdAbstract(SenderIdSource source, SenderPresence presence, SenderType type)
	{
		if (source == null) throw new NullPointerException("source");
		if (presence == null) throw new NullPointerException("presence");
		if (type == null) throw new NullPointerException("type");
		
		this.source = source;
		this.presence = presence;
		this.type = type;
	}
	
	public ARSenderIdAbstract(SenderIdSource source, SenderPresence presence)
	{
		this(source, presence, SenderType.ANY);
	}
	
	public ARSenderIdAbstract(SenderIdSource source)
	{
		this(source, SenderPresence.ANY);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T getResultForSenderId(String senderId);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		switch (presence)
		{
			case LOCAL:
			case ONLINE: return "online player";
			case OFFLINE: return "offline player";
			case ANY: return "player";
		}
		throw new UnsupportedOperationException("Unknown SenderPresence: " + presence);
	}
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		// arg --> senderId
		String senderId = this.getSenderIdFor(arg);
		// All of our subclasses return null if senderId is null.
		// Thus we don't need to check for that being null, but only check ret.
		
		// Create & populate Ret
		T ret = this.getResultForSenderId(senderId);
		
		if (ret == null)
		{
			// No alternatives found
			throw new MassiveException().addMsg("<b>No %s matches \"<h>%s<b>\".", this.getTypeName(), arg);
		}
	
		// Return Ret
		return ret;
	}
	
	// This is used for arbitrary command order.
	// There might be no matching sender at this time, but that does not matter.
	// As long as the format is correct the arg is valid.
	@Override
	public boolean isValid(String arg, CommandSender sender)
	{
		// Allow names and uuid by format.
		if (MUtil.isValidPlayerName(arg)) return true;
		if (MUtil.isUuid(arg)) return true;
		
		// Check data presence. This handles specials like "@console".
		if (IdUtil.getRegistryIdToSender().containsKey(arg)) return true;
		
		return false;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{	
		// Step 1: Calculate presence.
		SenderPresence presence = this.presence;
		if (presence == SenderPresence.ANY) presence = SenderPresence.ONLINE;
		
		// Special step: We don't tab complete offline players.
		if (presence == SenderPresence.OFFLINE) return Collections.emptySet();

		// Step 2: Calculate type.
		SenderType type = this.type;
		
		// Step 3: Create the ret.
		Set<String> ret = IdUtil.getNames(presence, type);
		
		// Step 4: Return the ret.
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
			if ( ! coll.contains(senderId)) continue;
			
			// ... and the presence check passes ...
			if ( ! IdUtil.getMaintainedIds().contains(senderId, presence, type)) continue;
			
			// ... and the result is non null ...
			T result = this.getResultForSenderId(senderId);
			if (result == null) continue;
			
			// ... then we are go!
			return senderId;
		}
		
		return null;
	}

}
