package com.massivecraft.massivecore.command;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.sender.TypeSenderId;
import com.massivecraft.massivecore.event.EventMassiveCoreCommandSet;
import com.massivecraft.massivecore.mixin.MixinEvent;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.PermissionUtil;

import java.io.Serializable;
import java.util.List;

public abstract class MassiveCommandSetSharding<T extends Serializable> extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //

	private final Type<T> type;
	public Type<T> getType() { return this.type; }

	private String permSetOther;
	public String getPermSetOther() { return permSetOther; }
	public void setPermSetOther(String permSetOther) { this.permSetOther = permSetOther; }
	public boolean hasPermSetOther() { return this.getPermSetOther() != null; }
	
	private PlayerValue<T> playerValue;
	public PlayerValue<T> getPlayerValue() { return playerValue; }
	public void setPlayerValue(PlayerValue<T> playerValue) { this.playerValue = playerValue; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public MassiveCommandSetSharding(Type<T> type)
	{
		this(type, TypeSenderId.get());
	}

	public MassiveCommandSetSharding(Type<T> type, Type<String> typeSenderId)
	{
		this.type = type;

		// Parameters
		this.addParameter(type);
		this.addParameter(typeSenderId, "player", "you");
	}
	
	// -------------------------------------------- //
	// NAME
	// -------------------------------------------- //
	
	public String getName()
	{
		List<String> aliases = this.getAliases();
		return aliases.get(aliases.size() - 1);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		T after = this.readArg();

		String senderId = IdUtil.getId(sender);
		String targetId = this.readArg(senderId);
		boolean other = !targetId.equals(senderId);

		// Check other
		if (other && this.hasPermSetOther())
		{
			if (!PermissionUtil.hasPermission(sender, this.getPermSetOther(), true)) return;
		}
		
		EventMassiveCoreCommandSet event = new EventMassiveCoreCommandSet<T>(senderId, targetId, after, this.getPlayerValue(), this.getName());
		MixinEvent.get().distribute(event, true);
	}

}
