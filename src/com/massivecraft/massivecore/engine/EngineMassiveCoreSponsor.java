package com.massivecraft.massivecore.engine;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCoreMSponsorInfo;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class EngineMassiveCoreSponsor extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreSponsor i = new EngineMassiveCoreSponsor();
	public static EngineMassiveCoreSponsor get() { return i; }
	public EngineMassiveCoreSponsor()
	{
		this.setPeriod(1L);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;

		// We delay informing the console.
		// This is because the console may not exist when this engine is activated.
		Bukkit.getScheduler().runTask(this.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				ConsoleCommandSender console = IdUtil.getConsole();
				inform(console);
			}
		});
	}
	
	// -------------------------------------------- //
	// UPDATE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		MassiveCoreMSponsorInfo.update();
	}
	
	// -------------------------------------------- //
	// INFORM
	// -------------------------------------------- //
	
	public void inform(final CommandSender sender)
	{
		// Fail Safe
		if (sender == null) return;
		
		// If enabled by mconf ...
		if ( ! MassiveCoreMConf.get().sponsorEnabled) return;
		
		// ... and enabled by info base ...
		if ( ! MassiveCoreMSponsorInfo.get().enabled) return;
		
		// ... and enabled by info time ...
		long now = System.currentTimeMillis();
		long to = MassiveCoreMSponsorInfo.get().enabledToMillis;
		long left = to - now;
		if (left <= 0) return;
		
		// ... and enabled by sender type ...
		boolean isConsole = IdUtil.isConsole(sender);
		boolean enabledByType = (isConsole ? MassiveCoreMSponsorInfo.get().consoleEnabled : MassiveCoreMSponsorInfo.get().ingameEnabled);
		if ( ! enabledByType) return;
		
		// ... and enabled by sender operator ...
		if ( ! sender.isOp()) return;
		
		// ... and enabled by in indicator files ...
		for (String indicatorFileName : MassiveCoreMSponsorInfo.get().indicatorFileNames)
		{
			File indicatorFile = new File(indicatorFileName);
			if (indicatorFile.exists()) return;
		}
		
		// ... then schedule inner inform.
		int delayTicks = (isConsole ? MassiveCoreMSponsorInfo.get().consoleDelayTicks : MassiveCoreMSponsorInfo.get().ingameDelayTicks);
		Bukkit.getScheduler().runTaskLater(this.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				informInner(sender);
			}
		}, delayTicks);
	}
	
	public void informInner(CommandSender sender)
	{
		// Console?
		boolean isConsole = IdUtil.isConsole(sender);
		
		// Messages
		List<String> msgs = (isConsole ? MassiveCoreMSponsorInfo.get().consoleMsgs : MassiveCoreMSponsorInfo.get().ingameMsgs);		
		String senderVisual = MixinDisplayName.get().getDisplayName(sender, sender);
		for (String msg : msgs)
		{
			String message = Txt.parse(msg);
			message = message.replace("{p}", senderVisual);
			Mson mson = Mson.fromParsedMessage(message).link(MassiveCoreMSponsorInfo.get().ingameLink);
			MixinMessage.get().messageOne(sender, mson);
		}
		
		// Sound
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			SoundEffect.runAll(MassiveCoreMSponsorInfo.get().ingameSoundEffects, player);
		}
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
		Player player = event.getPlayer();
		this.inform(player);
    }
	
}
