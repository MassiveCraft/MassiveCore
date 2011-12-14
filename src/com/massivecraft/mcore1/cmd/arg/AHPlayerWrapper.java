package com.massivecraft.mcore1.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore1.MPlugin;
import com.massivecraft.mcore1.persist.IClassManager;
import com.massivecraft.mcore1.persist.Persist;
import com.massivecraft.mcore1.util.PlayerUtil;

public abstract class AHPlayerWrapper<T> extends AHBase<T>
{
	public abstract Class<T> getClazz();
	
	public IClassManager<T> getManager(MPlugin p)
	{
		return p.persist.getManager(getClazz());
	}
	
	@Override
	public T parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error = null;
		if (str == null) return null;
		
		IClassManager<T> manager = this.getManager(p);
		T ret;
		
		if (style != null && style.equalsIgnoreCase("match"))
		{
			ret = manager.getBestMatch(str);
			if (ret != null)
			{
				return ret;
			}
			this.error = "<b>No player name begins with \"<p>"+str+"<b>\".";
		}
		else if (style != null && style.equalsIgnoreCase("matchany"))
		{
			ret = manager.get(Persist.getBestCIStart(PlayerUtil.getAllVisitorNames(), str));
			if (ret != null)
			{
				return ret;
			}
			this.error = "<b>No player name begins with \"<p>"+str+"<b>\".";
		}
		else
		{
			ret = manager.get(str);
			if (ret != null)
			{
				return ret;
				
			}
			this.error = "<b>No player with the exact name \"<p>"+str+"<b>\".";
		}
		return null;
	}
}
