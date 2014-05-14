package com.massivecraft.mcore.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.IdUtil;
import com.massivecraft.mcore.util.Txt;

public class ReqIsntCertainSender extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsntCertainSender get(Object senderObject) { return new ReqIsntCertainSender(senderObject); }
	public ReqIsntCertainSender(Object senderObject) { this.senderId = IdUtil.getId(senderObject); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String senderId;
	public String getSenderId() { return this.senderId; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MCommand command)
	{
		return !this.getSenderId().equalsIgnoreCase(IdUtil.getId(sender));
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return Txt.parse("<b>Player can't be <h>%s<b>.", Mixin.getDisplayName(this.getSenderId()));
	}
	
}
