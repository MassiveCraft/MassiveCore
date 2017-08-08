package com.massivecraft.massivecore.command.type.sender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinVisibility;
import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class TypeSenderIdAbstract<T> extends TypeAbstract<T>
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
	
	public TypeSenderIdAbstract(Class<?> clazz, SenderIdSource source, SenderPresence presence, SenderType type)
	{
		super(clazz);
		if (source == null) throw new NullPointerException("source");
		if (presence == null) throw new NullPointerException("presence");
		if (type == null) throw new NullPointerException("type");
		
		this.source = source;
		this.presence = presence;
		this.type = type;
	}
	
	public TypeSenderIdAbstract(Class<?> clazz, SenderIdSource source, SenderPresence presence)
	{
		this(clazz, source, presence, SenderType.ANY);
	}
	
	public TypeSenderIdAbstract(Class<?> clazz, SenderIdSource source, SenderType type)
	{
		this(clazz, source, SenderPresence.ANY, type);
	}
	
	public TypeSenderIdAbstract(Class<?> clazz, SenderIdSource source)
	{
		this(clazz, source, SenderPresence.ANY);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T getResultForSenderId(String senderId);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
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
	public String getVisualInner(T value, CommandSender sender)
	{
		String id = IdUtil.getId(value);
		if (id == null) return null;
		
		return MixinDisplayName.get().getDisplayName(id, sender);
	}

	@Override
	public String getNameInner(T value)
	{
		return IdUtil.getName(value);
	}

	@Override
	public String getIdInner(T value)
	{
		return IdUtil.getId(value);
	}
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		// arg --> senderId
		String senderId = this.getSenderIdFor(arg);
		// All of our subclasses return null if senderId is null.
		// Thus we don't need to check for that being null, but only check ret.

		// If presence is online or local and the target is not visible for the sender then throw an error.
		if
		(
			(this.presence == SenderPresence.LOCAL || this.presence == SenderPresence.ONLINE)
			&&
			senderId != null
			&&
			!MixinVisibility.get().isVisible(senderId, sender)
		)
		{
			throwError(arg);
		}

		// Create & populate Ret
		T ret = this.getResultForSenderId(senderId);
		
		if (ret == null) throwError(arg);
	
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
		
		// Step 3: Get Ids
		Set<String> ids = IdUtil.getIds(presence, type);
		
		// Step 4: Create Ret with visible names
		boolean addIds = (arg.length() >= TAB_LIST_UUID_THRESHOLD);
		int size = ids.size();
		if (addIds) size *= 2;
		Set<String> ret = new MassiveSet<>(size);
		for (String id : ids)
		{
			if ( ! MixinVisibility.get().isVisible(id, sender)) continue;
			if (addIds) ret.add(id);
			String name = IdUtil.getName(id);
			if (name == null) continue;
			ret.add(name);
		}
		
		// Step 5: Return the ret.
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	public void throwError(String arg) throws MassiveException
	{
		throw new MassiveException().addMessage(this.getErrorMessageForArg(arg));
	}

	public String getErrorMessageForArg(String arg)
	{
		return Txt.parse("<b>No %s matches \"<h>%s<b>\".", this.getName(), arg);
	}
	
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
			// NOTE: Certain SenderColls will on purpose contain other things than players.
			// NOTE: For that reason we only check sender presence and type if a certain one is required.
			if (this.presence != SenderPresence.ANY || this.type != SenderType.ANY)
			{
				if ( ! IdUtil.getMaintainedIds().contains(senderId, this.presence, this.type)) continue;
			}
			
			// ... and the result is non null ...
			T result = this.getResultForSenderId(senderId);
			if (result == null) continue;
			
			// ... then we are go!
			return senderId;
		}
		
		return null;
	}

}
