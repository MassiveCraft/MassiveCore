package com.massivecraft.massivecore.integration.vault;

import com.massivecraft.massivecore.integration.IntegrationAbstract;
import com.massivecraft.massivecore.money.MoneyMixinVault;

public class IntegrationVault extends IntegrationAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IntegrationVault i = new IntegrationVault();
	public static IntegrationVault get() { return i; }
	private IntegrationVault() { super("Vault"); }
	
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
