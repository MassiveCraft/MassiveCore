package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.command.type.store.TypeColl;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.cleanable.Cleanable;
import com.massivecraft.massivecore.store.cleanable.CleaningUtil;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class CmdMassiveCoreStoreClean extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreStoreClean()
	{
		// Parameters
		this.addParameter(TypeSet.get(TypeColl.get()), "coll", true).setDesc("the coll to clean");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Set<Coll<? extends Cleanable>> colls = this.readArg();
		
		Set<CommandSender> receivers = MUtil.set(sender, IdUtil.getConsole());
		
		for (Coll<? extends Cleanable> coll : colls)
		{
			CleaningUtil.considerClean(coll, receivers);
		}
	}
	
}
