package com.massivecraft.massivecore.integration.vault;

import com.massivecraft.massivecore.Integration;
import com.massivecraft.massivecore.money.MoneyMixinVault;

public class IntegrationVault extends Integration
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IntegrationVault i = new IntegrationVault();
	public static IntegrationVault get() { return i; }
	private IntegrationVault()
	{
		this.setPluginName("Vault");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setIntegrationActiveInner(boolean active)
	{
		if (active)
		{
			MoneyMixinVault.get().activate();
		}
		else
		{
			MoneyMixinVault.get().deactivate();
		}
	}
	
}
