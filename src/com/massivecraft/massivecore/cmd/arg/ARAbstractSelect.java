package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;

public abstract class ARAbstractSelect<T> extends ArgReaderAbstract<T>
{
	// -------------------------------------------- //
	// CONSTANT
	// -------------------------------------------- //
	
	public static final int LIST_COUNT_MAX = 50;
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract String typename();
	public abstract T select(String str, CommandSender sender) throws MassiveException;
	public abstract Collection<String> altNames(CommandSender sender);
	public boolean canList(CommandSender sender) { return true; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		T result = this.select(arg, sender);
		
		if (result == null)
		{
			MassiveException exception = new MassiveException();
			exception.addMsg("<b>No %s matches \"<h>%s<b>\".", this.typename(), arg);
			
			if (this.canList(sender))
			{
				Collection<String> names = this.altNames(sender);
				if (names.size() == 0)
				{
					exception.addMsg("<i>Note: There is no %s available.", this.typename());
				}
				else if (names.size() > LIST_COUNT_MAX)
				{
					exception.addMsg("<i>More than %d alternatives available.", LIST_COUNT_MAX);
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
