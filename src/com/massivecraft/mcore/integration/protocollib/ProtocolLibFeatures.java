package com.massivecraft.mcore.integration.protocollib;

import com.massivecraft.mcore.integration.IntegrationFeaturesAbstract;

public class ProtocolLibFeatures extends IntegrationFeaturesAbstract 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ProtocolLibFeatures() { super("ProtocolLib"); }
	private static ProtocolLibFeatures i = new ProtocolLibFeatures();
	public static ProtocolLibFeatures get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void activate()
	{
		EntityPotionColorPacketAdapter.get().setup();
	}
	
}
