package com.massivecraft.mcore4.cmd.arg.old;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.usys.Aspect;
import com.massivecraft.mcore4.usys.AspectColl;
import com.massivecraft.mcore4.util.Txt;

public class AHAspect extends AHBase<Aspect>
{
	@Override
	public Aspect parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		
		Aspect ret = AspectColl.i.get(str);
		
		if (ret == null)
		{
			this.error.add("<b>No aspect called \"<p>"+str+"<b>\".");
			if (Permission.USYS_ASPECT_LIST.has(sender, false))
			{
				this.error.add("<i>Use "+Txt.implodeCommaAndDot(AspectColl.i.ids(), "<h>%s", "<i>, ", " <i>or ", "<i>."));
			}
		}
		
		return ret;
	}
}
