package com.massivecraft.massivecore.nms;

import java.util.logging.Level;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.particleeffect.ParticleEffect.ParticlePacket;

public abstract class NmsAbstract
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final int DEFAULT_REQUIRED_VERSION = 7;
	
	public static final String BUG_TRACKER_URL = "https://github.com/MassiveCraft/MassiveCore/issues";
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean available = false;
	public boolean isAvailable() { return this.available; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public NmsAbstract()
	{
		try
		{
			if (this.isAvailableForCurrentVersion())
			{
				setup();
				available = true;
				
				/*String name = this.getClass().getSimpleName();
				name = name.substring(3);
				name = name + "s";
				String msg = String.format("The NMS util for %s was enabled.", name);
				MassiveCore.get().log(msg);*/
			}
		}
		catch (Throwable t)
		{
			String errorMsg = String.format("If you use 1.%s.X or above, please report this error at %s", this.getRequiredVersion(), BUG_TRACKER_URL);
			MassiveCore.get().log(Level.INFO, errorMsg);
			t.printStackTrace();
			
			available = false;
		}
	}
	
	protected abstract void setup() throws Throwable;
	
	// -------------------------------------------- //
	// VERSIONING
	// -------------------------------------------- //
	
	public static int getCurrentVersion()
	{
		return ParticlePacket.getVersion();
	}
	
	public int getRequiredVersion()
	{
		return DEFAULT_REQUIRED_VERSION;
	}
	
	public boolean isAvailableForCurrentVersion()
	{
		return getCurrentVersion() >= this.getRequiredVersion();
	}
	
}
