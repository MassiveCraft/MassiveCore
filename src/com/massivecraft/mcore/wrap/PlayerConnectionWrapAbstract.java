package com.massivecraft.mcore.wrap;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.util.LazyPlayerSet;
import org.bukkit.craftbukkit.v1_6_R2.CraftServer;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.massivecraft.mcore.store.accessor.AccessorUtil;
import com.massivecraft.mcore.store.accessor.PropertyAccessor;

import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.Packet;
import net.minecraft.server.v1_6_R2.Packet0KeepAlive;
import net.minecraft.server.v1_6_R2.Packet101CloseWindow;
import net.minecraft.server.v1_6_R2.Packet102WindowClick;
import net.minecraft.server.v1_6_R2.Packet106Transaction;
import net.minecraft.server.v1_6_R2.Packet107SetCreativeSlot;
import net.minecraft.server.v1_6_R2.Packet108ButtonClick;
import net.minecraft.server.v1_6_R2.Packet130UpdateSign;
import net.minecraft.server.v1_6_R2.Packet15Place;
import net.minecraft.server.v1_6_R2.Packet16BlockItemSwitch;
import net.minecraft.server.v1_6_R2.Packet18ArmAnimation;
import net.minecraft.server.v1_6_R2.Packet19EntityAction;
import net.minecraft.server.v1_6_R2.Packet202Abilities;
import net.minecraft.server.v1_6_R2.Packet203TabComplete;
import net.minecraft.server.v1_6_R2.Packet204LocaleAndViewDistance;
import net.minecraft.server.v1_6_R2.Packet205ClientCommand;
import net.minecraft.server.v1_6_R2.Packet250CustomPayload;
import net.minecraft.server.v1_6_R2.Packet255KickDisconnect;
import net.minecraft.server.v1_6_R2.Packet3Chat;
import net.minecraft.server.v1_6_R2.Packet7UseEntity;
import net.minecraft.server.v1_6_R2.Packet9Respawn;
import net.minecraft.server.v1_6_R2.Packet14BlockDig;
import net.minecraft.server.v1_6_R2.Packet10Flying;
import net.minecraft.server.v1_6_R2.MinecraftServer;
import net.minecraft.server.v1_6_R2.Packet27PlayerInput;
import net.minecraft.server.v1_6_R2.PlayerConnection;

/**
 * It's sometime fun to wrap the NMS PlayerConnection with a version that behaves slightly differently.
 * This class is an unmodified wrap to be used just for that.
 * Just extend this class with the features you want and then instantiate for the player.
 * It's perfectly fine to wrap a wrap the wraps a wrap and so on.
 * 
 * Great care should be taken when updating this class to a new NMS version.
 * Follow the following steps:
 * 
 * 1. Make sure that all public methods are overriden to call the this.inner.
 * 2. Take a look at the method constructor. Does it do something new fancy?
 * 3. Where are the public fields altered? Make sure to update them accordingly. (remember to check mc-dev as well as CraftBukkit)
 * 4. handleCommandPublic method update though copy paste and minor adjustments.
 * 
 * https://github.com/Bukkit/CraftBukkit/commits/master/src/main/java/net/minecraft/server/PlayerConnection.java
 * This file was last updated for the following commit:
 * "Fix missed diff for chat packets. Fixes BUKKIT-4666"
 * Wolvereness authored 2013-08-07
 */
public abstract class PlayerConnectionWrapAbstract extends PlayerConnection
{
	// -------------------------------------------- //
	// STATIC UTILS
	// -------------------------------------------- //
	
	public static PlayerConnection getPlayerConnection(Player player)
	{
		CraftPlayer cplayer = (CraftPlayer)player;
		EntityPlayer eplayer = cplayer.getHandle();
		return eplayer.playerConnection;
	}
	
	public static void setPlayerConnection(Player player, PlayerConnection playerConnection)
	{
		CraftPlayer cplayer = (CraftPlayer)player;
		EntityPlayer eplayer = cplayer.getHandle();
		eplayer.playerConnection = playerConnection;
	}
	
	// -------------------------------------------- //
	// FIEDS
	// -------------------------------------------- //
	
	private final PlayerConnection inner;
	
	// -------------------------------------------- //
	// RAW NATIVE FIELD THEORY
	// -------------------------------------------- //
	
	// public final INetworkManager networkManager;
	// This one should never change since it's final.
	// Using the very same must thus be fine.
	
	// private final MinecraftServer minecraftServer;
	// Same argumentation as above.
	
    // public boolean disconnected;
	// This one is set in two locations only from within this class
	// 1. public void a(String s, Object[] aobject)
	// 2. public void disconnect(String s)
	// For that reason we update the reference upwards at those locations.
	
    // public EntityPlayer player;
	// This one is set in two locations only from within this class
	// 1. The constructor
	// 2. public void a(Packet205ClientCommand packet205clientcommand)
	// For that reason we update the reference upwards at those locations.
	
	// public boolean checkMovement = true; // CraftBukkit - private -> public
	// This one was made public but I do not see why.
	// It's only used from within this class.
	// Thus we do not need to keep it updated in the wrapper.
	
	// -------------------------------------------- //
	// MAKING STUFF PUBLIC
	// -------------------------------------------- //
	
	public final static transient PropertyAccessor minecraftServerAccessor = AccessorUtil.createPropertyAccessor(PlayerConnection.class, "minecraftServer");
	public MinecraftServer getMinecraftServer()
	{
		return (MinecraftServer) minecraftServerAccessor.get(this);
	}
	
	public final static transient PropertyAccessor serverAccessor = AccessorUtil.createPropertyAccessor(PlayerConnection.class, "server");
	public CraftServer getServer()
	{
		return (CraftServer) serverAccessor.get(this);
	}
	
	public final static transient PropertyAccessor chatSpamFieldAccessor = AccessorUtil.createPropertyAccessor(PlayerConnection.class, "chatSpamField");
	@SuppressWarnings("unchecked")
	public AtomicIntegerFieldUpdater<PlayerConnection> getChatSpamField()
	{
		return (AtomicIntegerFieldUpdater<PlayerConnection>) chatSpamFieldAccessor.get(this);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	// Note that instantiating this class or a subclass of this class will cause the PlayerConnection to be injected.
	// It's designed that way in the vanilla NMS superclass.
	
	public PlayerConnectionWrapAbstract(Player player)
	{
		this(getPlayerConnection(player));
	}
	
	public PlayerConnectionWrapAbstract(PlayerConnection inner)
	{
		super(inner.player.server, inner.networkManager, inner.player);
		
		this.inner = inner;
		
		this.disconnected = inner.disconnected;
		this.checkMovement = inner.checkMovement;
	}
	
	// -------------------------------------------- //
	// WRAP
	// -------------------------------------------- //
	
	@Override
    public CraftPlayer getPlayer()
    {
    	return this.inner.getPlayer();
    }
    
	@Override
    public void e()
    {
    	this.inner.e();
    }
    
	@Override
    public void disconnect(String s)
    {
    	this.inner.disconnect(s);
    	this.disconnected = this.inner.disconnected;
    }
    
	@Override
    public void a(Packet27PlayerInput packet27playerinput)
    {
    	this.inner.a(packet27playerinput);
    }
    
	@Override
    public void a(Packet10Flying packet10flying)
    {
    	this.inner.a(packet10flying);
    }
	
	@Override
    public void a(double d0, double d1, double d2, float f, float f1)
    {
    	this.inner.a(d0, d1, d2, f, f1);
    }
    
	@Override
    public void teleport(Location dest)
    {
    	this.inner.teleport(dest);
    }
    
	@Override
    public void a(Packet14BlockDig packet14blockdig)
    {
    	this.inner.a(packet14blockdig);
    }
    
	@Override
    public void a(Packet15Place packet15place)
    {
    	this.inner.a(packet15place);
    }
    
	@Override
    public void a(String s, Object[] aobject)
    {
    	this.inner.a(s, aobject);
    	this.disconnected = this.inner.disconnected;
    }
    
	@Override
    public void onUnhandledPacket(Packet packet)
	{
    	this.inner.onUnhandledPacket(packet);
    }
    
	@Override
    public void sendPacket(Packet packet)
	{
    	this.inner.sendPacket(packet);
    }
    
	@Override
    public void a(Packet16BlockItemSwitch packet16blockitemswitch)
	{
    	this.inner.a(packet16blockitemswitch);
    }
    
	@Override
    public void a(Packet3Chat packet3chat)
    {
    	this.inner.a(packet3chat);
    }
    
	@Override
    public void chat(String s, boolean async)
    {
    	this.inner.chat(s, async);
    }
	
    public void handleCommandPublic(String s)
	{
        // CraftBukkit start
        CraftPlayer player = this.getPlayer();

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet());
        this.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        try {
            this.getMinecraftServer().getLogger().info(event.getPlayer().getName() + " issued server command: " + event.getMessage()); // CraftBukkit
            if (this.getServer().dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
                return;
            }
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(PlayerConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return;
        }
        // CraftBukkit end

        /* CraftBukkit start - No longer needed as we have already handled it in server.dispatchServerCommand above.
        this.minecraftServer.getCommandHandler().a(this.player, s);
        // CraftBukkit end */
    }
    
	@Override
    public void a(Packet18ArmAnimation packet18armanimation)
    {
    	this.inner.a(packet18armanimation);
    }
    
	@Override
    public void a(Packet19EntityAction packet19entityaction)
    {
    	this.inner.a(packet19entityaction);
    }
    
	@Override
    public void a(Packet255KickDisconnect packet255kickdisconnect)
	{
    	this.inner.a(packet255kickdisconnect);
    }
    
	@Override
    public int lowPriorityCount()
    {
    	return this.inner.lowPriorityCount();
    }
    
	@Override
    public void a(Packet7UseEntity packet7useentity)
	{
    	this.inner.a(packet7useentity);
    }
    
	@Override
    public void a(Packet205ClientCommand packet205clientcommand)
    {
    	this.inner.a(packet205clientcommand);
    	this.player = this.inner.player;
    }
    
	@Override
    public boolean b()
    {
    	return this.inner.b();
    }
    
	@Override
    public void a(Packet9Respawn packet9respawn)
    {
    	this.inner.a(packet9respawn);
    }
    
	@Override
    public void handleContainerClose(Packet101CloseWindow packet101closewindow)
    {
    	this.inner.handleContainerClose(packet101closewindow);
    }
    
	@Override
    public void a(Packet102WindowClick packet102windowclick)
    {
    	this.inner.a(packet102windowclick);
    }
    
	@Override
    public void a(Packet108ButtonClick packet108buttonclick)
    {
    	this.inner.a(packet108buttonclick);
    }
    
	@Override
    public void a(Packet107SetCreativeSlot packet107setcreativeslot)
    {
    	this.inner.a(packet107setcreativeslot);
    }
    
	@Override
    public void a(Packet106Transaction packet106transaction)
    {
    	this.inner.a(packet106transaction);
    }
    
	@Override
    public void a(Packet130UpdateSign packet130updatesign)
    {
    	this.inner.a(packet130updatesign);
    }
    
	@Override
    public void a(Packet0KeepAlive packet0keepalive)
    {
    	this.inner.a(packet0keepalive);
    }
    
	@Override
    public boolean a()
    {
    	return this.inner.a();
    }
    
	@Override
    public void a(Packet202Abilities packet202abilities)
    {
    	this.inner.a(packet202abilities);
    }
    
	@Override
    public void a(Packet203TabComplete packet203tabcomplete)
    {
    	this.inner.a(packet203tabcomplete);
    }
    
	@Override
    public void a(Packet204LocaleAndViewDistance packet204localeandviewdistance)
    {
    	this.inner.a(packet204localeandviewdistance);
    }
    
	@Override
    public void a(Packet250CustomPayload packet250custompayload)
    {
    	this.inner.a(packet250custompayload);
    }
    
	@Override
    public boolean c()
    {
    	return this.inner.c();
    }
    
}
