package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.command.type.store.TypeSenderColl;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.inactive.InactiveUtil;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class CmdMassiveCoreStorePlayerclean extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreStorePlayerclean()
	{
		// Parameters
		this.addParameter(TypeSet.get(TypeSenderColl.get()), "player coll", true).setDesc("the coll to clean inactive players from");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Set<SenderColl<?>> colls = this.readArg();
		
		Set<CommandSender> receivers = MUtil.set(sender, IdUtil.getConsole());
		
		for (SenderColl<?> coll : colls)
		{
			InactiveUtil.considerRemoveInactive(coll, receivers);
		}
	}
	
}
