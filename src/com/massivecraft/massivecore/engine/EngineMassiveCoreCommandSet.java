package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.PlayerValue;
import com.massivecraft.massivecore.command.type.RegistryType;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.event.EventMassiveCoreCommandSet;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.io.Serializable;

public class EngineMassiveCoreCommandSet extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreCommandSet i = new EngineMassiveCoreCommandSet();
	public static EngineMassiveCoreCommandSet get() { return i; }
	
	// -------------------------------------------- //
	// RUN
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public <T extends Serializable> void setValueListener(EventMassiveCoreCommandSet<T> event)
	{
		try
		{
			this.setValue(event);
		}
		catch (MassiveException e)
		{
			MixinMessage.get().messageOne(event.getSenderId(), e.getMessages());
		}
	}
	
	
	public <T extends Serializable> void setValue(EventMassiveCoreCommandSet<T> event) throws MassiveException
	{
		String senderId = event.getSenderId();
		String targetId = event.getTargetId();
		T after = event.getValue();
		PlayerValue<T> playerValue = event.getPlayerValue();
		String name = event.getName();
		
		Player player = IdUtil.getPlayer(targetId);
		if (player == null) return;
		
		T before = playerValue.getValue(player);
		
		Type<T> type = (Type<T>) RegistryType.getType(after.getClass());
		String afterDesc = type.getVisual(after);
		String targetDesc = this.getTargetDesc(targetId, senderId, name);
		
		// NoChange
		if (after == before)
		{
			throw new MassiveException().addMsg("%s<i> is already <h>%s<i>.", targetDesc, afterDesc);
		}
		
		// Apply
		playerValue.setValue(after, player);
		
		// Inform
		MixinMessage.get().msgOne(senderId, "%s<i> is now <h>%s<i>.", targetDesc, afterDesc);
		
		// Inform target
		if (!targetId.equals(senderId))
		{
			MixinMessage.get().msgOne(targetId, "%s<i> is now <h>%s<i>.", getTargetDesc(targetId, targetId, name), afterDesc);
		}
	}
	
	public String getTargetDesc(String targetId, String watcherId, String name)
	{
		if (targetId.equals(watcherId))
		{
			return Txt.parse("<i>Your <h>%s", name);
		}
		else
		{
			return Txt.parse("<h>%s <i>of %s", Txt.upperCaseFirst(name), MixinDisplayName.get().getDisplayName(targetId, watcherId));
		}
	}
	
}
