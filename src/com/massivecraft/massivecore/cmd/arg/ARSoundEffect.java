package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.collections.MassiveSet;

public class ARSoundEffect extends ARAbstractException<SoundEffect>
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
	public SoundEffect valueOf(String arg, CommandSender sender) throws Exception
	{
		return SoundEffect.valueOf(arg);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new MassiveSet<String>();
		
		for (String str : ARSound.get().getTabList(sender, arg))
		{
			ret.add(str + "-");
		}
		
		return ret;
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return false;
	}

}
