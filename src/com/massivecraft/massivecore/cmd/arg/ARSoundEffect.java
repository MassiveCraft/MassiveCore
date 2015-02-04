package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.cmd.MassiveCommandException;

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
	public SoundEffect read(String arg, CommandSender sender) throws MassiveCommandException
	{
		SoundEffect ret;
		
		try
		{
			ret = SoundEffect.valueOf(arg);
		}
		catch (Exception e)
		{
			throw new MassiveCommandException().addMsg("<b>%s", e.getMessage());
		}
		return ret;
	}
	
}
