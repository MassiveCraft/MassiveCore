package com.massivecraft.massivecore.command.type.sender;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.store.SenderIdSource;
import com.massivecraft.massivecore.store.SenderIdSourceMixinAllSenderIds;

public class TypeSenderId extends TypeSenderIdAbstract<String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private TypeSenderId(SenderIdSource source, SenderPresence presence, SenderType type)
	{
		super(source, presence, type);
	}
	
	private TypeSenderId(SenderIdSource source, SenderPresence presence)
	{
		super(source, presence);
	}
	
	
	private TypeSenderId(SenderIdSource source, SenderType type)
	{
		super(source, type);
	}
	
	
	private TypeSenderId(SenderIdSource source)
	{
		super(source);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static final TypeSenderId i = new TypeSenderId(SenderIdSourceMixinAllSenderIds.get());
	public static TypeSenderId get() { return i; }
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public static TypeSenderId get(SenderIdSource source, SenderPresence presence, SenderType type) { return new TypeSenderId(source, presence, type); }
	public static TypeSenderId get(SenderIdSource source, SenderPresence presence) { return new TypeSenderId(source, presence); }
	public static TypeSenderId get(SenderIdSource source, SenderType type) { return new TypeSenderId(source, type); }
	public static TypeSenderId get(SenderIdSource source) { return new TypeSenderId(source); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getResultForSenderId(String senderId)
	{
		return senderId;
	}

}
