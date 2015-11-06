package com.massivecraft.massivecore.command.requirement;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class RequirementIsntCertainSender extends RequirementAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static RequirementIsntCertainSender get(Object senderObject) { return new RequirementIsntCertainSender(senderObject); }
	public RequirementIsntCertainSender(Object senderObject) { this.senderId = IdUtil.getId(senderObject); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String senderId;
	public String getSenderId() { return this.senderId; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		return !this.getSenderId().equalsIgnoreCase(IdUtil.getId(sender));
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		return Txt.parse("<b>Player can't be <h>%s<b>.", Mixin.getDisplayName(this.getSenderId(), sender));
	}
	
}
