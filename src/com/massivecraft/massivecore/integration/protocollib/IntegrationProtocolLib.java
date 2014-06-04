package com.massivecraft.massivecore.integration.protocollib;

import com.massivecraft.massivecore.integration.IntegrationAbstract;

public class IntegrationProtocolLib extends IntegrationAbstract 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private IntegrationProtocolLib() { super("ProtocolLib"); }
	private static IntegrationProtocolLib i = new IntegrationProtocolLib();
	public static IntegrationProtocolLib get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void activate()
	{
		EntityPotionColorPacketAdapter.get().setup();
	}
	
}
