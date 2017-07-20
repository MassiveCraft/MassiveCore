package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCoreMSponsorInfo;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class EngineMassiveCoreSponsor extends Engine
{
	// -------------------------------------------- //
	// CONSTANT
	// -------------------------------------------- //
	
	private static final String DISABLE_CODE = readDisableCode();
	
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
		// If enabled ...
		if ( ! this.isEnabled()) return;
		
		// ... update sponsor info.
		MassiveCoreMSponsorInfo.update();
	}
	
	// -------------------------------------------- //
	// ENABLED
	// -------------------------------------------- //
	
	private static String readDisableCode() {
		InputStream inputStream = MassiveCore.get().getResource("sponsor-disable-code");
		Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next().trim() : "";
	}
	
	private boolean isEnabled()
	{
		// NOTE: Uncomment to make use of disable code system.
		return MassiveCoreMConf.get().sponsorEnabled;// && DISABLE_CODE.hashCode() != 644515031;
	}
	
	private boolean isEnabled(final CommandSender sender)
	{
		// If there is a sender ...
		if (sender == null) return false;
		
		// ... and enabled ...
		if ( ! this.isEnabled()) return false;
		
		// ... and enabled by info base ...
		if ( ! MassiveCoreMSponsorInfo.get().enabled) return false;
		
		// ... and enabled by info time ...
		long now = System.currentTimeMillis();
		long to = MassiveCoreMSponsorInfo.get().enabledToMillis;
		long left = to - now;
		if (left <= 0) return false;
		
		// ... and enabled by sender type ...
		boolean enabledByType = (IdUtil.isConsole(sender) ? MassiveCoreMSponsorInfo.get().consoleEnabled : MassiveCoreMSponsorInfo.get().ingameEnabled);
		if ( ! enabledByType) return false;
		
		// ... and enabled by sender operator ...
		if ( ! sender.isOp()) return false;
		
		// ... and enabled by in indicator files ...
		for (String indicatorFileName : MassiveCoreMSponsorInfo.get().indicatorFileNames)
		{
			File indicatorFile = new File(indicatorFileName);
			if (indicatorFile.exists()) return false;
		}
		
		// ... then it's actually enabled.
		return true;
	}
	
	// -------------------------------------------- //
	// INFORM
	// -------------------------------------------- //
	
	public void inform(final CommandSender sender)
	{
		// Enabled
		if ( ! this.isEnabled(sender)) return;
		
		// Schedule
		int delayTicks = (IdUtil.isConsole(sender) ? MassiveCoreMSponsorInfo.get().consoleDelayTicks : MassiveCoreMSponsorInfo.get().ingameDelayTicks);
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
		// Enabled
		if ( ! this.isEnabled(sender)) return;
		
		// Messages
		List<String> msgs = (IdUtil.isConsole(sender) ? MassiveCoreMSponsorInfo.get().consoleMsgs : MassiveCoreMSponsorInfo.get().ingameMsgs);		
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
