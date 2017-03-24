package com.massivecraft.massivecore.command;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.sender.TypeSenderId;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.PermissionUtil;
import com.massivecraft.massivecore.util.Txt;

import java.util.List;

public abstract class MassiveCommandSet<T> extends MassiveCommand
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

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public MassiveCommandSet(Type<T> type)
	{
		this(type, TypeSenderId.get());
	}

	public MassiveCommandSet(Type<T> type, Type<String> typeSenderId)
	{
		this.type = type;

		// Parameters
		this.addParameter(type);
		this.addParameter(typeSenderId, "player", "you");
	}
	
	// -------------------------------------------- //
	// VALUE
	// -------------------------------------------- //
	
	public abstract T getValue(String senderId) throws MassiveException;
	
	public abstract void setValue(T value, String senderId) throws MassiveException;
	
	// -------------------------------------------- //
	// NAME
	// -------------------------------------------- //
	
	public String getName()
	{
		List<String> aliases = this.getAliases();
		return aliases.get(aliases.size() - 1);
	}

	public String getTargetDesc(String targetId, String watcherId)
	{
		if (targetId.equals(watcherId))
		{
			return Txt.parse("<i>Your <h>%s", this.getName());
		}
		else
		{
			return Txt.parse("<h>%s <i>of %s", Txt.upperCaseFirst(this.getName()), MixinDisplayName.get().getDisplayName(targetId, watcherId));
		}
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


		T before = this.getValue(targetId);
		String afterDesc = this.getType().getVisual(after, sender);
		String targetDesc = this.getTargetDesc(targetId, senderId);
		
		// NoChange
		if (after == before)
		{
			throw new MassiveException().addMsg("%s<i> is already <h>%s<i>.", targetDesc, afterDesc);
		}
		
		// Apply
		this.setValue(after, targetId);
		
		// Inform
		msg("%s<i> is now <h>%s<i>.", targetDesc, afterDesc);

		// Inform target
		if (!targetId.equals(senderId))
		{
			MixinMessage.get().msgOne(targetId, "%s<i> is now <h>%s<i>.", getTargetDesc(targetId, targetId), afterDesc);
		}
	}

}
