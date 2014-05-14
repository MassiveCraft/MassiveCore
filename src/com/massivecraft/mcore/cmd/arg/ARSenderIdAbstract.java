package com.massivecraft.mcore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.IdUtil;
import com.massivecraft.mcore.util.Txt;

public abstract class ARSenderIdAbstract<T> extends ArgReaderAbstract<T>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static int MAX_COUNT = 10;
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract String getTypename();
	
	public abstract T getResultForSenderId(String senderId);
	
	public abstract Collection<String> getSenderIdsFor(String arg, CommandSender sender);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ArgResult<T> read(String arg, CommandSender sender)
	{
		ArgResult<T> ret = new ArgResult<T>();
		
		Collection<String> senderIds = this.getSenderIdsFor(arg, sender);
		
		if (senderIds.size() == 0)
		{
			// No alternatives found
			ret.setErrors("<b>No "+this.getTypename()+" matches \"<h>"+arg+"<b>\".");
		}
		else if (senderIds.size() == 1)
		{
			// Only one alternative found
			String senderId = senderIds.iterator().next();
			ret.setResult(this.getResultForSenderId(senderId));
		}
		else if (senderIds.contains(arg))
		{
			// Exact match
			String senderId = IdUtil.getName(arg);
			ret.setResult(this.getResultForSenderId(senderId));
		}
		else
		{
			// Ambigious!
			ret.getErrors().add("<b>"+this.getTypename()+" matching \"<h>"+arg+"<b>\" is ambigious.");
			if (senderIds.size() >= MAX_COUNT)
			{
				// To many to list
				ret.getErrors().add("<b>More than "+MAX_COUNT+" possible alternatives.");
			}
			else
			{
				// List the alternatives
				ret.getErrors().add("<b>Did you mean "+Txt.implodeCommaAndDot(senderIds, "<h>%s", "<b>, ", " <b>or ", "<b>?"));
			}
		}
	
		return ret;
	}

}
