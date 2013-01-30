package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.mixin.Mixin;
import com.massivecraft.mcore5.util.Txt;

public abstract class ARSenderIdAbstract<T> implements ArgReader<T>
{
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
			String senderId = Mixin.tryFix(arg);
			ret.setResult(this.getResultForSenderId(senderId));
		}
		else
		{
			// Ambigious!
			ret.getErrors().add("<b>Online "+this.getTypename()+" matching \"<h>"+arg+"<b>\" is ambigious.");
			if (senderIds.size() > 10)
			{
				// To many to list
				ret.getErrors().add("<b>Could be any of <h>"+senderIds.size()+" <b>possible alternatives.");
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
