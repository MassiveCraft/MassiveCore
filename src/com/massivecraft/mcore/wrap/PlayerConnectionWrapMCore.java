package com.massivecraft.mcore.wrap;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import net.minecraft.server.v1_6_R2.ChatMessage;
import net.minecraft.server.v1_6_R2.EnumChatFormat;
import net.minecraft.server.v1_6_R2.SharedConstants;
import net.minecraft.server.v1_6_R2.Packet3Chat;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R2.util.LazyPlayerSet;
import org.bukkit.craftbukkit.v1_6_R2.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import com.massivecraft.mcore.event.MCorePlayerToRecipientChatEvent;

/**
 * This is the MCore PlayerConnectionWrap.
 * It will be injected for all players unless you disable that from the ServerConf.
 * 
 * Currently it adds in the features required for the event MCorePlayerToRecipientChatEvent.
 * 
 * To update void a(Packet3Chat packet3chat):
 * 1. Paste in the method a(Packet3Chat packet3chat):
 * 2. Change the private field references to the public ones.
 * 
 * To update void chat(String s, boolean async):
 * 1. Paste in the method chat(String s, boolean async)
 * 2. Change the private field references to the public ones.
 * 3. Replace the message distribution parts.
 * 
 * https://github.com/Bukkit/CraftBukkit/commits/master/src/main/java/net/minecraft/server/PlayerConnection.java
 * This file was last updated for the following commit:
 * "Fix missed diff for chat packets. Fixes BUKKIT-4666"
 * Wolvereness authored 2013-08-07
 */
@SuppressWarnings("deprecation")
public class PlayerConnectionWrapMCore extends PlayerConnectionWrapAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PlayerConnectionWrapMCore(Player player)
	{
		super(player);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	public void distributeMessage(boolean async, Player sender, Set<Player> recipients, String message, String format)
	{
		// The console shall not have special attention!
		Set<CommandSender> recipientsAndConsole = new LinkedHashSet<CommandSender>(recipients);
		recipientsAndConsole.add(this.getMinecraftServer().console);
		
		// For each of the recipients
		for (CommandSender recipient : recipientsAndConsole)
		{
			// Run the event for this unique recipient
			MCorePlayerToRecipientChatEvent event = new MCorePlayerToRecipientChatEvent(async, sender, recipient, message, format);
			event.run();
			
			// Format and send with the format and message from this recipient's own event. 
			String playerMessage = String.format(event.getFormat(), sender.getDisplayName(), event.getMessage());
			recipient.sendMessage(playerMessage);
		}
		
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("rawtypes")
	@Override
    public void a(Packet3Chat packet3chat)
    {
        if (this.player.getChatFlags() == 2) {
            this.sendPacket(new Packet3Chat(ChatMessage.e("chat.cannotSend").a(EnumChatFormat.RED)));
        } else {
            String s = packet3chat.message;

            if (s.length() > 100) {
                // CraftBukkit start
                if (packet3chat.a_()) {
                    Waitable waitable = new Waitable() {
                        @Override
                        protected Object evaluate() {
                        	PlayerConnectionWrapMCore.this.disconnect("Chat message too long");
                            return null;
                        }
                    };

                    this.getMinecraftServer().processQueue.add(waitable);

                    try {
                        waitable.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.disconnect("Chat message too long");
                }
                // CraftBukkit end
            } else {
                s = StringUtils.normalizeSpace(s);

                for (int i = 0; i < s.length(); ++i) {
                    if (!SharedConstants.isAllowedChatCharacter(s.charAt(i))) {
                        // CraftBukkit start
                        if (packet3chat.a_()) {
                            Waitable waitable = new Waitable() {
                                @Override
                                protected Object evaluate() {
                                    PlayerConnectionWrapMCore.this.disconnect("Illegal characters in chat");
                                    return null;
                                }
                            };

                            this.getMinecraftServer().processQueue.add(waitable);

                            try {
                                waitable.get();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } catch (ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            this.disconnect("Illegal characters in chat");
                        }
                        // CraftBukkit end
                        return;
                    }
                }

                // CraftBukkit start
                if (this.player.getChatFlags() == 1 && !s.startsWith("/")) {
                    this.sendPacket(new Packet3Chat(ChatMessage.e("chat.cannotSend").a(EnumChatFormat.RED)));
                    return;
                }

                this.chat(s, packet3chat.a_());

                // This section stays because it is only applicable to packets
                if (this.getChatSpamField().addAndGet(this, 20) > 200 && !this.getMinecraftServer().getPlayerList().isOp(this.player.getName())) { // CraftBukkit use thread-safe spam
                    if (packet3chat.a_()) {
                        Waitable waitable = new Waitable() {
                            @Override
                            protected Object evaluate() {
                            	PlayerConnectionWrapMCore.this.disconnect("disconnect.spam");
                                return null;
                            }
                        };

                        this.getMinecraftServer().processQueue.add(waitable);

                        try {
                            waitable.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        this.disconnect("disconnect.spam");
                    }
                }
            }
        }
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	public void chat(String s, boolean async)
	{
		if (!this.player.dead) {
            if (s.length() == 0) {
                this.getMinecraftServer().getLogger().warning(this.player.getName() + " tried to send an empty message");
                return;
            }

            if (getPlayer().isConversing()) {
                getPlayer().acceptConversationInput(s);
                return;
            }

            if (s.startsWith("/")) {
                this.handleCommandPublic(s);
                return;
            } else {
                Player player = this.getPlayer();
                AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet());
                this.getServer().getPluginManager().callEvent(event);

                if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
                    // Evil plugins still listening to deprecated event
                    final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
                    queueEvent.setCancelled(event.isCancelled());
                    Waitable waitable = new Waitable() {
                        @Override
                        protected Object evaluate() {
                            org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

                            if (queueEvent.isCancelled()) {
                                return null;
                            }
                            
                            // MCore - start
                            distributeMessage(false, queueEvent.getPlayer(), queueEvent.getRecipients(), queueEvent.getMessage(), queueEvent.getFormat());
                            /*String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
                            minecraftServerPublic.console.sendMessage(message);
                            if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                                for (Object player : minecraftServerPublic.getPlayerList().players) {
                                    ((EntityPlayer) player).sendMessage(ChatMessage.d(message));
                                }
                            } else {
                                for (Player player : queueEvent.getRecipients()) {
                                    player.sendMessage(message);
                                }
                            }*/
                            // MCore - end
                            return null;
                        }};
                    if (async) {
                    	this.getMinecraftServer().processQueue.add(waitable);
                    } else {
                        waitable.run();
                    }
                    try {
                        waitable.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // This is proper habit for java. If we aren't handling it, pass it on!
                    } catch (ExecutionException e) {
                        throw new RuntimeException("Exception processing chat event", e.getCause());
                    }
                } else {
                    if (event.isCancelled()) {
                        return;
                    }
                    
                    // MCore - start
                    distributeMessage(async, event.getPlayer(), event.getRecipients(), event.getMessage(), event.getFormat());
                    /*s = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                    minecraftServerPublic.console.sendMessage(s);
                    if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
                        for (Object recipient : minecraftServerPublic.getPlayerList().players) {
                            ((EntityPlayer) recipient).sendMessage(ChatMessage.d(s));
                        }
                    } else {
                        for (Player recipient : event.getRecipients()) {
                            recipient.sendMessage(s);
                        }
                    }*/
                    // MCore - end
                }
            }
        }

        return;
	}

}
