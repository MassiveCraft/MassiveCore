package com.massivecraft.mcore4.cmd.arg.old;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.usys.Multiverse;
import com.massivecraft.mcore4.usys.MultiverseColl;
import com.massivecraft.mcore4.util.Txt;


public class AHMultiverse extends AHBase<Multiverse>
{
	@Override
	public Multiverse parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		
		Multiverse ret = MultiverseColl.i.get(str);
		
		if (ret == null)
		{
			this.error.add("<b>No multiverse called \"<p>"+str+"<b>\".");
			if (Permission.USYS_MULTIVERSE_LIST.has(sender, false))
			{
				this.error.add("<i>Use "+Txt.implodeCommaAndDot(MultiverseColl.i.getIds(), "<h>%s", "<i>, ", " <i>or ", "<i>."));
			}
		}
		
		return ret;
	}
}
