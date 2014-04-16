package com.massivecraft.mcore;

import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.IndexUniqueField;
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
	// FIELD
	// -------------------------------------------- //
	
	private IndexUniqueField<String, MCoreMPlayer> indexName = new IndexUniqueField<String, MCoreMPlayer>(new TreeMap<String, MCoreMPlayer>(String.CASE_INSENSITIVE_ORDER));
	public IndexUniqueField<String, MCoreMPlayer> getIndexName() { return this.indexName; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void postAttach(MCoreMPlayer entity, String id)
	{
		super.postAttach(entity, id);
		this.getIndexName().update(entity, entity.getName());
	}
	
	@Override
	public void postDetach(MCoreMPlayer entity, String id)
	{
		super.postDetach(entity, id);
		this.getIndexName().removeObject(entity);
	}
	
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
				MCoreMPlayer mplayer = this.getIndexName().getObject(string);
				if (mplayer != null) return mplayer.getId();
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
