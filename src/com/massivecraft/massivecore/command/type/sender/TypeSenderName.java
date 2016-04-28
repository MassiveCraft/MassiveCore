package com.massivecraft.massivecore.command.type.sender;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;
import com.massivecraft.massivecore.util.IdUtil;

public class TypeSenderName extends TypeSenderIdAbstract<String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private TypeSenderName(SenderIdSource source, SenderPresence presence, SenderType type)
	{
		super(String.class, source, presence, type);
	}
	
	private TypeSenderName(SenderIdSource source, SenderPresence presence)
	{
		super(String.class, source, presence);
	}
	
	private TypeSenderName(SenderIdSource source, SenderType type)
	{
		super(String.class, source, type);
	}
	
	private TypeSenderName(SenderIdSource source)
	{
		super(String.class, source);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static final TypeSenderName i = new TypeSenderName(SenderIdSourceMixinAllSenderIds.get());
	public static TypeSenderName get() { return i; }
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public static TypeSenderName get(SenderIdSource source, SenderPresence presence, SenderType type) { return new TypeSenderName(source, presence, type); }
	public static TypeSenderName get(SenderIdSource source, SenderPresence presence) { return new TypeSenderName(source, presence); }
	public static TypeSenderName get(SenderIdSource source, SenderType type) { return new TypeSenderName(source, type); }
	public static TypeSenderName get(SenderIdSource source) { return new TypeSenderName(source); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getResultForSenderId(String senderId)
	{
		return IdUtil.getName(senderId);
	}

}
