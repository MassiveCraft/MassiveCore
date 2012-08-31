package com.massivecraft.mcore4.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.util.PlayerUtil;
import com.massivecraft.mcore4.util.Txt;

public class AHPlayerWrapperNew<E> extends AHBase<E>
{
	final Coll<E, ?> coll;
	public Coll<E, ?> coll() { return this.coll; };
	
	public AHPlayerWrapperNew(Coll<E, ?> coll)
	{
		this.coll = coll;
	}
	
	@Override
	public E parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		if (str == null) return null;
		
		E ret;
		
		if (style != null && style.equalsIgnoreCase("match"))
		{
			@SuppressWarnings("unchecked")
			Collection<String> ids = (Collection<String>) this.coll().ids();
			String id = Txt.getBestCIStart(ids, str);
			ret = this.coll().get(id);
			if (ret != null)
			{
				return ret;
			}
			this.error.add("<b>No player name begins with \"<p>"+str+"<b>\".");
		}
		else if (style != null && style.equalsIgnoreCase("matchany"))
		{
			ret = this.coll().get(Txt.getBestCIStart(PlayerUtil.getAllVisitorNames(), str));
			if (ret != null)
			{
				return ret;
			}
			this.error.add("<b>No player name begins with \"<p>"+str+"<b>\".");
		}
		else
		{
			ret = this.coll().get(str);
			if (ret != null)
			{
				return ret;
				
			}
			this.error.add("<b>No player with the exact name \"<p>"+str+"<b>\".");
		}
		return null;
	}
}
