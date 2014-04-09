package com.massivecraft.mcore;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;
import com.massivecraft.mcore.util.MUtil;

public class MCoreMPlayerColl extends Coll<MCoreMPlayer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MCoreMPlayerColl i = new MCoreMPlayerColl();
	public static MCoreMPlayerColl get() { return i; }
	public MCoreMPlayerColl()
	{
		super("mcore_mplayer", MCoreMPlayer.class, MStore.getDb(), MCore.get(), false, false, true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String fixId(Object oid)
	{
		if (oid == null) return null;
		
		if (oid instanceof MCoreMPlayer)
		{
			return this.entity2id.get(oid);
		}
		
		if (oid instanceof String)
		{
			String string = (String)oid;
			string = string.toLowerCase();
			
			// Handle Player Name
			if (MUtil.isValidPlayerName(string))
			{
				// TODO: Improve the speed of this using an index!
				for (MCoreMPlayer mplayer : this.getAll())
				{
					String name = mplayer.getName();
					if (name == null) continue;
					if (!string.equals(name.toLowerCase())) continue;
					return mplayer.getId();
				}
			}
				
			return string;
		}
		
		if (oid instanceof UUID)
		{
			UUID uuid = (UUID)oid;
			return uuid.toString();
		}
		
		if (oid instanceof Player)
		{
			Player player = (Player)oid;
			return player.getUniqueId().toString();
		}
			
		return null;
	}
	
}
