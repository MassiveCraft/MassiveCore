package com.massivecraft.mcore.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.SenderUtil;
import com.massivecraft.mcore.util.Txt;

public class ReqIsntCertainSender extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsntCertainSender get(CommandSender sender) { return new ReqIsntCertainSender(sender); }
	public ReqIsntCertainSender(CommandSender sender) { this.senderId = SenderUtil.getSenderId(sender); }
	
	public static ReqIsntCertainSender get(String senderId) { return new ReqIsntCertainSender(senderId); }
	public ReqIsntCertainSender(String senderId) { this.senderId = senderId; }
	
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
		return !this.getSenderId().equalsIgnoreCase(SenderUtil.getSenderId(sender));
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return Txt.parse("<b>Player can't be <h>%s<b>.", Mixin.getDisplayName(this.getSenderId()));
	}
	
}
