package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.json.simple.JSONObject;

public class NmsChat extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsChat d = new NmsChat().setAlternatives(
		NmsChat112R1P.class,
		NmsChat18R2P.class,
		NmsChat18R1.class,
		NmsChatFallback.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsChat i = d;
	public static NmsChat get() { return i; }
	
	// -------------------------------------------- //
	// CHAT
	// -------------------------------------------- //
	
	public void sendChatMson(Object sendeeObject, Mson mson)
	{
		CommandSender sendee = IdUtil.getSender(sendeeObject);
		if (sendee == null) return;
		
		String plain = mson.toPlain(true);
		sendChatPlain(sendee, plain);
	}
	
	public void sendChatPlain(Object sendeeObject, String plain) {
		CommandSender sendee = IdUtil.getSender(sendeeObject);
		if (sendee == null) return;
		
		MassiveCoreMConf mconf = MassiveCoreMConf.get();
		if (mconf != null && !mconf.consoleColorsEnabled && sendee instanceof ConsoleCommandSender) {
			plain = ChatColor.stripColor(plain);
		}
		
		sendee.sendMessage(plain);
	}
	
	// -------------------------------------------- //
	// TITLE
	// -------------------------------------------- //
	
	public void sendTitleRaw(Object sendeeObject, int ticksIn, int ticksStay, int ticksOut, String rawMain, String rawSub)
	{
		
	}
	
	public void sendTitleMson(Object sendeeObject, int ticksIn, int ticksStay, int ticksOut, Mson msonMain, Mson msonSub)
	{
		String rawMain = msonMain.toRaw();
		String rawSub = msonSub.toRaw();
		
		this.sendTitleRaw(sendeeObject, ticksIn, ticksStay, ticksOut, rawMain, rawSub);
	}
	
	public void sendTitleMessage(Object sendeeObject, int ticksIn, int ticksStay, int ticksOut, String messageMain, String messageSub)
	{
		// If we don't send any message (empty is ok) we might end up displaying old messages.
		if (messageMain == null) messageMain = "";
		if (messageSub == null) messageSub = "";

		String rawMain = messageToRaw(messageMain);
		String rawSub = messageToRaw(messageSub);
		
		this.sendTitleRaw(sendeeObject, ticksIn, ticksStay, ticksOut, rawMain, rawSub);
	}
	
	public void sendTitleMsg(Object sendeeObject, int ticksIn, int ticksStay, int ticksOut, String msgMain, String msgSub)
	{
		String messageMain = Txt.parse(msgMain);
		String messageSub = Txt.parse(msgSub);
		
		this.sendTitleMessage(sendeeObject, ticksIn, ticksStay, ticksOut, messageMain, messageSub);
	}
	
	// -------------------------------------------- //
	// ACTIONBAR
	// -------------------------------------------- //
	
	public void sendActionbarRaw(Object sendeeObject, String raw)
	{
		
	}
	
	public void sendActionbarMson(Object sendeeObject, Mson mson)
	{
		String message = mson.toRaw();
		
		this.sendActionbarRaw(sendeeObject, message);
	}
	
	public void sendActionbarMessage(Object sendeeObject, String message)
	{
		message = messageToRaw(message);

		this.sendActionbarRaw(sendeeObject, message);
	}

	public void sendActionbarMsg(Object sendeeObject, String msg)
	{
		String message = Txt.parse(msg);
		
		this.sendActionbarMessage(sendeeObject, message);
	}
	
	// -------------------------------------------- //
	// MESSAGE TO RAW
	// -------------------------------------------- //

	public static String messageToRaw(String message)
	{
		message = JSONObject.escape(message);
		message = "{\"text\": \"" + message + "\"}";
		return message;
	}
	
}
