package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommandException;
import com.massivecraft.massivecore.util.Txt;

public abstract class ARAbstractSelect<T> extends ArgReaderAbstract<T>
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract String typename();
	public abstract T select(String str, CommandSender sender);
	public abstract Collection<String> altNames(CommandSender sender);
	public boolean canList(CommandSender sender) { return this.altNames(sender).size() < 50; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T read(String arg, CommandSender sender)
	{
		T result = this.select(arg, sender);
		
		if (result == null)
		{
			MassiveCommandException errors = new MassiveCommandException();
			errors.addErrorMsg("<b>No " + this.typename() + " matches \"<h>"+arg+"<b>\".");
			if (this.canList(sender))
			{
				Collection<String> names = this.altNames(sender);
				if (names.size() == 0)
				{
					errors.addErrorMsg("<i>Note: There is no "+this.typename()+" available.");
				}
				else
				{
					errors.addErrorMsg("<i>Use "+Txt.implodeCommaAndDot(names, "<h>%s", "<i>, ", " <i>or ", "<i>."));
				}
			}
			
			throw errors;
		}
		
		return result;
	}
	
}
