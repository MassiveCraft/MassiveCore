package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.SoundEffect;

public class ARSoundEffect extends ArgReaderAbstract<SoundEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARSoundEffect i = new ARSoundEffect();
	public static ARSoundEffect get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public SoundEffect read(String arg, CommandSender sender) throws MassiveException
	{
		SoundEffect ret;
		
		try
		{
			ret = SoundEffect.valueOf(arg);
		}
		catch (Exception e)
		{
			throw new MassiveException().addMsg("<b>%s", e.getMessage());
		}
		return ret;
	}
	
}
