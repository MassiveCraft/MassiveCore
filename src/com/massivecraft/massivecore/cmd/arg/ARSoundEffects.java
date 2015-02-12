package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.SoundEffect;

/**
 * @deprecated use ARList
 */
@Deprecated
public class ARSoundEffects extends ArgReaderAbstract<List<SoundEffect>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARSoundEffects i = new ARSoundEffects();
	public static ARSoundEffects get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<SoundEffect> read(String arg, CommandSender sender) throws MassiveException
	{
		List<SoundEffect> ret = new ArrayList<SoundEffect>();
		List<SoundEffect> result = new ArrayList<SoundEffect>();
		
		arg = arg.trim();
		List<String> soundStrings = Arrays.asList(arg.split("\\s+"));
		
		try
		{
			for (String soundString : soundStrings)
			{
				result.add(SoundEffect.valueOf(soundString));
			}
			ret = result;
		}
		catch (Exception e)
		{
			throw new MassiveException().addMsg("<b>%s", e.getMessage());
		}
		return ret;
	}
	
}
