package com.massivecraft.mcore4.cmd.arg;

import java.util.Collection;

import com.massivecraft.mcore4.cmd.MCommand;
import com.massivecraft.mcore4.util.Txt;

public abstract class ARAbstractSelect<T> implements ArgReader<T>
{
	public abstract String typename();
	public abstract T select(String str, MCommand mcommand);
	public abstract Collection<String> altNames(MCommand mcommand);
	public boolean canList(MCommand mcommand) { return true; }
	
	@Override
	public ArgResult<T> read(String str, MCommand mcommand)
	{
		ArgResult<T> result = new ArgResult<T>(this.select(str, mcommand));
		
		if (!result.hasResult())
		{
			result.getErrors().add("<b>No "+this.typename()+" matches \"<h>"+str+"<b>\".");
			if (this.canList(mcommand))
			{
				Collection<String> names = this.altNames(mcommand);
				if (names.size() == 0)
				{
					result.getErrors().add("<i>Note: There is no "+this.typename()+" available.");
				}
				else
				{
					result.getErrors().add("<i>Use "+Txt.implodeCommaAndDot(names, "<h>%s", "<i>, ", " <i>or ", "<i>."));
				}
			}
		}
		
		return result;
	}
}
