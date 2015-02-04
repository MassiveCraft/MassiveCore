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
	public T read(String arg, CommandSender sender) throws MassiveCommandException
	{
		T result = this.select(arg, sender);
		
		if (result == null)
		{
			MassiveCommandException exception = new MassiveCommandException();
			exception.addMsg("<b>No %s matches \"<h>%s<b>\".", this.typename(), arg);
			if (this.canList(sender))
			{
				Collection<String> names = this.altNames(sender);
				if (names.size() == 0)
				{
					exception.addMsg("<i>Note: There is no %s available.", this.typename());
				}
				else
				{
					String format = Txt.parse("<h>%s");
					String comma = Txt.parse("<i>, ");
					String and = Txt.parse(" <i>or ");
					String dot = Txt.parse("<i>.");
					exception.addMsg("<i>Use %s", Txt.implodeCommaAndDot(names, format, comma, and, dot));
				}
			}
			
			throw exception;
		}
		
		return result;
	}
	
}
