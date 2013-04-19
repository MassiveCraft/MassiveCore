package com.massivecraft.mcore.integration.vault;

import com.massivecraft.mcore.integration.IntegrationFeaturesAbstract;
import com.massivecraft.mcore.money.MoneyMixinVault;

public class VaultFeatures extends IntegrationFeaturesAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static VaultFeatures i = new VaultFeatures();
	public static VaultFeatures get() { return i; }
	private VaultFeatures() { super("Vault"); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void activate()
	{
		MoneyMixinVault.get().activate();
	}
	
	@Override
	public void deactivate()
	{
		MoneyMixinVault.get().deactivate();
	}
	
}
