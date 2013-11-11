package com.massivecraft.mcore.integration.protocollib;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.massivecraft.mcore.MCore;

public class EntityPotionColorPacketAdapter extends PacketAdapter
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private EntityPotionColorPacketAdapter() { super(MCore.get(), ConnectionSide.SERVER_SIDE, ListenerPriority.NORMAL, Packets.Server.ENTITY_METADATA); }
	private static EntityPotionColorPacketAdapter i = new EntityPotionColorPacketAdapter();
	public static EntityPotionColorPacketAdapter get() { return i; }
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		ProtocolLibrary.getProtocolManager().addPacketListener(this);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		try
		{
			// If the server is sending a meta-data packet to a sendee player...
			// NOTE: That must be the case. We are listening to no other situation.
			final PacketContainer packet = event.getPacket();
			final Player sendee = event.getPlayer();
			
			// ... fetch the entity ...
			// NOTE: MetaData packets are only sent to players in the same world.
			final Entity entity = packet.getEntityModifier(sendee.getWorld()).read(0);
			
			// Fireworks cannot have potion effects! They also reuse index 8 
			// for sending their item stack, causing a crash if we don't bail out now.
			if (entity instanceof Firework) return;
			
			// ... fetch the metadata ...
			final List<WrappedWatchableObject> metadata = packet.getWatchableCollectionModifier().read(0);
			
			// ... for each watchable in the metadata ...
			for (WrappedWatchableObject watchable : metadata)
			{
				// If the watchable is about potion effect color ...
				if (watchable.getIndex() != 7) continue;
				
				// ... run our custom async event to allow changing it ...
				int oldColor = (Integer) watchable.getValue();
				MCoreEntityPotionColorEvent colorEvent = new MCoreEntityPotionColorEvent(sendee, entity, oldColor);
				colorEvent.run();
				int newColor = colorEvent.getColor();
				
				// ... alter if changed.
				if (newColor != oldColor) watchable.setValue(newColor, false);
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught "+e.getClass().getName()+" exception in EntityPotionColorPacketAdapter#onPacketSending");
			System.out.println("Message: "+e.getMessage());
			System.out.println("Stack Trace:");
			e.printStackTrace();
		}
	}
	
}
